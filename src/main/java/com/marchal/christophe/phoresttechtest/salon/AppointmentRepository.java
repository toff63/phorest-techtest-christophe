package com.marchal.christophe.phoresttechtest.salon;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "appointment", path = "appointment")
public interface AppointmentRepository extends PagingAndSortingRepository<Appointment, Long>, CrudRepository<Appointment, Long> {
}
