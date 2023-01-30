package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.migration.model.MigratingClient;
import com.marchal.christophe.phoresttechtest.salon.*;
import com.marchal.christophe.phoresttechtest.salon.dto.ProductOrServiceDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ImportServiceTest {

    private static Validator validator;
    @Autowired
    private ImportService importService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void deleteAllBeforeTest() {
        purchaseRepository.deleteAll();
        productRepository.deleteAll();
        serviceRepository.deleteAll();
        appointmentRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testImportClientCsv() {
        InputStream is = CsvParserTest.class.getResourceAsStream("/clients.csv");
        try {
            ImportEntityStatus<MigratingClient> result = importService.importClientCsv(is);
            Iterable<Client> clientsIt = clientRepository.findAll();
            assertEquals(4, result.numberOfImportedEntities());
            List<Client> clients = StreamSupport.stream(clientsIt.spliterator(), false).toList();
            assertEquals(4, clients.size());
            clients.forEach(client -> assertEquals(0, validator.validate(client).size()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testImportClientCsvWithInvalids() {
        InputStream is = CsvParserTest.class.getResourceAsStream("/clients_with_invalid_rows.csv");
        try {
            ImportEntityStatus<MigratingClient> result = importService.importClientCsv(is);
            Iterable<Client> clientsIt = clientRepository.findAll();
            assertEquals(2, result.numberOfImportedEntities());
            List<Client> clients = StreamSupport.stream(clientsIt.spliterator(), false).toList();
            assertEquals(2, clients.size());
            clients.forEach(client -> assertEquals(0, validator.validate(client).size()));
            assertEquals(2, result.errors().size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testImportAppointmentCsv() {
        try {
            InputStream clientsIs = CsvParserTest.class.getResourceAsStream("/clients.csv");
            importService.importClientCsv(clientsIs);
            InputStream is = CsvParserTest.class.getResourceAsStream("/appointments.csv");
            ImportEntityStatus<Appointment> result = importService.importAppointmentCsv(is);
            assertEquals(19, result.numberOfImportedEntities());

            Iterable<Appointment> appointmentsIt = appointmentRepository.findAll();
            List<Appointment> appointments = StreamSupport.stream(appointmentsIt.spliterator(), false).toList();
            assertEquals(19, appointments.size());
            appointments.forEach(appointment -> {
                assertEquals(0, validator.validate(appointments).size());
                assertNotNull(appointment.getClient());
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testImportPurchaseCsv() {
        Function<Product, ProductOrServiceDTO> converter = p -> new ProductOrServiceDTO(p.getName(), p.getPrice(), p.getLoyaltyPoints());
        Function<InputStream, ImportEntityStatus<Purchase>> serviceFunction = is -> {
            try {
                return importService.importPurchaseCsv(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        testSoldEntityCsv(
                "/purchases.csv",
                serviceFunction,
                13,
                converter,
                productRepository
        );
    }

    @Test
    public void testImportServicesCsv() {
        Function<Service, ProductOrServiceDTO> converter = s -> new ProductOrServiceDTO(s.getName(), s.getPrice(), s.getLoyaltyPoints());
        Function<InputStream, ImportEntityStatus<Purchase>> serviceFunction = is -> {
            try {
                return importService.importServiceCsv(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        testSoldEntityCsv(
                "/services.csv",
                serviceFunction,
                39,
                converter,
                serviceRepository
        );
    }

    private <E> void testSoldEntityCsv(
            String csvFileName,
            Function<InputStream, ImportEntityStatus<Purchase>> serviceFunction,
            int expectedNumberOfPurchases,
            Function<E, ProductOrServiceDTO> converter,
            CrudRepository<E, UUID> repository) {
        try {
            InputStream clientsIs = CsvParserTest.class.getResourceAsStream("/clients.csv");
            importService.importClientCsv(clientsIs);
            InputStream appointmentsIs = CsvParserTest.class.getResourceAsStream("/appointments.csv");
            importService.importAppointmentCsv(appointmentsIs);
            InputStream servicesIs = CsvParserTest.class.getResourceAsStream(csvFileName);
            ImportEntityStatus<Purchase> result = serviceFunction.apply(servicesIs);
            assertEquals(expectedNumberOfPurchases, result.numberOfImportedEntities());

            Iterable<Purchase> purchasesIt = purchaseRepository.findAll();
            List<Purchase> purchases = StreamSupport.stream(purchasesIt.spliterator(), false).toList();
            assertEquals(expectedNumberOfPurchases, purchases.size());
            purchases.forEach(purchase -> {
                assertEquals(0, validator.validate(purchase).size());
                assertNotNull(appointmentRepository.findByPurchases_Id(purchase.getId()));
            });


            Set<ProductOrServiceDTO> expectedServices = purchases.stream().collect(Collectors.groupingBy(Purchase::byProduct)).keySet();
            StreamSupport.stream(repository.findAll().spliterator(), false).forEach(entity -> {
                assertTrue(expectedServices.contains(converter.apply(entity)));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}