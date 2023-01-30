package com.marchal.christophe.phoresttechtest.migration.model;

import java.util.UUID;

/**
 * Common interface for all sold services and products
 */
public interface MigratingSoldEntity {
    UUID id();

    String name();

    Double price();

    Integer loyaltyPoints();

    UUID appointmentId();
}
