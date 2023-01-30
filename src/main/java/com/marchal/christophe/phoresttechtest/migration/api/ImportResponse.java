package com.marchal.christophe.phoresttechtest.migration.api;

import java.util.Map;

public record ImportResponse(
        int numberOfImportedClients,
        Map<String, String> clientImportErrors,
        int numberOfImportedAppointments,
        Map<String, String> appointmentImportErrors,
        int numberOfImportedPurchases,
        Map<String, String> purchaseImportErrors,
        int numberOfImportedServices,
        Map<String, String> serviceImportErrors
) {

}
