package com.marchal.christophe.phoresttechtest.salon;

import com.marchal.christophe.phoresttechtest.salon.dto.ProductOrServiceDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
public class Purchase {
    @Id
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
    private Integer loyaltyPoints;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    public Purchase() {
    }

    public Purchase(UUID id, String name, Double price, Integer loyaltyPoints, Appointment appointment) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.loyaltyPoints = loyaltyPoints;
        this.appointment = appointment;
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

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public ProductOrServiceDTO byProduct() {
        return new ProductOrServiceDTO(this.name, this.price, this.loyaltyPoints);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", loyaltyPoints=" + loyaltyPoints +
                ", appointment=" + appointment +
                '}';
    }
}
