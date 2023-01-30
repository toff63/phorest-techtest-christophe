package com.marchal.christophe.phoresttechtest.migration.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.marchal.christophe.phoresttechtest.serialization.OffsetDateTimeDeserializer;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * CSV Appointment format
 *
 * @param id
 * @param startTime
 * @param endTime
 * @param clientId
 */
public record MigratingAppointment(
        @NotNull
        UUID id,

        @NotNull
        @JsonAlias("start_time")
        @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
        OffsetDateTime startTime,
        @NotNull
        @JsonAlias("end_time")
        @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
        OffsetDateTime endTime,

        @NotNull
        @JsonAlias("client_id")
        UUID clientId) {
}
