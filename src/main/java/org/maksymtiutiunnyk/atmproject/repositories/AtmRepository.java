package org.maksymtiutiunnyk.atmproject.repositories;

import org.maksymtiutiunnyk.atmproject.entites.Atm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AtmRepository extends JpaRepository<Atm,Long> {
    Optional<Atm> findById(Long id);
}
