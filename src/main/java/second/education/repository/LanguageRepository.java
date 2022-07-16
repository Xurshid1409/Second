package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.Language;
import second.education.model.response.GetStatisByLanguage;
import second.education.model.response.StatisDirectionResponse;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    @Query("select new second.education.model.response.StatisDirectionResponse(d.id, d.name) from Direction d " +
            "where d.futureInstitution.id=?1 ")
    List<StatisDirectionResponse> findAllByEduFormId(Integer eduForm_id);
}
