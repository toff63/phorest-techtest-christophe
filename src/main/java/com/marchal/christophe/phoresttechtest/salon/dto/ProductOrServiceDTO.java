package com.marchal.christophe.phoresttechtest.salon.dto;

public record ProductOrServiceDTO(
        String name,
        Double price,
        Integer loyaltyPoints
) {
}
