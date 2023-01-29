package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.salon.*;
import com.marchal.christophe.phoresttechtest.salon.dto.ProductDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.Set;
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
        Iterable<Client> clients = clientRepository.findAll();
        int nbClient = 0;
        for (Client client : clients) {
            assertEquals(0, validator.validate(client).size());
            nbClient++;
        }
        assertEquals(4, nbClient);
    }

    @Test
    public void testImportAppointmentCsv() {
        InputStream clientsIs = CsvParserTest.class.getResourceAsStream("/clients.csv");
        importService.importClientCsv(clientsIs);
        InputStream is = CsvParserTest.class.getResourceAsStream("/appointments.csv");
        importService.importAppointmentCsv(is);
        Iterable<Appointment> appointments = appointmentRepository.findAll();
        int nbAppointment = 0;
        for (Appointment appointment : appointments) {
            assertEquals(0, validator.validate(appointments).size());
            assertNotNull(appointment.getClient());
            nbAppointment++;
        }
        assertEquals(19, nbAppointment);
    }

    @Test
    public void testImportPurchaseCsv() {
        InputStream clientsIs = CsvParserTest.class.getResourceAsStream("/clients.csv");
        importService.importClientCsv(clientsIs);
        InputStream appointmentsIs = CsvParserTest.class.getResourceAsStream("/appointments.csv");
        importService.importAppointmentCsv(appointmentsIs);
        InputStream purchasesIs = CsvParserTest.class.getResourceAsStream("/purchases.csv");
        importService.importPurchaseCsv(purchasesIs);
        Iterable<Purchase> purchases = purchaseRepository.findAll();
        int nbPurchase = 0;
        for (Purchase purchase : purchases) {
            assertEquals(0, validator.validate(purchases).size());
            assertNotNull(appointmentRepository.findByPurchases_Id(purchase.getId()));
            nbPurchase++;
        }
        assertEquals(13, nbPurchase);
        Set<ProductDTO> expectedProducts = StreamSupport.stream(purchases.spliterator(), false).collect(Collectors.groupingBy(Purchase::byProduct)).keySet();
        StreamSupport.stream(productRepository.findAll().spliterator(), false).forEach(product -> {
            assertTrue(expectedProducts.contains(new ProductDTO(product.getName(), product.getPrice(), product.getLoyaltyPoints())));
        });
    }
}