package com.marchal.christophe.phoresttechtest.migration;

import java.util.Set;

public record ImportEntityStatus(
        int numberOfImportedEntities,
        Set<ImportError> errors
) {
}
