package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.exception.ImportFileParsingException;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingAppointment;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingClient;
import com.marchal.christophe.phoresttechtest.salon.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public void importAppointmentCsv(InputStream inputStream) {
        try {
            List<MigratingAppointment> migratingAppointments = parser.parseCsv(MigratingAppointment.class, inputStream);
            Set<UUID> clientIds = migratingAppointments.stream().map(MigratingAppointment::getClientId).collect(Collectors.toSet());
            Iterable<Client> clients = clientRepository.findAllById(clientIds);
            Map<UUID, Client> clientsByUUID = StreamSupport.stream(clients.spliterator(), false)
                    .collect(Collectors.groupingBy(Client::getId, Collectors.reducing(new Client(), (a, b) -> b)));
            List<Appointment> appointments = migratingAppointments.stream()
                    .map(appointment -> converter.toAppointment(appointment, clientsByUUID.get(appointment.getClientId())))
                    .toList();
            appointmentRepository.saveAll(appointments);
        } catch (IOException e) {
            throw new ImportFileParsingException("Failed to parse client csv file", e);
        }
    }
}
