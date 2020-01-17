package naucna_centrala.nc.repositories;

import naucna_centrala.nc.model.EnablingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestRepository extends JpaRepository<EnablingRequest, Long> {

    @Query(value = "SELECT er FROM EnablingRequest er WHERE er.processId=:id")
    EnablingRequest findByProcessId(@Param("id") String id);
}
