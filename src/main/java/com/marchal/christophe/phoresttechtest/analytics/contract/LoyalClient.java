package com.marchal.christophe.phoresttechtest.analytics.contract;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoyalClient(
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        @Email
        @NotNull
        String email,

        //TODO integrate with https://github.com/google/libphonenumber to validate phone number
        // currently we would need a way for user to provide their local which should be one per salon.
        @NotNull
        String phone,
        @NotNull
        String gender

) {
}
