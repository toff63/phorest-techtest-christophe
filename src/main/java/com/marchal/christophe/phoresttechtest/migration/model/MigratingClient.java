package com.marchal.christophe.phoresttechtest.migration.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MigratingClient(
        UUID id,
        @NotNull
        @JsonAlias("first_name")
        String firstName,
        @NotNull
        @JsonAlias("last_name")
        String lastName,
        @Email
        @NotNull
        String email,

        //TODO integrate with https://github.com/google/libphonenumber to validate phone number
        // currently we would need a way for user to provide their local which should be one per salon.
        @NotNull
        String phone,

        @NotNull
        String gender,

        @NotNull
        Boolean banned
) {
}
