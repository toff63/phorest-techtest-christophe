package com.marchal.christophe.phoresttechtest.migration.api;

import com.marchal.christophe.phoresttechtest.migration.ImportError;
import com.marchal.christophe.phoresttechtest.migration.ImportStatus;
import com.marchal.christophe.phoresttechtest.migration.model.MigratingClient;
import com.marchal.christophe.phoresttechtest.salon.Appointment;
import com.marchal.christophe.phoresttechtest.salon.Purchase;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ServiceResponseConverter {
    public ImportResponse toImportResponse(ImportStatus serviceResponse) {
        Map<String, String> clientErrors = serviceResponse.clients().errors().stream().collect(Collectors.toMap(error -> serializeMigratingClient(error.parsedLine()), ImportError::errorMessage));
        Map<String, String> appointmentErrors = serviceResponse.appointments().errors().stream().collect(Collectors.toMap(error -> serializeAppointment(error.parsedLine()), ImportError::errorMessage));
        Map<String, String> purchaseErrors = serviceResponse.purchases().errors().stream().collect(Collectors.toMap(error -> serializePurchase(error.parsedLine()), ImportError::errorMessage));
        Map<String, String> servicesErrors = serviceResponse.services().errors().stream().collect(Collectors.toMap(error -> serializePurchase(error.parsedLine()), ImportError::errorMessage));
        return new ImportResponse(
                serviceResponse.clients().numberOfImportedEntities(), clientErrors,
                serviceResponse.appointments().numberOfImportedEntities(), appointmentErrors,
                serviceResponse.purchases().numberOfImportedEntities(), purchaseErrors,
                serviceResponse.services().numberOfImportedEntities(), servicesErrors
        );
    }

    private String serializeMigratingClient(MigratingClient c) {
        return "id=" + c.id() +
                ", firstName='" + c.firstName() + '\'' +
                ", lastName='" + c.lastName() + '\'' +
                ", email='" + c.email() + '\'' +
                ", phone='" + c.phone() + '\'' +
                ", gender='" + c.gender() + '\'' +
                ", banned=" + c.banned();
    }

    private String serializeAppointment(Appointment a) {
        return "id=" + a.getId() +
                ", startTime=" + a.getStartTime() +
                ", endTime=" + a.getEndTime() +
                ", client=" + (a.getClient() == null ? "null" : a.getClient().getId());
    }

    private String serializePurchase(Purchase p) {
        return "id=" + p.getId() +
                ", name='" + p.getName() + '\'' +
                ", price=" + p.getPrice() +
                ", loyaltyPoints=" + p.getLoyaltyPoints() +
                ", appointment=" + (p.getAppointment() == null ? "null" : p.getAppointment());
    }
}
