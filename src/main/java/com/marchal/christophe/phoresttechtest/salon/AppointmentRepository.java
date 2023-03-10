package com.marchal.christophe.phoresttechtest.salon;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.lang.NonNull;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "appointment", path = "appointment")
public interface AppointmentRepository extends PagingAndSortingRepository<Appointment, UUID>, CrudRepository<Appointment, UUID> {

    Appointment findByPurchases_Id(@NonNull UUID id);
}
