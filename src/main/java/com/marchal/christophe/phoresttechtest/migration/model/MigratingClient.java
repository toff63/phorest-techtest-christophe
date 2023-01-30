package com.marchal.christophe.phoresttechtest.migration.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * * CSV Client format
 *
 * @param id
 * @param firstName
 * @param lastName
 * @param email
 * @param phone
 * @param gender
 * @param banned
 */
public record MigratingClient(
        UUID id,
        @NotEmpty
        @JsonAlias("first_name")
        String firstName,
        @NotEmpty
        @JsonAlias("last_name")
        String lastName,
        @Email
        @NotEmpty
        String email,

        //TODO integrate with https://github.com/google/libphonenumber to validate phone number
        // currently we would need a way for user to provide their local which should be one per salon.
        @NotEmpty
        String phone,

        @NotEmpty
        String gender,

        @NotNull
        Boolean banned
) {
}
