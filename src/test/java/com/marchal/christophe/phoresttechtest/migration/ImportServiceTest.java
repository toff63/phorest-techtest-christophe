package com.marchal.christophe.phoresttechtest.migration;

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

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
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
        importService.importClientCsv(is);
        Iterable<Client> clientsIt = clientRepository.findAll();
        List<Client> clients = StreamSupport.stream(clientsIt.spliterator(), false).toList();
        assertEquals(4, clients.size());
        clients.forEach(client -> assertEquals(0, validator.validate(client).size()));
    }

    @Test
    public void testImportAppointmentCsv() {
        InputStream clientsIs = CsvParserTest.class.getResourceAsStream("/clients.csv");
        importService.importClientCsv(clientsIs);
        InputStream is = CsvParserTest.class.getResourceAsStream("/appointments.csv");
        importService.importAppointmentCsv(is);
        Iterable<Appointment> appointmentsIt = appointmentRepository.findAll();
        List<Appointment> appointments = StreamSupport.stream(appointmentsIt.spliterator(), false).toList();
        assertEquals(19, appointments.size());
        appointments.forEach(appointment -> {
            assertEquals(0, validator.validate(appointments).size());
            assertNotNull(appointment.getClient());
        });

    }

    @Test
    public void testImportPurchaseCsv() {
        Function<Product, ProductOrServiceDTO> converter = p -> new ProductOrServiceDTO(p.getName(), p.getPrice(), p.getLoyaltyPoints());
        testSoldEntityCsv(
                "/purchases.csv",
                importService::importPurchaseCsv,
                13,
                converter,
                productRepository
        );
    }

    @Test
    public void testImportServicesCsv() {
        Function<Service, ProductOrServiceDTO> converter = s -> new ProductOrServiceDTO(s.getName(), s.getPrice(), s.getLoyaltyPoints());
        testSoldEntityCsv(
                "/services.csv",
                importService::importServiceCsv,
                39,
                converter,
                serviceRepository
        );
    }

    private <E> void testSoldEntityCsv(
            String csvFileName,
            Consumer<InputStream> serviceFunction,
            int expectedNumberOfPurchases,
            Function<E, ProductOrServiceDTO> converter,
            CrudRepository<E, UUID> repository) {
        InputStream clientsIs = CsvParserTest.class.getResourceAsStream("/clients.csv");
        importService.importClientCsv(clientsIs);
        InputStream appointmentsIs = CsvParserTest.class.getResourceAsStream("/appointments.csv");
        importService.importAppointmentCsv(appointmentsIs);
        InputStream servicesIs = CsvParserTest.class.getResourceAsStream(csvFileName);
        serviceFunction.accept(servicesIs);
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
    }
}