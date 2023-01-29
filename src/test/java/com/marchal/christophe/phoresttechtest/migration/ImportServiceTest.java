package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.salon.Appointment;
import com.marchal.christophe.phoresttechtest.salon.AppointmentRepository;
import com.marchal.christophe.phoresttechtest.salon.Client;
import com.marchal.christophe.phoresttechtest.salon.ClientRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ImportServiceTest {

    private static Validator validator;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ImportService importService;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void deleteAllBeforeTest() {
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
}