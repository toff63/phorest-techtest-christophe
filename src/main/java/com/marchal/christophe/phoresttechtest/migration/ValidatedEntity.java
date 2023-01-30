package com.marchal.christophe.phoresttechtest.migration;

import java.util.List;
import java.util.Set;

/**
 * Contains list of entities that have been validated and entities with errors.
 *
 * @param validEntities
 * @param errors
 * @param <T>
 */
public record ValidatedEntity<T>(
        List<T> validEntities,
        Set<ImportError<T>> errors
) {
}
