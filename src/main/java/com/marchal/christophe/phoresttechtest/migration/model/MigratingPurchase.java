package com.marchal.christophe.phoresttechtest.migration.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * * CSV Purchase format
 *
 * @param id
 * @param name
 * @param price
 * @param loyaltyPoints
 * @param appointmentId
 */
public record MigratingPurchase(
        @NotNull
        UUID id,
        @NotNull
        @Size(min = 2)
        String name,
        @NotNull
        @Min(0)
        Double price,
        @NotNull
        @Min(0)
        @JsonAlias("loyalty_points")
        Integer loyaltyPoints,

        @NotNull
        @JsonAlias("appointment_id")
        UUID appointmentId) implements MigratingSoldEntity {
}
