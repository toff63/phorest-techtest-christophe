package com.marchal.christophe.phoresttechtest.migration.api.v1;

import java.util.Map;

/**
 * Import salon response
 *
 * @param numberOfImportedClients
 * @param clientImportErrors
 * @param numberOfImportedAppointments
 * @param appointmentImportErrors
 * @param numberOfImportedPurchases
 * @param purchaseImportErrors
 * @param numberOfImportedServices
 * @param serviceImportErrors
 */
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
