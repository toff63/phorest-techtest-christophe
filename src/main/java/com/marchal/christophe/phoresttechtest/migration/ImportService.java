package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.exception.ImportFileParsingException;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingClient;
import com.marchal.christophe.phoresttechtest.salon.AppointmentRepository;
import com.marchal.christophe.phoresttechtest.salon.ClientRepository;
import com.marchal.christophe.phoresttechtest.salon.ProductRepository;
import com.marchal.christophe.phoresttechtest.salon.ServiceRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ImportService {
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;
    private final CsvParser parser;
    private final MigrationModelConverter converter;

    public ImportService(ClientRepository clientRepository,
                         ProductRepository productRepository,
                         ServiceRepository serviceRepository,
                         AppointmentRepository appointmentRepository, CsvParser parser, MigrationModelConverter converter) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
        this.parser = parser;
        this.converter = converter;
    }

    public void importClientCsv(InputStream inputStream) {
        try {
            List<MigratingClient> clients = parser.parseCsv(MigratingClient.class, inputStream);
            clientRepository.saveAll(clients.stream().map(converter::toClient).toList());
        } catch (IOException e) {
            throw new ImportFileParsingException("Failed to parse client csv file", e);
        }

    }
}
