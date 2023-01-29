package com.marchal.christophe.phoresttechtest.salon;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RepositoryEventHandler
public class SalonEventHandler {
    @HandleBeforeCreate
    public void handleClientSave(Client client) {
        if (client.getId() == null) {
            client.setId(UUID.randomUUID());
        }
    }

    @HandleBeforeCreate
    public void handleAppointmentSave(Appointment appointment) {
        if (appointment.getId() == null) {
            appointment.setId(UUID.randomUUID());
        }
    }
}
