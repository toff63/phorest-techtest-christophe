package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.migration.model.MigratingAppointment;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingClient;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingPurchase;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CsvParserTest {
    private static Validator validator;

    @Autowired
    private CsvParser parser;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    void parseClientCsv() {
        InputStream is = CsvParserTest.class.getResourceAsStream("/clients.csv");
        try {
            List<MigratingClient> clients = parser.parseCsv(MigratingClient.class, is);
            assertEquals(4, clients.size());
            assertTrue(clients.stream().allMatch(c -> validator.validate(c).size() == 0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void parseAppointmentCsv() {
        InputStream is = CsvParserTest.class.getResourceAsStream("/appointments.csv");
        try {
            List<MigratingAppointment> appointments = parser.parseCsv(MigratingAppointment.class, is);
            assertEquals(19, appointments.size());
            assertTrue(appointments.stream().allMatch(c -> validator.validate(c).size() == 0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void parsePurchaseCsv() {
        InputStream is = CsvParserTest.class.getResourceAsStream("/purchases.csv");
        try {
            List<MigratingPurchase> purchases = parser.parseCsv(MigratingPurchase.class, is);
            assertEquals(13, purchases.size());
            assertTrue(purchases.stream().allMatch(c -> validator.validate(c).size() == 0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}