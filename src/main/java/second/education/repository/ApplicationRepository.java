package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.Application;
import second.education.model.response.ApplicationResponse;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @Query(nativeQuery = true, value = "select * from application a where a.enrollee_info_id =?1")
    Optional<Application> findByEnrolleeInfoId(Integer enrolleInfoId);


    @Query(nativeQuery = true, value = "select l.id as tilId, l.language as tilName, " +
            "ef.id as shaklId, ef.name as shaklName, d.name as directionName, fi.name as futureInstitutionName " +
            "from application a " +
            "join edu_form ef on a.edu_form_id = ef.id " +
            "join language l on a.language_id = l.id " +
            "join direction d on ef.direction_id = d.id " +
            "join future_institution fi on d.future_institution_id = fi.id where a.enrollee_info_id =?")
    Optional<ApplicationResponse> findByAppByPrincipal(Integer enrolleeInfoId);
}
