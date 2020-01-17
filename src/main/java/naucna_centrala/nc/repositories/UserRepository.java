package naucna_centrala.nc.repositories;

import naucna_centrala.nc.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<CustomUser, Long> {

    @Query(value = "SELECT cu FROM CustomUser cu WHERE cu.korisnicko_ime=:username")
    public CustomUser findUserByUsername(@Param("username") String username);

    @Query(value = "SELECT cu FROM CustomUser cu WHERE cu.email=:email")
    public CustomUser findUserByEmail(@Param("email") String email);
}
