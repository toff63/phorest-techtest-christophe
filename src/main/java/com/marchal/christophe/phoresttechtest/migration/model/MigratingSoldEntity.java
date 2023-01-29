package com.marchal.christophe.phoresttechtest.migration.model;

import java.util.UUID;

public interface MigratingSoldEntity {
    UUID id();

    String name();

    Double price();

    Integer loyaltyPoints();

    UUID appointmentId();
}
