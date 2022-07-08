package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.Direction;
import second.education.domain.classificator.FutureInstitution;
import java.util.Optional;

@Repository
public interface DirectionRepository extends JpaRepository<Direction, Integer> {
}
