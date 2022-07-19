package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.AdminEntity;

import java.util.Optional;

@Repository
public interface AdminEntityRepository extends JpaRepository<AdminEntity, Integer> {

    @Query("select ae from AdminEntity as ae where ae.user.phoneNumber=?1")
    Optional<AdminEntity> getAdminUniversity(String phoneNumber);

}
