package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.University;
import java.util.List;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {


}
