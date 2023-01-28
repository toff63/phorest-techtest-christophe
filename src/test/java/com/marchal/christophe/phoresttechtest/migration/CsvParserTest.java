package com.marchal.christophe.phoresttechtest.migration;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvParserTest {
    private static Validator validator;
    private final CsvParser parser = new CsvParser();

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
            System.out.println(clients);
            assertEquals(9, clients.size());
            assertTrue(clients.stream().allMatch(c -> validator.validate(c).size() == 0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}