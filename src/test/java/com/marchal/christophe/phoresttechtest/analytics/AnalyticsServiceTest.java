package com.marchal.christophe.phoresttechtest.analytics;

import com.marchal.christophe.phoresttechtest.migration.ImportService;
import com.marchal.christophe.phoresttechtest.salon.Client;
import com.marchal.christophe.phoresttechtest.salon.ClientRepository;
import com.marchal.christophe.phoresttechtest.salon.dto.LoyalClientDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class AnalyticsServiceTest {

    @Autowired
    private ImportService importService;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AnalyticsService analyticsService;

    @Test
    public void testTop5() {
        InputStream clientsIs = AnalyticsServiceTest.class.getResourceAsStream("/clients.csv");
        importService.importClientCsv(clientsIs);
        InputStream appointmentsIs = AnalyticsServiceTest.class.getResourceAsStream("/appointments.csv");
        importService.importAppointmentCsv(appointmentsIs);
        InputStream purchasesIs = AnalyticsServiceTest.class.getResourceAsStream("/purchases.csv");
        importService.importPurchaseCsv(purchasesIs);
        List<LoyalClientDto> clientsOrderedByLoyalty = analyticsService.findMostLoyalClients(4, LocalDate.parse("2016-01-01"));
        List<String> expectedClientIds = List.of(
                "b10231f8-d7a2-4486-a4e9-f55bf80e27fe",
                "104fdf33-c8a2-4f1c-b371-3e9c2facdfa0",
                "6cc17b57-478f-4b1d-adcf-437af488fe7f",
                "e0b8ebfc-6e57-4661-9546-328c644a3764");
        for (int i = 0; i < 4; i++) {
            Client c = clientRepository.findById(UUID.fromString(expectedClientIds.get(i))).get();
            Assertions.assertEquals(convert(c), clientsOrderedByLoyalty.get(i));
        }
    }

    private LoyalClientDto convert(Client c) {
        return new LoyalClientDto(c.getFirstName(), c.getLastName(), c.getEmail(), c.getPhone(), c.getGender());
    }

}