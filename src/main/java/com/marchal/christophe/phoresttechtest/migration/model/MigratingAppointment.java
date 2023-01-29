package com.marchal.christophe.phoresttechtest.migration.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.marchal.christophe.phoresttechtest.serialization.OffsetDateTimeDeserializer;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class MigratingAppointment {
    @NotNull
    private UUID id;

    @NotNull
    @JsonAlias("start_time")
    @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
    private OffsetDateTime startTime;
    @NotNull
    @JsonAlias("end_time")
    @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
    private OffsetDateTime endTime;

    @NotNull
    @JsonAlias("client_id")
    private UUID clientId;


    public MigratingAppointment() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(OffsetDateTime endTime) {
        this.endTime = endTime;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MigratingAppointment that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, clientId);
    }

    @Override
    public String toString() {
        return "MigratingAppointment{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", clientId=" + clientId +
                '}';
    }
}
