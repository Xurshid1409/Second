package second.education.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.AdminEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminEntityRepository extends JpaRepository<AdminEntity, Integer> {

    @Query("select ae from AdminEntity as ae where ae.user.phoneNumber=?1")
    Optional<AdminEntity> getAdminUniversity(String phoneNumber);

    @Query("select ae from AdminEntity ae where ae.user.role.id = 3")
    Page<AdminEntity> getAllAdmins(Pageable pageable);

    @Query("select ae from AdminEntity ae where ae.user.role.id = 3 and ae.id =?1 ")
    Optional<AdminEntity> getAdminById(Integer Id);

}
