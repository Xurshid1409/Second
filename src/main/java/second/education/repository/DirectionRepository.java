package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.Direction;
import java.util.List;

@Repository
public interface DirectionRepository extends JpaRepository<Direction, Integer> {

    @Query(nativeQuery = true, value = "select * from direction d where d.future_institution_id=?1")
    List<Direction> findAllByFutureInstitutionId(Integer futureInstitution_id);
}
