package com.marchal.christophe.phoresttechtest.migration.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

public class MigratingPurchase {
    @NotNull
    private UUID id;
    @NotNull
    @Size(min = 2)
    private String name;
    @NotNull
    @Min(0)
    private Double price;
    @NotNull
    @Min(0)
    @JsonAlias("loyalty_points")
    private Integer loyaltyPoints;

    @NotNull
    @JsonAlias("appointment_id")
    private UUID appointmentId;

    public MigratingPurchase() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MigratingPurchase that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(loyaltyPoints, that.loyaltyPoints) && Objects.equals(appointmentId, that.appointmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, loyaltyPoints, appointmentId);
    }

    @Override
    public String toString() {
        return "MigratingPurchase{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", loyaltyPoints=" + loyaltyPoints +
                ", appointmentId=" + appointmentId +
                '}';
    }
}
