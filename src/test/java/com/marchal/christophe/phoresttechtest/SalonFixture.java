package com.marchal.christophe.phoresttechtest;

public class SalonFixture {
    public final static String frodoBagginsClient = """
            {
                "firstName": "Frodo",
                "lastName":"Baggins",
                "email":"frodo@baggins.net",
                "phone":"01234",
                "gender":"Male",
                "banned":"false"
            }""";
    public final static String bilboBagginsClient = """
            {
                "firstName": "Bilbo",
                "lastName":"Baggins",
                "email":"frodo@baggins.net",
                "phone":"01234",
                "gender":"Male",
                "banned":"false"
            }""";
    public final static String frodoFirstName = "Frodo";
    public final static String bilboFirstName = "Bilbo";
    public final static String bagginsLastName = "Baggins";

    public static String appointment(String client) {
        return "{\"startTime\": \"2016-02-07T17:15:00+0000\", \"endTime\":\"2016-02-07T20:15:00+0000\", \"client\": \"" + client + "\"}";
    }
}
