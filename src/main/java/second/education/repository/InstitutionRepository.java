package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.University;

import java.util.List;

@Repository
public interface InstitutionRepository extends JpaRepository<University, Integer> {

    List<University> findAllByInstitutionTypeIdBetween(Integer institutionTypeId, Integer institutionTypeId2);



}
