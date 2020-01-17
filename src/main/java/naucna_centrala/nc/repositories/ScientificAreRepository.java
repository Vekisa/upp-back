package naucna_centrala.nc.repositories;

import naucna_centrala.nc.model.ScientificArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScientificAreRepository extends JpaRepository<ScientificArea, Long> {

    @Query(value = "SELECT sa FROM ScientificArea sa WHERE sa.naziv=:title")
    ScientificArea findByTitle(@Param("title") String title);
}
