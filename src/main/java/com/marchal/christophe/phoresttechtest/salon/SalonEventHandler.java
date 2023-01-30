package com.marchal.christophe.phoresttechtest.salon;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Ensure imported entities always have a UUID, either they are imported either they are added using REST API
 */
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

    @HandleBeforeCreate
    public void handlePurchaseSave(Purchase purchase) {
        if (purchase.getId() == null) {
            purchase.setId(UUID.randomUUID());
        }
    }
}
