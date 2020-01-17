package naucna_centrala.nc.repositories;

import naucna_centrala.nc.model.MagazineRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MagazineRequestRepository   extends JpaRepository<MagazineRequest, Long> {

    @Query(value = "SELECT mr FROM MagazineRequest mr WHERE mr.processId=:id")
    MagazineRequest findByProcessId(@Param("id") String id);
}
