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


    @Query(nativeQuery = true, value = "select count(a.id) as count_today,(select count(a.id) from application a inner join enrollee_info ei on ei.id = a.enrollee_info_id\n" +
            " join diploma d on ei.id = d.enrollee_info_id\n" +
            " where  d.is_active=true  ) count from application a where  Date(a.created_date)=current_date ")
    Optional<GetStatAllCountAndToday> getCountTodayAndAllCount();

    @Query(nativeQuery = true, value = "select  count(a.id) as count , CAST(a.created_date AS DATE) as sana from   application as a " +
            "inner join enrollee_info ei on ei.id = a.enrollee_info_id  inner join  diploma d on ei.id = d.enrollee_info_id " +
            " where a.future_institution_id=?1 and d.is_active=true group by CAST(a.created_date AS DATE) order by sana ")
    List<GetCountAppallDate> getAppCountTodayByUAdmin(Integer instId);

    @Query(nativeQuery = true, value = "select count(a.id) as count, CAST(d.created_date AS DATE) as sana " +
            "from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            "inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.institution_old_name_id=?1 and d.is_active=true " +
            "group by CAST(d.created_date AS DATE) " +
            "order by sana")
    List<GetCountAppallDate> getDiplomaCountTodayByUAdmin(Integer instId);
    @Query(nativeQuery = true, value = "select count(a.id) as count, CAST(d.created_date AS DATE) as sana " +
            "from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            "inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.is_active=true " +
            "group by CAST(d.created_date AS DATE) " +
            "order by sana")
    List<GetCountAppallDate> getDiplomaCountTodayByAdminAll();

    @Query(nativeQuery = true, value = "select count(a.id) as count, CAST(d.created_date AS DATE) as sana " +
            " from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            "inner join diploma d on ei.id = d.enrollee_info_id " +
            " where a.future_institution_id=?1 and d.is_active=true " +
            " group by CAST(d.created_date AS DATE) " +
            " order by sana ")
    List<GetCountAppallDate> getForeignDiplomaCountTodayByUAdmin(Integer instId);
    @Query(nativeQuery = true, value = "select count(a.id) as count, CAST(d.created_date AS DATE) as sana " +
            " from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            "inner join diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active=true " +
            " group by CAST(d.created_date AS DATE) " +
            " order by sana ")
    List<GetCountAppallDate> getForeignDiplomaCountTodayByAdminAll();

    @Query(nativeQuery = true, value = "select count(a.id) as count , ei.gender as gender from  application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id inner join diploma d on ei.id = d.enrollee_info_id " +
            "    where a.future_institution_id=?1 and d.is_active=true group by ei.gender")
    List<GetAppByGender> getCounAppAndGenderByUAdmin(Integer instId);

    @Query(nativeQuery = true, value = " select count(a.id) as count, ei.gender as gender " +
            "from application as a " +
            "  inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.institution_old_name_id=?1 and d.is_active=true " +
            "group by ei.gender ")
    List<GetAppByGender> getCountDiplomaAndGender(Integer institutionId);
    @Query(nativeQuery = true, value = " select count(a.id) as count, ei.gender as gender " +
            "from application as a " +
            "  inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.is_active=true " +
            "group by ei.gender ")
    List<GetAppByGender> getCountDiplomaAndGenderAll();

    @Query(nativeQuery = true, value = " select count(a.id) as count, ei.gender as gender " +
            "from application as a " +
            "  inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "where a.future_institution_id=?1 and d.institution_old_name_id is null and d.is_active=true " +
            "group by ei.gender ")
    List<GetAppByGender> getCountForeingDiplomaAndGender(Integer institutionId);
    @Query(nativeQuery = true, value = " select count(a.id) as count, ei.gender as gender " +
            "from application as a " +
            "  inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "where  d.institution_old_name_id is null and d.is_active=true " +
            "group by ei.gender ")
    List<GetAppByGender> getCountForeingDiplomaAndGenderAll();

    @Query("select a from Application as a where a.enrolleeInfo.user.phoneNumber=?1")
    Optional<Application> checkApp(String phoneNumber);

    @Query(nativeQuery = true, value = "select l.language as language, l.kvota_soni as kvota, count(a.id) as count from application a " +
            "join language l on l.id = a.language_id where l.edu_form_id=?1 group by l.language, l.kvota_soni")
    List<StatisLanguageResponse> getStatisByEduForm(Integer eduFromId);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.isActive=true and a.status=?2 ")
    Page<Application> getAllApp(Integer instId, String status, Pageable pageable);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.isActive=true and a.id=?2")
    Optional<Application> getAppOne(Integer instId, Integer appId);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where  d.isActive=true and a.id=?1")
    Optional<Application> getAppOneByAdmin(Integer appId);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where d.institutionOldNameId=?1 and d.isActive=true and a.diplomaStatus=?2")
    Page<Application> getAppDiplomaByEnrollId(Integer id, Boolean status, Pageable pageable);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where d.institutionOldNameId=?1 and d.isActive=true and a.diplomaStatus is null ")
    Page<Application> getAppDiplomaByEnrollAppDiplomStatusNull(Integer id, Pageable pageable);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where d.institutionOldNameId=?1 and d.isActive=true and d.id=?2")
    Optional<Application> getAppAndDiplomaById(Integer id, Integer diplomaId);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where  d.isActive=true and d.id=?1")
    Optional<Application> getAppAndDiplomaByAdmin(Integer diplomaId);

    ////
    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.institutionOldNameId is null and d.isActive=true and a.diplomaStatus=?2")
    Page<Application> getAppDipForeign(Integer id, Boolean status, Pageable pageable);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.id=?2 ")
    Optional<Application> getAppByUadmin(Integer institutionId, Integer diplomaId);

    ///
    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.institutionOldNameId is null and d.isActive=true and a.diplomaStatus is null ")
    Page<Application> getAppForeignDipStatusNull(Integer id, Pageable pageable);


    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where a.futureInstitution.id=?1 and d.institutionOldNameId is null and d.isActive=true and d.id=?2")
    Optional<Application> getAppAndForeignDiplomaById(Integer id, Integer diplomaId);

    @Query("select a from Application as a join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where  d.institutionOldNameId is null and d.isActive=true and d.id=?1")
    Optional<Application> getAppAndForeignDiplomaByIdByAdmin(Integer diplomaId);

    @Query("select a  from Application  as a  join Diploma as d on a.enrolleeInfo.id=d.enrolleeInfo.id where d.id=?1 and d.isActive=true ")
    Optional<Application> getAppByDiplomId(Integer diplomaId);

    @Query(" select a from Application a join Diploma as d on d.enrolleeInfo.id=a.enrolleeInfo.id  where a.futureInstitution.id=?1 and d.isActive=true and a.status=?2 and (a.enrolleeInfo.pinfl like %?3% or a.enrolleeInfo.phoneNumber like %?3% or   a.enrolleeInfo.firstname like %?3% or a.enrolleeInfo.lastname like %?3% or a.enrolleeInfo.middleName like %?3%)")
    Page<Application> searchAppByFirstnameAndLastname(Integer futureInstId, String status, String search, Pageable pageable);


    @Query(" select a from Application a join Diploma as d on  d.enrolleeInfo.id=a.enrolleeInfo.id    where a.futureInstitution.id=?1 and d.isActive=true and a.status=?2 and a.diplomaStatus=?3 and (a.enrolleeInfo.pinfl like %?4% or a.enrolleeInfo.phoneNumber like %?4% or   a.enrolleeInfo.firstname like %?4% or a.enrolleeInfo.lastname like %?4% or a.enrolleeInfo.middleName like %?4%)")
    Page<Application> searchAppByFirstnameAndLastnameByDiplomastatus(Integer futureInstId, String appStatus, Boolean diplomaStatus, String search, Pageable pageable);

    @Query(" select a from Application a join Diploma as d on d.enrolleeInfo.id=a.enrolleeInfo.id where a.futureInstitution.id=?1 and d.isActive=true and a.status=?2 and a.diplomaStatus is null and (a.enrolleeInfo.pinfl like %?3% or a.enrolleeInfo.phoneNumber like %?3% or a.enrolleeInfo.firstname like %?3% or a.enrolleeInfo.lastname like %?3% or a.enrolleeInfo.middleName like %?3%) ")
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

    @Query(nativeQuery = true, value = " select d.id as diplomaId," +
            " ei.firstname  as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber, " +
            " d.edu_finishing_date as finishingDate " +
            "from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.is_active = true " +
            "  and d.institution_old_name_id = ?1 " +
            "  and a.diploma_status = ?2 order by d.id ")
    List<GetDiplomasToExcel> exportDiplomaToExcel(Integer id, Boolean status);

    @Query(nativeQuery = true, value = " select d.id as diplomaId," +
            " ei.firstname  as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber, " +
            " d.edu_finishing_date as finishingDate " +
            "from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.is_active = true " +
            "  and a.future_institution_id = ?1 and d.institution_old_name_id is null " +
            "  and a.diploma_status = ?2 order by d.id ")
    List<GetDiplomasToExcel> exportForeignDiplomaToExcel(Integer id, Boolean status);

    @Query(nativeQuery = true, value = " select d.id as diplomaId," +
            " ei.firstname as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber, " +
            " d.edu_finishing_date as finishingDate " +
            "from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.is_active = true " +
            "  and a.future_institution_id = ?1 and d.institution_old_name_id is null " +
            "  and a.diploma_status  is null order by d.id ")
    List<GetDiplomasToExcel> exportForeignDiplomaNullToExcel(Integer id);

    @Query(nativeQuery = true, value = " select d.id as diplomaId," +
            " ei.firstname as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber, " +
            " d.edu_finishing_date as finishingDate " +
            "from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.is_active = true " +
            "  and d.institution_old_name_id = ?1 " +
            "  and a.diploma_status  is null order by d.id ")
    List<GetDiplomasToExcel> exportDiplomaNullToExcel(Integer id);

    @Query(nativeQuery = true, value = " select a.id  as appId, " +
            "  ei.firstname as firstName, " +
            "  ei.lastname  as lastName, " +
            "  ei.middle_name as middleName, " +
            "  ei.phone_number as phoneNumber, " +
            "  ei.passport_serial_and_number as passportSerialNumber, " +
            " fi.name as futureInstitutionName, " +
            " d2.name as directionName, " +
            " ef.name as talimShakli, " +
            " l.language as language, " +
            "  d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber," +
            " d.edu_finishing_date as finishingDate " +
            " from application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "inner join future_institution fi on fi.id = a.future_institution_id " +
            "inner join edu_form ef on a.edu_form_id = ef.id " +
            "inner join direction d2 on d2.id = ef.direction_id " +
            "inner join language l on l.id = a.language_id " +
            "where d.is_active = true and a.status='Ariza yuborildi' and a.future_institution_id=?1 and a.diploma_status is null " +
            "order by a.id ")
    List<GetAppToExcel> exportAppByDiplomaNullToExcel(Integer id);

    @Query(nativeQuery = true, value = " select a.id  as appId, " +
            "  ei.firstname as firstName, " +
            "  ei.lastname  as lastName, " +
            "  ei.middle_name as middleName, " +
            "  ei.phone_number as phoneNumber, " +
            "  ei.passport_serial_and_number as passportSerialNumber, " +
            " fi.name as futureInstitutionName, " +
            " d2.name as directionName, " +
            " ef.name as talimShakli, " +
            " l.language as language, " +
            "  d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber," +
            " d.edu_finishing_date as finishingDate " +
            " from application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "inner join future_institution fi on fi.id = a.future_institution_id " +
            "inner join edu_form ef on a.edu_form_id = ef.id " +
            "inner join direction d2 on d2.id = ef.direction_id " +
            "inner join language l on l.id = a.language_id " +
            "where d.is_active = true and a.status='Ariza yuborildi' and a.future_institution_id=?1 and a.diploma_status =true " +
            "order by a.id ")
    List<GetAppToExcel> exportAppDiplomaTrueToExcel(Integer id);

    @Query(nativeQuery = true, value = " select a.id  as appId, " +
            "  ei.firstname as firstName, " +
            "  ei.lastname  as lastName, " +
            "  ei.middle_name as middleName, " +
            "  ei.phone_number as phoneNumber, " +
            "  ei.passport_serial_and_number as passportSerialNumber, " +
            " fi.name as futureInstitutionName, " +
            " d2.name as directionName, " +
            " ef.name as talimShakli, " +
            " l.language as language, " +
            "  d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber," +
            " d.edu_finishing_date as finishingDate " +
            " from application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "inner join future_institution fi on fi.id = a.future_institution_id " +
            "inner join edu_form ef on a.edu_form_id = ef.id " +
            "inner join direction d2 on d2.id = ef.direction_id " +
            "inner join language l on l.id = a.language_id " +
            "where d.is_active = true and a.future_institution_id=?1 and a.status =?2 " +
            "order by a.id ")
    List<GetAppToExcel> exportAppToExcel(Integer id, String status);

    //  Admin block
    @Query(nativeQuery = true, value = " select a.id  as appId, " +
            "  ei.firstname as firstName, " +
            "  ei.lastname  as lastName, " +
            "  ei.middle_name as middleName, " +
            "  ei.phone_number as phoneNumber, " +
            "  ei.passport_serial_and_number as passportSerialNumber, " +
            " fi.name as futureInstitutionName, " +
            " d2.name as directionName, " +
            " ef.name as talimShakli, " +
            " l.language as language, " +
            "  d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber," +
            " d.edu_finishing_date as finishingDate " +
            " from application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "inner join future_institution fi on fi.id = a.future_institution_id " +
            "inner join edu_form ef on a.edu_form_id = ef.id " +
            "inner join direction d2 on d2.id = ef.direction_id " +
            "inner join language l on l.id = a.language_id " +
            "where d.is_active = true and a.status =?1 " +
            "order by a.id ")
    Page<GetAppToExcel> getAllAppToAdmin(String status, Pageable pageable);

    // All APP by diploma status true
    @Query(nativeQuery = true, value = " select a.id  as appId, " +
            "  ei.firstname as firstName, " +
            "  ei.lastname  as lastName, " +
            "  ei.middle_name as middleName, " +
            "  ei.phone_number as phoneNumber, " +
            "  ei.passport_serial_and_number as passportSerialNumber, " +
            " fi.name as futureInstitutionName, " +
            " d2.name as directionName, " +
            " ef.name as talimShakli, " +
            " l.language as language, " +
            "  d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber," +
            " d.edu_finishing_date as finishingDate " +
            " from application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "inner join future_institution fi on fi.id = a.future_institution_id " +
            "inner join edu_form ef on a.edu_form_id = ef.id " +
            "inner join direction d2 on d2.id = ef.direction_id " +
            "inner join language l on l.id = a.language_id " +
            "where d.is_active = true and a.status='Ariza yuborildi' and a.diploma_status =?1 " +
            "order by a.id ")
    Page<GetAppToExcel> getAllAppDiplomaTrueToAdmin(Boolean status, Pageable pageable);

    // All APP by diploma status null
    @Query(nativeQuery = true, value = " select a.id  as appId, " +
            "  ei.firstname as firstName, " +
            "  ei.lastname  as lastName, " +
            "  ei.middle_name as middleName, " +
            "  ei.phone_number as phoneNumber, " +
            "  ei.passport_serial_and_number as passportSerialNumber, " +
            " fi.name as futureInstitutionName, " +
            " d2.name as directionName, " +
            " ef.name as talimShakli, " +
            " l.language as language, " +
            "  d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber," +
            " d.edu_finishing_date as finishingDate " +
            " from application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "inner join future_institution fi on fi.id = a.future_institution_id " +
            "inner join edu_form ef on a.edu_form_id = ef.id " +
            "inner join direction d2 on d2.id = ef.direction_id " +
            "inner join language l on l.id = a.language_id " +
            "where d.is_active = true and a.status='Ariza yuborildi' and a.diploma_status is null " +
            "order by a.id ")
    Page<GetAppToExcel> getAllAppByDiplomaNullToAdmin(Pageable pageable);

    // Diploma to admin
    @Query(nativeQuery = true, value = " select d.id as diplomaId," +
            " ei.firstname  as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber, " +
            " d.edu_finishing_date as finishingDate " +
            " from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active = true " +
            " and a.diploma_status = ?1 order by d.id ")
    Page<GetDiplomasToExcel> getAllDiplomaToAdmin(Boolean status, Pageable pageable);

    // Diploma null to admin
    @Query(nativeQuery = true, value = " select d.id as diplomaId," +
            " ei.firstname as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber, " +
            " d.edu_finishing_date as finishingDate " +
            " from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active = true and " +
            " a.diploma_status is null order by d.id ")
    Page<GetDiplomasToExcel> getAllDiplomaNullToAdmin(Pageable pageable);

    //  Foreign diploma to admin
    @Query(nativeQuery = true, value = " select d.id as diplomaId," +
            " ei.firstname  as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber, " +
            " d.edu_finishing_date as finishingDate " +
            " from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active = true and d.institution_old_name_id is null " +
            " and a.diploma_status = ?1 order by d.id ")
    Page<GetDiplomasToExcel> getAllForeignDiplomaToAdmin(Boolean status, Pageable pageable);

    //  Foreign null diploma to admin
    @Query(nativeQuery = true, value = " select d.id as diplomaId," +
            " ei.firstname as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber, " +
            " d.edu_finishing_date as finishingDate " +
            " from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active = true and d.institution_old_name_id is null " +
            " and a.diploma_status is null order by d.id ")
    Page<GetDiplomasToExcel> getAllForeignDiplomaNullToAdmin(Pageable pageable);

    // Export all diploma to admin
    @Query(nativeQuery = true, value = " select d.id as diplomaId," +
            " ei.firstname  as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber, " +
            " d.edu_finishing_date as finishingDate " +
            " from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active = true " +
            " and a.diploma_status = ?1 order by d.id ")
    List<GetDiplomasToExcel> exportAllDiplomaToAdmin(Boolean status);

    // Export all null diploma to admin
    @Query(nativeQuery = true, value = " select d.id as diplomaId," +
            " ei.firstname as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber, " +
            " d.edu_finishing_date as finishingDate " +
            " from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active = true and " +
            " a.diploma_status is null order by d.id ")
    List<GetDiplomasToExcel> exportAllDiplomaNullToAdmin();

    // Export all Foreign diploma to admin
    @Query(nativeQuery = true, value = " select d.id as diplomaId," +
            " ei.firstname  as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber, " +
            " d.edu_finishing_date as finishingDate " +
            " from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active = true and d.institution_old_name_id is null " +
            " and a.diploma_status = ?1 order by d.id ")
    List<GetDiplomasToExcel> exportAllForeignDiplomaToAdmin(Boolean status);

    // Export all null Foreign diploma to admin
    @Query(nativeQuery = true, value = " select d.id as diplomaId," +
            " ei.firstname as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber, " +
            " d.edu_finishing_date as finishingDate " +
            " from application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active = true and d.institution_old_name_id is null " +
            " and a.diploma_status is null order by d.id ")
    List<GetDiplomasToExcel> exportAllForeignDiplomaNullToAdmin();


    // Export all App diploma status true
    @Query(nativeQuery = true, value = " select a.id  as appId, " +
            "  ei.firstname as firstName, " +
            "  ei.lastname  as lastName, " +
            "  ei.middle_name as middleName, " +
            "  ei.phone_number as phoneNumber, " +
            "  ei.passport_serial_and_number as passportSerialNumber, " +
            " fi.name as futureInstitutionName, " +
            " d2.name as directionName, " +
            " ef.name as talimShakli, " +
            " l.language as language, " +
            "  d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber," +
            " d.edu_finishing_date as finishingDate " +
            " from application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            "inner join future_institution fi on fi.id = a.future_institution_id " +
            "inner join edu_form ef on a.edu_form_id = ef.id " +
            "inner join direction d2 on d2.id = ef.direction_id " +
            "inner join language l on l.id = a.language_id " +
            "where d.is_active = true and a.status='Ariza yuborildi' and a.diploma_status = true " +
            "order by a.id ")
    List<GetAppToExcel> exportAllAppDiplomaTrueToAdmin();


    // Export all App diploma status null
    @Query(nativeQuery = true, value = " select a.id  as appId, " +
            " ei.firstname as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " fi.name as futureInstitutionName, " +
            " d2.name as directionName, " +
            " ef.name as talimShakli, " +
            " l.language as language, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber," +
            " d.edu_finishing_date as finishingDate " +
            " from application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " inner join future_institution fi on fi.id = a.future_institution_id " +
            " inner join edu_form ef on a.edu_form_id = ef.id " +
            " inner join direction d2 on d2.id = ef.direction_id " +
            " inner join language l on l.id = a.language_id " +
            " where d.is_active = true and a.status='Ariza yuborildi' and a.diploma_status is null " +
            " order by a.id ")
    List<GetAppToExcel> exportAllAppByDiplomaNullToAdmin();

    // Export all App by status
    @Query(nativeQuery = true, value = " select a.id  as appId, " +
            " ei.firstname as firstName, " +
            " ei.lastname  as lastName, " +
            " ei.middle_name as middleName, " +
            " ei.phone_number as phoneNumber, " +
            " ei.passport_serial_and_number as passportSerialNumber, " +
            " fi.name as futureInstitutionName, " +
            " d2.name as directionName, " +
            " ef.name as talimShakli, " +
            " l.language as language, " +
            " d.institution_name as institutionName, " +
            " d.institution_old_name as institutionOldName, " +
            " d.speciality_name as specialityName, " +
            " d.diploma_serial_and_number as diplomaSerialAndNumber," +
            " d.edu_finishing_date as finishingDate " +
            " from application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " inner join future_institution fi on fi.id = a.future_institution_id " +
            " inner join edu_form ef on a.edu_form_id = ef.id " +
            " inner join direction d2 on d2.id = ef.direction_id " +
            " inner join language l on l.id = a.language_id " +
            " where d.is_active = true and a.status =?1 " +
            " order by a.id ")
    List<GetAppToExcel> exportAllAppToAdmin(String status);


    //ariza rad etildi yoki qabul qilindi
    @Query(nativeQuery = true, value = "select count(a.status) as count, a.status as status, fi.name as futureInstName, a.future_institution_id  as futureId " +
            " from application as a " +
            " inner join future_institution fi on fi.id = a.future_institution_id " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where a.future_institution_id=?1 and d.is_active=true " +
            " group by a.status, fi.name, a.future_institution_id ")
    List<AcceptAndRejectApp> getAcceptAndRejectApp(Integer id);

    @Query(nativeQuery = true, value = "select count(a.status) as count, a.status as status " +
            " from application as a " +
            "inner join future_institution fi on fi.id = a.future_institution_id " +
            "  inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where  d.is_active=true " +
            " group by a.status")
    List<AcceptAndRejectApp> getAcceptAndRejectAppAll();

    //diplom tekshirilmoqda

    @Query(nativeQuery = true, value = "select count(a.status) as count, a.status as status,fi.name as futureInstName,a.future_institution_id as futureId " +
            " from application as a " +
            " inner join future_institution fi on fi.id = a.future_institution_id " +
            " inner join  enrollee_info ei on a.enrollee_info_id = ei.id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where a.future_institution_id=?1 and d.is_active=true and a.status='Ariza yuborildi' and  a.diploma_status is null  " +
            " group by a.status, fi.name, a.future_institution_id ")
    Optional<AcceptAndRejectApp> getcheckDiploma(Integer id);

    @Query(nativeQuery = true, value = "select count(a.status) as count, a.status as status " +
            " from application as a " +
            " inner join future_institution fi on fi.id = a.future_institution_id " +
            " inner join  enrollee_info ei on a.enrollee_info_id = ei.id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where  d.is_active=true and a.status='Ariza yuborildi' and  a.diploma_status is null  " +
            " group by a.status ")
    Optional<AcceptAndRejectApp> getcheckDiplomaAll();

    @Query(nativeQuery = true, value = "select count(a.status) as count, a.status as status,fi.name as futureInstName,a.future_institution_id as futureId " +
            " from application as a " +
            " inner join future_institution fi on fi.id = a.future_institution_id " +
            " inner join  enrollee_info ei on a.enrollee_info_id = ei.id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where a.future_institution_id=?1 and d.is_active=true and a.status='Ariza yuborildi' and  a.diploma_status=true " +
            " group by a.status ,fi.name , a.future_institution_id ")
    Optional<AcceptAndRejectApp> getAcceptDiploma(Integer id);

    @Query(nativeQuery = true, value = "select count(a.status) as count, a.status as status" +
            " from application as a " +
            " inner join future_institution fi on fi.id = a.future_institution_id " +
            " inner join  enrollee_info ei on a.enrollee_info_id = ei.id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active=true and a.status='Ariza yuborildi' and  a.diploma_status=true " +
            " group by a.status ")
    Optional<AcceptAndRejectApp> getAcceptDiplomaAll();

    @Query(nativeQuery = true, value = "select  count(a.id) as count , CAST(a.created_date AS DATE) as sana from   application as a " +
            "inner join enrollee_info ei on ei.id = a.enrollee_info_id  inner join  diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active=true group by CAST(a.created_date AS DATE) order by sana ")
    List<GetCountAppallDate> getAppCountTodayBAdmin();

    @Query(nativeQuery = true, value = "select count(a.id) as count , ei.gender as gender from  application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id inner join diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active=true group by ei.gender")
    List<GetAppByGender> getCounAppAndGenderAdmin();


    @Query(nativeQuery = true, value = "select count(a. id) as count from  application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where future_institution_id=?1 and d.is_active=true")
    Optional<GetAppByGender> allInsAppbyAdmin(Integer id);

    @Query(nativeQuery = true, value = "select count(a. id) as count from  application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id " +
            " where d.is_active=true")
    Optional<GetAppByGender> allAppbyAdmin();
    @Query(nativeQuery = true, value = " select count(a.id),a.diploma_status as status from  application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            "inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.is_active=true and d.institution_old_name_id is not null group by a.diploma_status ")
    List<CountApp> allDiplomaCountByAdmin();
    @Query(nativeQuery = true, value = " select count(a.id),a.diploma_status from  application as a inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            "inner join diploma d on ei.id = d.enrollee_info_id " +
            "where d.is_active=true and d.institution_old_name_id is null group by a.diploma_status ")
    List<CountApp> allForeignDiplomaCountByAdmin();






    @Query(nativeQuery = true,value = "select count(a. id) as count from  application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id  where d.is_active=true and d.institution_old_name_id is null")
    Optional<CountApp>getAllCountForeignDAdmin();
    @Query(nativeQuery = true,value = "select count(a. id) as count from  application as a " +
            " inner join enrollee_info ei on ei.id = a.enrollee_info_id " +
            " inner join diploma d on ei.id = d.enrollee_info_id  where d.is_active=true and d.institution_old_name_id is not null")
    Optional<CountApp>getAllCountDiplomaAdmin();
}
