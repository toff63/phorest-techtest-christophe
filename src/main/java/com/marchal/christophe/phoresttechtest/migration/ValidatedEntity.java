package com.marchal.christophe.phoresttechtest.migration;

import java.util.List;
import java.util.Set;

public record ValidatedEntity<T>(
        List<T> validEntities,
        Set<ImportError<T>> errors
) {
}
