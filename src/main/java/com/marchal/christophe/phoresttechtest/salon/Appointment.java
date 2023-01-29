package com.marchal.christophe.phoresttechtest.salon;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Appointment {
    @Id
    @NotNull
    private UUID id;

    @NotNull
    private OffsetDateTime startTime;
    @NotNull
    private OffsetDateTime endTime;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "appointment")
    private List<Purchase> purchases;


    public Appointment() {
    }

    public Appointment(UUID id, OffsetDateTime startTime, OffsetDateTime endTime, Client client) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.client = client;
    }

    public Appointment(OffsetDateTime startTime, OffsetDateTime endTime, Client client) {
        this.id = UUID.randomUUID();
        this.startTime = startTime;
        this.endTime = endTime;
        this.client = client;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", client=" + client +
                ", purchases=" + purchases +
                '}';
    }
}
