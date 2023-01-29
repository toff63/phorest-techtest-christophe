package com.marchal.christophe.phoresttechtest.migration;

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

@SpringBootTest
class ImportServiceTest {

    private static Validator validator;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ImportService importService;

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
        assertEquals(9, nbClient);

    }
}