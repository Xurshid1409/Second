package second.education.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.Application;
import second.education.model.response.*;

import java.util.List;
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
            "join enrollee_info ei on a.enrollee_info_id = ei.id  where ei.id=?1")
    Optional<ApplicationResponse> findByAppByPrincipal(Integer enrolleInfoId);


    @Query(nativeQuery = true, value = "select count(a.id) as count_today, " +
            "(select count(a.id) from application a) count from application a where Date(a.created_date)=current_date")
    Optional<GetStatAllCountAndToday> getCountTodayAndAllCount();

    @Query(nativeQuery = true, value = "select  count(a.id) as count , CAST(a.created_date AS DATE) as sana from   application as a where a.future_institution_id=?1 group by CAST(a.created_date AS DATE)  order by sana ")
    List<GetCountAppallDate> getAppCountTodayByUAdmin(Integer instId);

    @Query(nativeQuery = true, value = "select count(a.id) as count, CAST(d.created_date AS DATE) as sana " +
            "from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            "inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.institution_old_name_id=?1 " +
            "group by CAST(d.created_date AS DATE) " +
            "order by sana")
    List<GetCountAppallDate> getDiplomaCountTodayByUAdmin(Integer instId);

    @Query(nativeQuery = true, value = "select count(a.id) as count, CAST(d.created_date AS DATE) as sana " +
            " from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            "inner join diploma d on ei.id = d.enrollee_info_id " +
            " where a.future_institution_id=?1 " +
            " group by CAST(d.created_date AS DATE) " +
            " order by sana ")
    List<GetCountAppallDate> getForeignDiplomaCountTodayByUAdmin(Integer instId);

    @Query(nativeQuery = true, value = "select count(a.id) as count , ei.gender as gender from  application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id where a.future_institution_id=?1 group by ei.gender")
    List<GetAppByGender> getCounAppAndGenderByUAdmin(Integer instId);

    @Query(nativeQuery = true, value = " select count(a.id) as count, ei.gender as gender " +
            "from application as a " +
            "  inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.institution_old_name_id=?1 " +
            "group by ei.gender ")
    List<GetAppByGender> getCountDiplomaAndGender(Integer institutionId);

    @Query(nativeQuery = true, value = " select count(a.id) as count, ei.gender as gender " +
            "from application as a " +
            "  inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.institution_old_name_id=?1 " +
            "group by ei.gender ")
    List<GetAppByGender> getCountForeingDiplomaAndGender(Integer institutionId);


    @Query("select a from Application as a where a.enrolleeInfo.user.phoneNumber=?1")
    Optional<Application> checkApp(String phoneNumber);

    @Query(nativeQuery = true, value = "select l.language as language, l.kvota_soni as kvota, count(a.id) as count from application a " +
            "join language l on l.id = a.language_id where l.edu_form_id=?1 group by l.language, l.kvota_soni")
    List<StatisLanguageResponse> getStatisByEduForm(Integer eduFromId);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.isActive=true and a.status=?2 ")
    Page<Application> getAllApp(Integer instId, String status, Pageable pageable);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.isActive=true and a.id=?2")
    Optional<Application> getAppOne(Integer instId, Integer appId);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where d.institutionOldNameId=?1 and d.isActive=true and a.diplomaStatus=?2")
    Page<Application> getAppDiplomaByEnrollId(Integer id, Boolean status, Pageable pageable);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where d.institutionOldNameId=?1 and d.isActive=true and a.diplomaStatus is null ")
    Page<Application> getAppDiplomaByEnrollAppDiplomStatusNull(Integer id, Pageable pageable);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where d.institutionOldNameId=?1 and d.isActive=true and d.id=?2")
    Optional<Application> getAppAndDiplomaById(Integer id, Integer diplomaId);


    ////
    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.institutionOldNameId is null and d.isActive=true and a.diplomaStatus=?2")
    Page<Application> getAppDipForeign(Integer id, Boolean status, Pageable pageable);

    ///

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.institutionOldNameId is null and d.isActive=true and a.diplomaStatus is null ")
    Page<Application> getAppForeignDipStatusNull(Integer id, Pageable pageable);


    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.institutionOldNameId is null and d.isActive=true and d.id=?2")
    Optional<Application> getAppAndForeignDiplomaById(Integer id, Integer diplomaId);

    @Query("select a  from Application  as a  join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where d.id=?1")
    Optional<Application> getAppByDiplomId(Integer diplomaId);

    @Query(" select a from Application a where a.futureInstitution.id=?1 and a.status=?2 and (a.enrolleeInfo.pinfl like %?3% or a.enrolleeInfo.phoneNumber like %?3% or   a.enrolleeInfo.firstname like %?3% or a.enrolleeInfo.lastname like %?3% or a.enrolleeInfo.middleName like %?3%)")
    Page<Application> searchAppByFirstnameAndLastname(Integer futureInstId, String status, String search, Pageable pageable);


    @Query(" select a from Application a where a.futureInstitution.id=?1 and a.status=?2 and a.diplomaStatus=?3 and (a.enrolleeInfo.pinfl like %?4% or a.enrolleeInfo.phoneNumber like %?4% or   a.enrolleeInfo.firstname like %?4% or a.enrolleeInfo.lastname like %?4% or a.enrolleeInfo.middleName like %?4%)")
    Page<Application> searchAppByFirstnameAndLastnameByDiplomastatus(Integer futureInstId, String appStatus, Boolean diplomaStatus, String search, Pageable pageable);

    @Query(" select a from Application a where a.futureInstitution.id=?1 and a.status=?2 and a.diplomaStatus is null and (a.enrolleeInfo.pinfl like %?3% or a.enrolleeInfo.phoneNumber like %?3% or a.enrolleeInfo.firstname like %?3% or a.enrolleeInfo.lastname like %?3% or a.enrolleeInfo.middleName like %?3%) ")
    Page<Application> searchAppByFirstnameAndLastnameByDiplomastatusIsNull(Integer futureInstId, String appStatus, String search, Pageable pageable);

    @Query(" select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where d.institutionOldNameId=?1 and d.isActive=true and a.diplomaStatus=?2 and " +
            " (d.enrolleeInfo.firstname like %?3% or d.enrolleeInfo.lastname like %?3% or d.enrolleeInfo.middleName like %?3% or d.specialityName like %?3% or " +
            " d.diplomaSerialAndNumber like %?3%) ")
    Page<Application> searchDiplomaByUAdmin(Integer id, Boolean status, String search, Pageable pageable);

    @Query(" select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where d.institutionOldNameId=?1 and d.isActive=true and a.diplomaStatus is null and " +
            " (d.enrolleeInfo.firstname like %?2% or d.enrolleeInfo.lastname like %?2% or d.enrolleeInfo.middleName like %?2% or d.diplomaSerialAndNumber like %?2% or d.specialityName like %?2%) ")
    Page<Application> searchDiplomStatusNull(Integer id, String search, Pageable pageable);

    ///
    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.institutionOldNameId is null and d.isActive=true and a.diplomaStatus=?2 and " +
            " (d.enrolleeInfo.firstname like %?3% or d.enrolleeInfo.lastname like %?3% or d.enrolleeInfo.middleName like %?3% or d.enrolleeInfo.pinfl like %?3% or d.enrolleeInfo.phoneNumber like %?3%) ")
    Page<Application> searchForeignDiplomas(Integer id, Boolean status, String search, Pageable pageable);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.institutionOldNameId is null and d.isActive=true and a.diplomaStatus is null and " +
            " (d.enrolleeInfo.firstname like %?2% or d.enrolleeInfo.lastname like %?2% or d.enrolleeInfo.middleName like %?2% or d.enrolleeInfo.pinfl like %?2% or d.enrolleeInfo.phoneNumber like %?2%) ")
    Page<Application> searchForeignDiplomaStatusNull(Integer id, String search, Pageable pageable);

    @Query(nativeQuery = true, value = "select count(a.id) ,a.status from application as a inner join future_institution fi on fi.id = a.future_institution_id " +
            " inner join diploma d on a.enrollee_info_id = d.enrollee_info_id " +
            "where fi.id = ?1 and d.is_active=true " +
            "group by a.status ")
    List<CountApp> getCountApp(Integer futureInsId);

    @Query(nativeQuery = true, value = " select count(a.id) ,a.diploma_status as status from application as a inner join future_institution fi on fi.id = a.future_institution_id " +
            " inner join diploma d on a.enrollee_info_id = d.enrollee_info_id " +
            "where fi.id = ?1 and d.is_active=true and a.status='Ariza yuborildi' " +
            "group by a.diploma_status ")
    List<CountApp> getCountAppByDiplomaStatus(Integer futureInsId);


    @Query(nativeQuery = true, value = "select count(a.id) ,a.diploma_status as status  from application as a inner join future_institution fi on fi.id = a.future_institution_id " +
            "inner join enrollee_info ei on ei.id = a.enrollee_info_id inner join diploma d on ei.id = d.enrollee_info_id where fi.id=?1 and d.institution_old_name_id is null and d.is_active=true group by a.diploma_status")
    List<CountApp> getCountForeignDiploma(Integer id);

    @Query(nativeQuery = true, value = " select count(a.id) ,a.diploma_status as status from application as a inner join future_institution fi on fi.id = a.future_institution_id " +
            "inner join enrollee_info ei on ei.id = a.enrollee_info_id inner join diploma d on ei.id = d.enrollee_info_id where d.institution_old_name_id=?1 and d.is_active=true group by a.diploma_status ")
    List<CountApp> getCountDiploma(Integer institutionId);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and a.diplomaStatus=?2 and d.isActive=true and a.status=?3 ")
    Page<Application> getAllAppByDiplomaStatusAndAppstatus(Integer instId, Boolean diplomStatus, String appStatus, Pageable pageable);

    @Query(" select a from Application as a inner join Diploma d on a.enrolleeInfo.id = d.enrolleeInfo.id " +
            "where  d.isActive=true and a.diplomaStatus is null and a.futureInstitution.id=?1 and a.status=?2 ")
    Page<Application> getAllAppByDiplomaStatusIsNull(Integer instId, String appStatus, Pageable pageable);

}
