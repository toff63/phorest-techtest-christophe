package com.marchal.christophe.phoresttechtest.migration;

import java.util.Set;

public record ImportEntityStatus<T>(
        int numberOfImportedEntities,
        Set<ImportError<T>> errors
) {
}
