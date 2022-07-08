package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
}
