package com.marchal.christophe.phoresttechtest.migration;

import java.util.Set;

/**
 * Data structure holding result of parsing an entity.
 *
 * @param numberOfImportedEntities number of saved entities
 * @param errors                   validation errors found when validating content
 * @param <T>                      can be any structure containing parsed data
 */
public record ImportEntityStatus<T>(
        int numberOfImportedEntities,
        Set<ImportError<T>> errors
) {
}
