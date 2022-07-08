package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.FutureInstitution;

@Repository
public interface FutureInstitutionRepository extends JpaRepository<FutureInstitution, Integer> {

}
