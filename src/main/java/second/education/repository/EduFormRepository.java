package second.education.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.EduForm;
import second.education.domain.classificator.Language;
import second.education.model.response.GetStatisByEduForm;

import java.util.List;
import java.util.Optional;

@Repository
public interface EduFormRepository extends JpaRepository<EduForm, Integer> {

    @Query(nativeQuery = true, value = "select * from edu_form ef where ef.direction_id =?1")
    List<EduForm> findAllByDirectionIdPage(Integer direction_id);

    @Query("select l from Language l where l.eduForm.id=?1 ")
    List<Language> findAllLanguageByEduForm(Integer eduFormId);

    Optional<EduForm> findByName(String name);

    @Query("Select ef from EduForm ef where ef.direction.name LIKE  %?1% or ef.direction.futureInstitution.name LIKE %?1%")
    Page<EduForm> findEduFormByNameLike(String name, Pageable pageable);

    @Query(nativeQuery = true, value = "select ef.id as EduFormId, ef.name as EduFormNAme from edu_form ef where ef.direction_id = ?1")
    List<GetStatisByEduForm> findAllByDirectionId(Integer direction_id);

}
