package com.marchal.christophe.phoresttechtest.salon;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "service", path = "service")
public interface ServiceRepository extends PagingAndSortingRepository<Service, Long>, CrudRepository<Service, Long> {
}
