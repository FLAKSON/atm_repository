package org.maksymtiutiunnyk.atmproject.repositories;

import org.maksymtiutiunnyk.atmproject.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByPassportId(String passportId);
}
