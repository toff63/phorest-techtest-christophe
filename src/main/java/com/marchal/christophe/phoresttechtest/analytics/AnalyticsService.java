package com.marchal.christophe.phoresttechtest.analytics;

import com.marchal.christophe.phoresttechtest.salon.Client;
import com.marchal.christophe.phoresttechtest.salon.ClientRepository;
import com.marchal.christophe.phoresttechtest.salon.dto.LoyalClientDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Analytics service implements business logic to serve Analytics requests
 */
@Service
public class AnalyticsService {

    private final ClientRepository clientRepository;

    public AnalyticsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<LoyalClientDto> findMostLoyalClients(int max, LocalDate from) {
        List<Client> clients = clientRepository.findMostLoyalClients(from.atStartOfDay().atOffset(ZoneOffset.UTC), max);
        return clients.stream().map(c -> new LoyalClientDto(c.getFirstName(), c.getLastName(), c.getEmail(), c.getPhone(), c.getGender())).toList();
    }
}
