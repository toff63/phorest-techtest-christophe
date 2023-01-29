package com.marchal.christophe.phoresttechtest.analytics;

import com.marchal.christophe.phoresttechtest.analytics.contract.LoyalClient;
import com.marchal.christophe.phoresttechtest.salon.dto.LoyalClientDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("analytics")
public class AnalyticsController {

    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/loyal")
    public List<LoyalClient> retrieveMostLoyalClients(@RequestParam("max") Integer numberOfClients, @RequestParam("from") String from) {
        // TODO return a 422 if form format is not valid
        List<LoyalClientDto> dto = service.findMostLoyalClients(numberOfClients, LocalDate.parse(from));
        return dto.stream().map(l -> new LoyalClient(l.firstName(), l.lastName(), l.email(), l.phone(), l.gender())).toList();
    }

}
