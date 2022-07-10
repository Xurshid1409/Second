package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.Tillar;

@Repository
public interface TillarRepository extends JpaRepository<Tillar, Integer> {
}
