package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.ActiveUniversity;

@Repository
public interface ActiveUniversityRepository extends JpaRepository<ActiveUniversity, Integer> {
}
