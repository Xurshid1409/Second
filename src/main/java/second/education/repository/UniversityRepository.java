package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.User;
import second.education.domain.classificator.University;
import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {

    @Query(" select u from University u where u.institutionId=?1 ")
    List<University> findAllByInstitutionId(Integer institutionId);


}
