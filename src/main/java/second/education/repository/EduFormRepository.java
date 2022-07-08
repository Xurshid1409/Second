package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.EduForm;

@Repository
public interface EduFormRepository extends JpaRepository<EduForm, Integer> {
}
