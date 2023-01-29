package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.exception.ImportFileParsingException;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingAppointment;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingClient;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingPurchase;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingService;
import com.marchal.christophe.phoresttechtest.salon.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ImportService {
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;
    private final CsvParser parser;
    private final MigrationModelConverter converter;
    private final PurchaseRepository purchaseRepository;

    public ImportService(ClientRepository clientRepository,
                         ProductRepository productRepository,
                         ServiceRepository serviceRepository,
                         AppointmentRepository appointmentRepository, CsvParser parser, MigrationModelConverter converter,
                         PurchaseRepository purchaseRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
        this.parser = parser;
        this.converter = converter;
        this.purchaseRepository = purchaseRepository;
    }

    public void importClientCsv(InputStream inputStream) {
        try {
            List<MigratingClient> clients = parser.parseCsv(MigratingClient.class, inputStream);
            clientRepository.saveAll(clients.stream().map(converter::toClient).toList());
        } catch (IOException e) {
            throw new ImportFileParsingException("Failed to parse client csv file", e);
        }

    }

    public void importAppointmentCsv(InputStream inputStream) {
        try {
            List<MigratingAppointment> migratingAppointments = parser.parseCsv(MigratingAppointment.class, inputStream);
            Set<UUID> clientIds = migratingAppointments.stream().map(MigratingAppointment::clientId).collect(Collectors.toSet());
            Iterable<Client> clients = clientRepository.findAllById(clientIds);
            Map<UUID, Client> clientsByUUID = StreamSupport.stream(clients.spliterator(), false)
                    .collect(Collectors.groupingBy(Client::getId, Collectors.reducing(new Client(), (a, b) -> b)));
            List<Appointment> appointments = migratingAppointments.stream()
                    .map(appointment -> converter.toAppointment(appointment, clientsByUUID.get(appointment.clientId())))
                    .toList();
            appointmentRepository.saveAll(appointments);
        } catch (IOException e) {
            throw new ImportFileParsingException("Failed to parse appointment csv file", e);
        }
    }

    public void importPurchaseCsv(InputStream purchasesIs) {
        try {
            List<MigratingPurchase> migratingPurchases = parser.parseCsv(MigratingPurchase.class, purchasesIs);
            Set<UUID> appointmentIds = migratingPurchases.stream().map(MigratingPurchase::appointmentId).collect(Collectors.toSet());
            Iterable<Appointment> appointments = appointmentRepository.findAllById(appointmentIds);
            Map<UUID, Appointment> appointmentsById = StreamSupport.stream(appointments.spliterator(), false)
                    .collect(Collectors.groupingBy(Appointment::getId, Collectors.reducing(new Appointment(), (a, b) -> b)));
            List<Purchase> purchases = migratingPurchases.stream()
                    .map(purchase -> converter.toPurchase(purchase, appointmentsById.get(purchase.appointmentId())))
                    .toList();
            purchaseRepository.saveAll(purchases);
            extractAndSaveProducts(purchases);
        } catch (IOException e) {
            throw new ImportFileParsingException("Failed to parse purchase csv file", e);
        }
    }

    private void extractAndSaveProducts(List<Purchase> purchases) {
        List<Product> products = purchases
                .stream().collect(Collectors.groupingBy(Purchase::byProduct)).keySet()
                .stream().map(p -> new Product(p.name(), p.price(), p.loyaltyPoints())).toList();

        productRepository.saveAll(products);
    }

    private void extractAndSaveServices(List<Purchase> purchases) {
        List<com.marchal.christophe.phoresttechtest.salon.Service> products = purchases
                .stream().collect(Collectors.groupingBy(Purchase::byProduct)).keySet()
                .stream().map(p -> new com.marchal.christophe.phoresttechtest.salon.Service(p.name(), p.price(), p.loyaltyPoints())).toList();

        serviceRepository.saveAll(products);
    }

    public void importServiceCsv(InputStream servicesIs) {
        try {
            List<MigratingService> migratingServices = parser.parseCsv(MigratingService.class, servicesIs);
            Set<UUID> appointmentIds = migratingServices.stream().map(MigratingService::appointmentId).collect(Collectors.toSet());
            Iterable<Appointment> appointments = appointmentRepository.findAllById(appointmentIds);
            Map<UUID, Appointment> appointmentsById = StreamSupport.stream(appointments.spliterator(), false)
                    .collect(Collectors.groupingBy(Appointment::getId, Collectors.reducing(new Appointment(), (a, b) -> b)));
            List<Purchase> purchases = migratingServices.stream()
                    .map(service -> converter.toPurchase(service, appointmentsById.get(service.appointmentId())))
                    .toList();
            purchaseRepository.saveAll(purchases);
            extractAndSaveServices(purchases);
        } catch (IOException e) {
            throw new ImportFileParsingException("Failed to parse purchase csv file", e);
        }
    }
}
