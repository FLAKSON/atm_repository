package org.maksymtiutiunnyk.atmproject.repositories;

import org.maksymtiutiunnyk.atmproject.entities.Cassette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CassetteRepository extends JpaRepository<Cassette,Long> {

    Optional<Cassette> getCassetteById(Long id);
}
