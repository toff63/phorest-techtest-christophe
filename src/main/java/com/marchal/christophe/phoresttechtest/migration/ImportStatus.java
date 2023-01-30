package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.migration.model.MigratingClient;
import com.marchal.christophe.phoresttechtest.salon.Appointment;
import com.marchal.christophe.phoresttechtest.salon.Purchase;

public record ImportStatus(
        ImportEntityStatus<MigratingClient> clients,
        ImportEntityStatus<Appointment> appointments,
        ImportEntityStatus<Purchase> purchases,
        ImportEntityStatus<Purchase> services
) {
}
