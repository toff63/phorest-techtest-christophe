package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.exception.ImportFileParsingException;
import com.marchal.christophe.phoresttechtest.migration.model.*;
import com.marchal.christophe.phoresttechtest.salon.*;
import com.marchal.christophe.phoresttechtest.salon.dto.ProductOrServiceDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
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
        Function<ProductOrServiceDTO, Product> productConstructor = p -> new Product(p.name(), p.price(), p.loyaltyPoints());
        importSoldEntity(purchasesIs, MigratingPurchase.class, productConstructor, productRepository);
    }

    public void importServiceCsv(InputStream servicesIs) {
        Function<ProductOrServiceDTO, com.marchal.christophe.phoresttechtest.salon.Service> serviceConstructor =
                p -> new com.marchal.christophe.phoresttechtest.salon.Service(p.name(), p.price(), p.loyaltyPoints());
        importSoldEntity(servicesIs, MigratingService.class, serviceConstructor, serviceRepository);
    }

    private <T extends MigratingSoldEntity, E> void importSoldEntity(InputStream servicesIs, Class<T> clazz, Function<ProductOrServiceDTO, E> entityConstructor, CrudRepository<E, UUID> repository) {
        try {
            List<T> migratingServices = parser.parseCsv(clazz, servicesIs);
            Set<UUID> appointmentIds = migratingServices.stream().map(MigratingSoldEntity::appointmentId).collect(Collectors.toSet());
            Iterable<Appointment> appointments = appointmentRepository.findAllById(appointmentIds);
            Map<UUID, Appointment> appointmentsById = StreamSupport.stream(appointments.spliterator(), false)
                    .collect(Collectors.groupingBy(Appointment::getId, Collectors.reducing(new Appointment(), (a, b) -> b)));
            List<Purchase> purchases = migratingServices.stream()
                    .map(entity -> converter.toPurchase(entity, appointmentsById.get(entity.appointmentId())))
                    .toList();
            purchaseRepository.saveAll(purchases);
            extractAndSaveDerivedEntity(purchases, entityConstructor, repository);
        } catch (IOException e) {
            throw new ImportFileParsingException("Failed to parse purchase csv file", e);
        }
    }

    private <E> void extractAndSaveDerivedEntity(List<Purchase> purchases, Function<ProductOrServiceDTO, E> entityConstructor, CrudRepository<E, UUID> repository) {
        List<E> products = purchases
                .stream().collect(Collectors.groupingBy(Purchase::byProduct)).keySet()
                .stream().map(entityConstructor).toList();
        repository.saveAll(products);
    }
}
