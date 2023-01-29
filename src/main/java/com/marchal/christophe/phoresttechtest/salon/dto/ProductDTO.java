package com.marchal.christophe.phoresttechtest.salon.dto;

import java.util.Objects;
import java.util.UUID;

public class ProductDTO {
    private UUID id;
    private String name;
    private Double price;
    private Integer loyaltyPoints;

    public ProductDTO(String name, Double price, Integer loyaltyPoints) {
        this.name = name;
        this.price = price;
        this.loyaltyPoints = loyaltyPoints;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDTO that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(loyaltyPoints, that.loyaltyPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, loyaltyPoints);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", loyaltyPoints=" + loyaltyPoints +
                '}';

    }
}
