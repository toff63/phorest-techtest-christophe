package com.marchal.christophe.phoresttechtest.migration;

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
    private final MigratingValidator validator;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;
    private final CsvParser parser;
    private final MigrationModelConverter converter;
    private final PurchaseRepository purchaseRepository;

    public ImportService(MigratingValidator validator,
                         ClientRepository clientRepository,
                         ProductRepository productRepository,
                         ServiceRepository serviceRepository,
                         AppointmentRepository appointmentRepository,
                         PurchaseRepository purchaseRepository,
                         CsvParser parser,
                         MigrationModelConverter converter) {
        this.validator = validator;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
        this.parser = parser;
        this.converter = converter;
        this.purchaseRepository = purchaseRepository;
    }

    public ImportEntityStatus<MigratingClient> importClientCsv(InputStream inputStream) throws IOException {
        List<MigratingClient> clients = parser.parseCsv(MigratingClient.class, inputStream);
        ValidatedEntity<MigratingClient> validatedEntity = validator.validate(clients);
        clientRepository.saveAll(validatedEntity.validEntities().stream().map(converter::toClient).toList());
        return new ImportEntityStatus<MigratingClient>(validatedEntity.validEntities().size(), validatedEntity.errors());
    }

    public ImportEntityStatus<Appointment> importAppointmentCsv(InputStream inputStream) throws IOException {
        List<MigratingAppointment> migratingAppointments = parser.parseCsv(MigratingAppointment.class, inputStream);
        List<Appointment> appointments = conciliateAppointmentsWithClients(migratingAppointments);
        ValidatedEntity<Appointment> validatedEntity = validator.validate(appointments);
        appointmentRepository.saveAll(validatedEntity.validEntities());
        return new ImportEntityStatus<Appointment>(validatedEntity.validEntities().size(), validatedEntity.errors());
    }

    private List<Appointment> conciliateAppointmentsWithClients(List<MigratingAppointment> migratingAppointments) {
        Set<UUID> clientIds = migratingAppointments.stream().map(MigratingAppointment::clientId).collect(Collectors.toSet());
        Iterable<Client> clients = clientRepository.findAllById(clientIds);
        Map<UUID, Client> clientsByUUID = StreamSupport.stream(clients.spliterator(), false)
                .collect(Collectors.groupingBy(Client::getId, Collectors.reducing(new Client(), (a, b) -> b)));
        return migratingAppointments.stream()
                .map(appointment -> converter.toAppointment(appointment, clientsByUUID.get(appointment.clientId())))
                .toList();
    }

    public ImportEntityStatus<Purchase> importPurchaseCsv(InputStream purchasesIs) throws IOException {
        Function<ProductOrServiceDTO, Product> productConstructor = p -> new Product(p.name(), p.price(), p.loyaltyPoints());
        return importSoldEntity(purchasesIs, MigratingPurchase.class, productConstructor, productRepository);
    }

    public ImportEntityStatus<Purchase> importServiceCsv(InputStream servicesIs) throws IOException {
        Function<ProductOrServiceDTO, com.marchal.christophe.phoresttechtest.salon.Service> serviceConstructor =
                p -> new com.marchal.christophe.phoresttechtest.salon.Service(p.name(), p.price(), p.loyaltyPoints());
        return importSoldEntity(servicesIs, MigratingService.class, serviceConstructor, serviceRepository);
    }

    private <T extends MigratingSoldEntity, E> ImportEntityStatus<Purchase> importSoldEntity(InputStream servicesIs, Class<T> clazz, Function<ProductOrServiceDTO, E> entityConstructor, CrudRepository<E, UUID> repository) throws IOException {
        List<T> migratingServices = parser.parseCsv(clazz, servicesIs);
        List<Purchase> purchases = conciliatePurchasesWithAppointments(migratingServices);
        ValidatedEntity<Purchase> validatedEntity = validator.validate(purchases);
        purchaseRepository.saveAll(validatedEntity.validEntities());
        extractAndSaveDerivedEntity(purchases, entityConstructor, repository);
        return new ImportEntityStatus<Purchase>(validatedEntity.validEntities().size(), validatedEntity.errors());
    }

    private <T extends MigratingSoldEntity> List<Purchase> conciliatePurchasesWithAppointments(List<T> migratingServices) {
        Set<UUID> appointmentIds = migratingServices.stream().map(MigratingSoldEntity::appointmentId).collect(Collectors.toSet());
        Iterable<Appointment> appointments = appointmentRepository.findAllById(appointmentIds);
        Map<UUID, Appointment> appointmentsById = StreamSupport.stream(appointments.spliterator(), false)
                .collect(Collectors.groupingBy(Appointment::getId, Collectors.reducing(new Appointment(), (a, b) -> b)));
        return migratingServices.stream()
                .map(entity -> converter.toPurchase(entity, appointmentsById.get(entity.appointmentId())))
                .toList();
    }

    private <E> void extractAndSaveDerivedEntity(List<Purchase> purchases, Function<ProductOrServiceDTO, E> entityConstructor, CrudRepository<E, UUID> repository) {
        List<E> products = purchases
                .stream().collect(Collectors.groupingBy(Purchase::byProduct)).keySet()
                .stream().map(entityConstructor).toList();
        repository.saveAll(products);
    }

    // TODO Look for a better way to handle CSV parsing failure.
    public ImportStatus importSalon(InputStream clientFile, InputStream appointmentFile, InputStream purchaseFile, InputStream servicesFile) {
        ImportEntityStatus failedToParseCsvStatus = new ImportEntityStatus(0, Set.of(new ImportError("", "CSV could not be parse")));
        ImportEntityStatus<MigratingClient> clientStatus = failedToParseCsvStatus;
        ImportEntityStatus<Appointment> appointmentStatus = null;
        ImportEntityStatus<Purchase> purchaseStatus = null;
        ImportEntityStatus<Purchase> serviceStatus = null;
        try {
            clientStatus = this.importClientCsv(clientFile);
            appointmentStatus = failedToParseCsvStatus;
            appointmentStatus = this.importAppointmentCsv(appointmentFile);
            purchaseStatus = failedToParseCsvStatus;
            purchaseStatus = this.importPurchaseCsv(purchaseFile);
            serviceStatus = failedToParseCsvStatus;
            serviceStatus = this.importServiceCsv(servicesFile);
            return new ImportStatus(clientStatus, appointmentStatus, purchaseStatus, serviceStatus);
        } catch (IOException e) {
            return new ImportStatus(clientStatus, appointmentStatus, purchaseStatus, serviceStatus);
        }

    }
}
