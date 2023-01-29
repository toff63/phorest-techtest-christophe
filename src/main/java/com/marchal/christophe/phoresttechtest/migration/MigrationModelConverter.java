package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.migration.model.MigratingClient;
import com.marchal.christophe.phoresttechtest.salon.AppointmentRepository;
import com.marchal.christophe.phoresttechtest.salon.Client;
import org.springframework.stereotype.Component;

@Component
public class MigrationModelConverter {
    private final AppointmentRepository appointmentRepository;

    public MigrationModelConverter(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Client toClient(MigratingClient migratingClient) {
        return new Client(
                migratingClient.getId(),
                migratingClient.getFirstName(),
                migratingClient.getLastName(),
                migratingClient.getEmail(),
                migratingClient.getPhone(),
                migratingClient.getGender(),
                migratingClient.getBanned()
        );
    }
}
