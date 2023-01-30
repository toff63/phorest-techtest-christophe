## Phorest Tech test

### Framework used and why

* Spring Data JPA as data is relational and consequently a relational database made sense. As this is an exercise, I chose H2 as storage.
* Spring Data Rest as it generates a self discoverable RESTful API with little configuration.
* Java Bean Validation as it is easy to use and a J2EE standard.
* Spring Boot as provides default configuration to start a web project

### Database schema
Below is the database model
![database schema](https://github.com/toff63/phorest-techtest-christophe/raw/master/doc/schema.png)

Purchase table contains all data related to purchased goods and services because the schema for product and service is the same. Appointment isn't referencing Products and Services tables because purchases should be immutable.

The system fills Products and Services tables when importing CSV by finding unique name, price and loyalty points tuples.

### API

#### Migration API

In order to import provided CSV in the system, `/import/salon` HTTP POST endpoint is provided. It accepts 4 files in Multipart format using parameters:
* clients
* appointments
* services
* purchases

Request example:
```shell
curl -F clients=@csvs_to_import/clients.csv \
 -F appointments=@csvs_to_import/appointments.csv \
 -F services=@csvs_to_import/services.csv  \
 -F purchases=@csvs_to_import/purchases.csv \
 localhost:8080/import/salon | jq ''
```
The response informs about number of stored entities and eventual consistency/format issues:
```json
{
  "numberOfImportedClients": 100,
  "clientImportErrors": {},
  "numberOfImportedAppointments": 490,
  "appointmentImportErrors": {},
  "numberOfImportedPurchases": 476,
  "purchaseImportErrors": {},
  "numberOfImportedServices": 1031,
  "serviceImportErrors": {}
}
```

### Analytics API

An endpoint to list the top X number of clients that have accumulated the most loyalty points since Y date excluding banned clients.
It uses HTTP Get verb at path `/analytics/loyal` and *requires* two query parameters:
* `max` as integer to inform the maximum number of clients to be returned
* `from` as date from which the number loyalty point should be computed. example: '2016-01-01'

Request example:
```shell
curl 'localhost:8080/analytics/loyal?max=2&from=2016-01-01' | jq ''
```
Response:
```json
[
  {
    "firstName": "Christen",
    "lastName": "Hermann",
    "email": "bobwunsch@wehner.info",
    "phone": "(873) 257-2444",
    "gender": "Male"
  },
  {
    "firstName": "Roxie",
    "lastName": "Rau",
    "email": "kirsten@larkinrosenbaum.biz",
    "phone": "961-468-1035",
    "gender": "Male"
  }
]
```

If a parameter is missing, the API returns a status code 400 with a message

```shell
 $ curl -i 'localhost:8080/analytics/loyal?max=2'
 
HTTP/1.1 400 
Content-Type: application/problem+json
Transfer-Encoding: chunked
Date: Mon, 30 Jan 2023 16:43:42 GMT
Connection: close
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Required parameter 'from' is not present.",
  "instance": "/analytics/loyal",
  "properties": null
}
```

### RESTful Salon API

CRUD endpoints have been implemented for all entities and are self discoverable.
All entities can be created, updated, patched, retrieved or deleted through the API. Inputs are validated using Java bean validation and returning useful information for clients to update their request.

Request to discover links:
```shell
curl localhost:8080
```
Response showing service, purchase, client, product and appointment entities. `profile` endpoints inform about available operations and their schema.
```shell
{
  "_links" : {
    "service" : {
      "href" : "http://localhost:8080/service{?page,size,sort}",
      "templated" : true
    },
    "purchases" : {
      "href" : "http://localhost:8080/purchases{?page,size,sort}",
      "templated" : true
    },
    "client" : {
      "href" : "http://localhost:8080/client{?page,size,sort}",
      "templated" : true
    },
    "product" : {
      "href" : "http://localhost:8080/product{?page,size,sort}",
      "templated" : true
    },
    "appointment" : {
      "href" : "http://localhost:8080/appointment{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost:8080/profile"
    }
  }
}
```

#### Navigating the entity graph

Retrieve one client
```shell
curl http://localhost:8080/client\?size\=1 
```
Returns client data and links to navigate through pagination as well as discovering their appointments:
```json
{
  "_embedded" : {
    "client" : [ {
      "firstName" : "Dori",
      "lastName" : "Dietrich",
      "email" : "patrica@keeling.net",
      "phone" : "(272) 301-6356",
      "gender" : "Male",
      "banned" : false,
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/client/e0b8ebfc-6e57-4661-9546-328c644a3764"
        },
        "client" : {
          "href" : "http://localhost:8080/client/e0b8ebfc-6e57-4661-9546-328c644a3764"
        },
        "appointments" : {
          "href" : "http://localhost:8080/client/e0b8ebfc-6e57-4661-9546-328c644a3764/appointments"
        }
      }
    } ]
  },
  "_links" : {
    "first" : {
      "href" : "http://localhost:8080/client?page=0&size=1"
    },
    "self" : {
      "href" : "http://localhost:8080/client?size=1"
    },
    "next" : {
      "href" : "http://localhost:8080/client?page=1&size=1"
    },
    "last" : {
      "href" : "http://localhost:8080/client?page=99&size=1"
    },
    "profile" : {
      "href" : "http://localhost:8080/profile/client"
    },
    "search" : {
      "href" : "http://localhost:8080/client/search"
    }
  },
  "page" : {
    "size" : 1,
    "totalElements" : 100,
    "totalPages" : 100,
    "number" : 0
  }
}
```
To retrieve this client appointment, clients just need to follow the provided link:

```shell
curl http://localhost:8080/client/e0b8ebfc-6e57-4661-9546-328c644a3764/appointments
```

It returns the appointment details and links to follow to discover what they bought.
```json
{        
  "_embedded" : {
    "appointment" : [ {
      "startTime" : "2017-05-09T15:30:00+01:00",
      "endTime" : "2017-05-09T18:30:00+01:00",
      "_links" : {
        "self" : {                                                                                                                                                                            
          "href" : "http://localhost:8080/appointment/67ce894a-9625-4ab7-8b91-17d83fb3fd10"
        },
        "appointment" : {                      
          "href" : "http://localhost:8080/appointment/67ce894a-9625-4ab7-8b91-17d83fb3fd10"                                                                                                   
        },
        "client" : {
          "href" : "http://localhost:8080/appointment/67ce894a-9625-4ab7-8b91-17d83fb3fd10/client"
        },
        "purchases" : {
          "href" : "http://localhost:8080/appointment/67ce894a-9625-4ab7-8b91-17d83fb3fd10/purchases"
        }
      }
    }, {
      "startTime" : "2017-08-04T17:15:00+01:00",
      "endTime" : "2017-08-04T18:15:00+01:00",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/appointment/a659bdd1-cd79-473a-aff4-a20c5760748d"
        },
        "appointment" : {
          "href" : "http://localhost:8080/appointment/a659bdd1-cd79-473a-aff4-a20c5760748d"
        },
        "client" : {
          "href" : "http://localhost:8080/appointment/a659bdd1-cd79-473a-aff4-a20c5760748d/client"
        },
        "purchases" : {
          "href" : "http://localhost:8080/appointment/a659bdd1-cd79-473a-aff4-a20c5760748d/purchases"
        }
      }
    }, {
      "startTime" : "2018-07-04T13:00:00+01:00",
      "endTime" : "2018-07-04T13:20:00+01:00",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/appointment/f9fd9d10-8832-48a1-b1c9-058fea5e3232"
        },
        "appointment" : {
          "href" : "http://localhost:8080/appointment/f9fd9d10-8832-48a1-b1c9-058fea5e3232"
        },
        "client" : {
          "href" : "http://localhost:8080/appointment/f9fd9d10-8832-48a1-b1c9-058fea5e3232/client"
        },
        "purchases" : {
          "href" : "http://localhost:8080/appointment/f9fd9d10-8832-48a1-b1c9-058fea5e3232/purchases"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/client/e0b8ebfc-6e57-4661-9546-328c644a3764/appointments" 
    }
  }
}
```

#### Creating a client with an appointment

Request to create a client
```shell
curl -X POST -H 'Content-Type: application/json' \
 -d '{"firstName": "Frodo","lastName":"Baggins", "email":"frodo@baggins.net","phone":"01234","gender":"Male","banned":"false"}' \
 http://localhost:8080/client
```
Response confirms the new client was saved.
```json
{
  "firstName" : "Frodo",
  "lastName" : "Baggins",
  "email" : "frodo@baggins.net",
  "phone" : "01234",
  "gender" : "Male",
  "banned" : false,
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/client/50f1dcfc-e4ab-4d90-8994-2654abc46228"
    },
    "client" : {
      "href" : "http://localhost:8080/client/50f1dcfc-e4ab-4d90-8994-2654abc46228"
    },
    "appointments" : {
      "href" : "http://localhost:8080/client/50f1dcfc-e4ab-4d90-8994-2654abc46228/appointments"
    }
  }
}
```

We can now add an appointment and reference the client it belongs to using the link returned by the previous call:

```shell
curl -X POST -H 'Content-Type: application/json' \
-d '{"client": "http://localhost:8080/client/50f1dcfc-e4ab-4d90-8994-2654abc46228", "startTime": "2016-02-07T17:15:00+0000", "endTime":"2016-02-07T20:15:00+0000"}' \
localhost:8080/appointment
```

The response follow the same pattern as the one for client:

```json
{
  "startTime" : "2016-02-07T17:15:00Z",
  "endTime" : "2016-02-07T20:15:00Z",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/appointment/226c3480-28e4-4fa9-95fc-12ccd2fca21e"
    },
    "appointment" : {
      "href" : "http://localhost:8080/appointment/226c3480-28e4-4fa9-95fc-12ccd2fca21e"
    },
    "client" : {
      "href" : "http://localhost:8080/appointment/226c3480-28e4-4fa9-95fc-12ccd2fca21e/client"
    },
    "purchases" : {
      "href" : "http://localhost:8080/appointment/226c3480-28e4-4fa9-95fc-12ccd2fca21e/purchases"
    }
  }
}
```

We can confirm appointments are associated with the created user as going to `http://localhost:8080/client/50f1dcfc-e4ab-4d90-8994-2654abc46228/appointments` returns the same data as above.

### Software design principles

Except for the generated RESTful API, endpoints follow the onion architecture with a clear separation between API objects and service and model objects. It adds some boilerplate code with converters but provides a flexibility that makes the system easier to maintain and evolve.
### Tests

Integration tests have been written to validate the RESTful API. Other endpoints logic has been validated by writing unit tests and tests at the service level.
### Todo

* [X] Configure Hibernate to work with H2
* [X] Create entities
* [X] Expose entities via REST
* [X] Add import CSV endpoint for clients
* [X] Add import CSV endpoint for appointments
* [X] Add import CSV endpoint for services
* [X] Add import CSV endpoint for purchases 
* [X] Add endpoint returning most loyal clients
* [X] Polish by adding some validation
* [X] Add / Update documentation
