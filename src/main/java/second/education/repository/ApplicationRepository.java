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

/*    @Query(nativeQuery = true, value = "select l.id as tilId, l.language as tilName, a.id, a.status as status, a.created_date as createdDate, " +
            "ef.id as shaklId, ef.name as shaklName, d.id as directionId, d.name as directionName, " +
            "fi.id as futureInstitutionId, fi.name as futureInstitutionName " +
            "from application a " +
            "join edu_form ef on a.edu_form_id = ef.id " +
            "join language l on a.language_id = l.id " +
            "join direction d on ef.direction_id = d.id " +
            "join future_institution fi on d.future_institution_id = fi.id where a.enrollee_info_id =?1")
    Optional<ApplicationResponse> findByAppByPrincipal(Integer enrolleeInfoId);*/

    @Query(nativeQuery = true, value = "select a.id, l.id as tilId, l.language as tilName, " +
            " ef.id as shaklId, ef.name as shaklName, d.id as directionId, d.name as directionName, " +
            "fi.id as futureInstitutionId, fi.name as futureInstitutionName, " +
            "a.status as appStatus, a.diploma_status as diplomaStatus, a.message as appMessage, " +
            "a.diploma_message as diplomaMessage, a.created_date as createdDate from application a " +
            "join edu_form ef on a.edu_form_id = ef.id " +
            "join direction d on ef.direction_id = d.id " +
            "join future_institution fi on d.future_institution_id = fi.id " +
            "join language l on a.language_id = l.id " +
            "join enrollee_info ei on a.enrollee_info_id = ei.id " +
            "join users u on ei.user_id = u.id where u.phone_number=?1")
    Optional<ApplicationResponse> findByAppByPrincipal(String phoneNumber);
}
