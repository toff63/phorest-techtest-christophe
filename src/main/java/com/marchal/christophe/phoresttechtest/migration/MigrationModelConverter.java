package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.migration.model.MigratingAppointment;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingClient;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingPurchase;
import com.marchal.christophe.phoresttechtest.salon.Appointment;
import com.marchal.christophe.phoresttechtest.salon.AppointmentRepository;
import com.marchal.christophe.phoresttechtest.salon.Client;
import com.marchal.christophe.phoresttechtest.salon.Purchase;
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
                migratingClient.id(),
                migratingClient.firstName(),
                migratingClient.lastName(),
                migratingClient.email(),
                migratingClient.phone(),
                migratingClient.gender(),
                migratingClient.banned()
        );
    }

    public Appointment toAppointment(MigratingAppointment migratingAppointment, Client client) {
        if (clientNotFound(migratingAppointment, client)) {
            log.error("Appointment imported with unknown client: " + migratingAppointment);
        }
        return new Appointment(
                migratingAppointment.id(),
                migratingAppointment.startTime(),
                migratingAppointment.endTime(),
                client
        );
    }

    private boolean clientNotFound(MigratingAppointment migratingAppointment, Client client) {
        return client == null || client.getId() == null || !client.getId().equals(migratingAppointment.clientId());
    }

    private boolean appointmentNotFound(MigratingPurchase migratingPurchase, Appointment appointment) {
        return appointment == null || appointment.getId() == null || !appointment.getId().equals(migratingPurchase.appointmentId());
    }

    public Purchase toPurchase(MigratingPurchase migratingPurchase, Appointment appointment) {
        if (appointmentNotFound(migratingPurchase, appointment)) {
            log.error("Purchase imported with unknown appointment: " + migratingPurchase);
        }
        return new Purchase(
                migratingPurchase.id(),
                migratingPurchase.name(),
                migratingPurchase.price(),
                migratingPurchase.loyaltyPoints(),
                appointment
        );
    }
}
