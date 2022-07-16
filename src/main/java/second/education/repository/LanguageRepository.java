package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.Language;
import second.education.model.response.GetStatisByLanguage;
import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    @Query(nativeQuery = true, value = "select l.language as languageName, l.kvota_soni as kvotaSoni, count(a.id) as count " +
            " from language l join application a on l.id = a.language_id where l.edu_form_id = ?1 group by l.language, l.kvota_soni")
    List<GetStatisByLanguage> findAllByEduFormId(Integer eduForm_id);
}
