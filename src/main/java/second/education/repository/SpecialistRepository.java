package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.Specialities;

import java.util.List;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialities, Integer> {

    List<Specialities> findAllByInstitutionId(Integer institutionId);
}
