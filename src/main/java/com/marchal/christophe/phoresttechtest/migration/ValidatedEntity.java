package com.marchal.christophe.phoresttechtest.migration;

import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record ValidatedEntity<T>(
        List<T> validEntities,
        Map<T, Set<ConstraintViolation<T>>> invalidEntities
) {
}
