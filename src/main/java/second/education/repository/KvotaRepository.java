package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.Kvota;

@Repository
public interface KvotaRepository extends JpaRepository<Kvota, Integer> {
}
