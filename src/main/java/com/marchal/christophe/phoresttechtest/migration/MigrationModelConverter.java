package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.migration.model.MigratingAppointment;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingClient;
import com.marchal.christophe.phoresttechtest.salon.Appointment;
import com.marchal.christophe.phoresttechtest.salon.AppointmentRepository;
import com.marchal.christophe.phoresttechtest.salon.Client;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
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

    public Appointment toAppointment(MigratingAppointment migratingAppointment, Client client) {
        if (clientNotFound(migratingAppointment, client)) {
            log.error("Appointment imported with unknown client: " + migratingAppointment);
        }
        return new Appointment(
                migratingAppointment.getId(),
                migratingAppointment.getStartTime(),
                migratingAppointment.getEndTime(),
                client
        );
    }

    private boolean clientNotFound(MigratingAppointment migratingAppointment, Client client) {
        return client == null || client.getId() == null || !client.getId().equals(migratingAppointment.getClientId());
    }
}
