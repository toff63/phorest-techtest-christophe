package com.marchal.christophe.phoresttechtest.migration;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public record ImportError(
        String csvLine,
        String errorMessage
) {
    public static <T> ImportError fromConstraintsViolation(T entity, Set<ConstraintViolation<T>> violations) {
        String errorMessage = String.join(", ", violations.stream().map(violation -> violation.getPropertyPath() + " " + violation.getMessage()).toList());
        return new ImportError(entity.toString(), errorMessage);
    }
}
