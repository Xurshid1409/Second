package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.Language;
import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    @Query(nativeQuery = true, value = "select * from language l where l.direction_id =?1")
    List<Language> findAllByDirectionId(Integer direction_id);
}
