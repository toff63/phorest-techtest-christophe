package com.marchal.christophe.phoresttechtest.migration.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.UUID;

public class MigratingClient {
    private UUID id;
    @NotNull
    @JsonAlias("first_name")
    private String firstName;
    @NotNull
    @JsonAlias("last_name")
    private String lastName;
    @Email
    @NotNull
    private String email;

    //TODO integrate with https://github.com/google/libphonenumber to validate phone number
    // currently we would need a way for user to provide their local which should be one per salon.
    @NotNull
    private String phone;

    @NotNull
    private String gender;

    @NotNull
    private Boolean banned;

    public MigratingClient() {
    }
    

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MigratingClient that)) return false;
        return id.equals(that.id) && firstName.equals(that.firstName) && lastName.equals(that.lastName) && email.equals(that.email) && phone.equals(that.phone) && gender.equals(that.gender) && banned.equals(that.banned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phone, gender, banned);
    }

    @Override
    public String toString() {
        return "MigratingClient{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", banned=" + banned +
                '}';
    }
}
