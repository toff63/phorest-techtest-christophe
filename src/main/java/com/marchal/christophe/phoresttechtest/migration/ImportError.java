package com.marchal.christophe.phoresttechtest.migration;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

/**
 * Information about why a parsed line failed once validated
 *
 * @param parsedLine   content after parsing
 * @param errorMessage validation error related to the parsed content
 * @param <T>          Content parsed type
 */
public record ImportError<T>(
        T parsedLine,
        String errorMessage
) {
    public static <T> ImportError<T> fromConstraintsViolation(T entity, Set<ConstraintViolation<T>> violations) {
        String errorMessage = String.join(", ", violations.stream().map(violation -> violation.getPropertyPath() + " " + violation.getMessage()).toList());
        return new ImportError<T>(entity, errorMessage);
    }
}
