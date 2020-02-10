package naucna_centrala.nc.repositories;

import naucna_centrala.nc.model.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MagazineRepository  extends JpaRepository<Magazine, Long> {

    @Query(value = "SELECT m FROM Magazine m WHERE m.issn=:issn")
    public Magazine findMagazineByIssn(@Param("issn") String issn);

    @Query(value = "SELECT m FROM Magazine m WHERE m.naziv=:naziv")
    public Magazine findMagazineByName(@Param("naziv") String naziv);
}
