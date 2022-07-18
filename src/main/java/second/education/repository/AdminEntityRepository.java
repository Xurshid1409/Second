package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.AdminEntity;
import java.util.Optional;

@Repository
public interface AdminEntityRepository extends JpaRepository<AdminEntity, Integer> {

    Optional<AdminEntity> findByPinfl(String pinfl);
}
