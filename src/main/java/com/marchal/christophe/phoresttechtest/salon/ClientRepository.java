package com.marchal.christophe.phoresttechtest.salon;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "client", path = "client")
public interface ClientRepository extends PagingAndSortingRepository<Client, UUID>, CrudRepository<Client, UUID> {
    @Query("""
            select c
            from Client c 
            inner join c.appointments a
            inner join a.purchases p
            where a.startTime >= ?1 and c.banned = false
            group by c
            order by sum(p.loyaltyPoints) DESC
            limit ?2""")
    List<Client> findMostLoyalClients(OffsetDateTime startTime, int limit);
}
