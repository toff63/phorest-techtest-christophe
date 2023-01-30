package com.marchal.christophe.phoresttechtest.migration;

import java.util.Set;

public record ImportResponse(
        int numberOfImportedClients,
        Set<String> clientImportErrors,
        int numberOfImportedAppointments,
        Set<String> appointmentImportErrors,
        int numberOfImportedPurchases,
        Set<String> purchaseImportErrors,
        int numberOfImportedServices,
        Set<String> serviceImportErrors
) {

}
