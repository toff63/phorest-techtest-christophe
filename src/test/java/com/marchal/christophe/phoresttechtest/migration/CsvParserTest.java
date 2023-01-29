package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.migration.model.MigratingAppointment;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingClient;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingPurchase;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingService;
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

    private <T> void parseCsv(String fileName, Class<T> clazz, int expectedNumberOfEntities) {
        InputStream is = CsvParserTest.class.getResourceAsStream(fileName);
        try {
            List<T> entities = parser.parseCsv(clazz, is);
            assertEquals(expectedNumberOfEntities, entities.size());
            assertTrue(entities.stream().allMatch(c -> validator.validate(c).size() == 0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void parseClientCsv() {
        parseCsv("/clients.csv", MigratingClient.class, 4);
    }

    @Test
    void parseAppointmentCsv() {
        parseCsv("/appointments.csv", MigratingAppointment.class, 19);
    }

    @Test
    void parsePurchaseCsv() {
        parseCsv("/purchases.csv", MigratingPurchase.class, 13);
    }

    @Test
    void parseServiceCsv() {
        parseCsv("/services.csv", MigratingService.class, 39);
    }
}