package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.EduForm;
import second.education.domain.classificator.Language;
import java.util.List;

@Repository
public interface EduFormRepository extends JpaRepository<EduForm, Integer> {

    @Query(nativeQuery = true, value = "select * from edu_form ef where ef.direction_id =?1")
    List<EduForm> findAllByDirectionId(Integer direction_id);


    @Query("select l from Language l where l.eduForm.id=?1 ")
    List<Language> findAllLanguageByEduForm(Integer eduFormId);


}
