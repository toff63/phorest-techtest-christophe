package com.marchal.christophe.phoresttechtest.migration;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public record ImportError<T>(
        T parsedLine,
        String errorMessage
) {
    public static <T> ImportError<T> fromConstraintsViolation(T entity, Set<ConstraintViolation<T>> violations) {
        String errorMessage = String.join(", ", violations.stream().map(violation -> violation.getPropertyPath() + " " + violation.getMessage()).toList());
        return new ImportError<T>(entity, errorMessage);
    }
}
