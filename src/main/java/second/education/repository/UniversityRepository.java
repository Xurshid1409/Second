package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.University;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {
}
