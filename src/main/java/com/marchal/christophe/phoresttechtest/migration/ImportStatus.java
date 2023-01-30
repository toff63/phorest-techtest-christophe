package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.migration.model.MigratingClient;
import com.marchal.christophe.phoresttechtest.salon.Appointment;
import com.marchal.christophe.phoresttechtest.salon.Purchase;

/**
 * Global import status
 *
 * @param clients
 * @param appointments
 * @param purchases
 * @param services
 */
public record ImportStatus(
        ImportEntityStatus<MigratingClient> clients,
        ImportEntityStatus<Appointment> appointments,
        ImportEntityStatus<Purchase> purchases,
        ImportEntityStatus<Purchase> services
) {
}
