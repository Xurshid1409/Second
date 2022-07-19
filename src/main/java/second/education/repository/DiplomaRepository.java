package second.education.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.Diploma;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiplomaRepository extends JpaRepository<Diploma, Integer> {

    @Query(nativeQuery = true, value = "select * from diploma d " +
            "join enrollee_info ei on ei.id = d.enrollee_info_id " +
            "join users u on ei.user_id = u.id where u.phone_number = ?1 and d.degree_id != 3 ")
    List<Diploma> findAllByEnrolleeInfo(String phoneNumber);

    @Query(nativeQuery = true, value = "select * from diploma d " +
            "join enrollee_info ei on ei.id = d.enrollee_info_id " +
            "join users u on ei.user_id = u.id where u.phone_number = ?1 and d.id =?2")
    Optional<Diploma> findByIdAndEnrolleeInfo(String phoneNumber, Integer diplomaId);


    @Query(nativeQuery = true, value = "select * from diploma d " +
            "join enrollee_info ei on ei.id = d.enrollee_info_id " +
            "join users u on ei.user_id = u.id where u.phone_number = ?1 and d.is_active = true ")
    Optional<Diploma> getDiplomaProfile(String phoneNumber);

    @Query(nativeQuery = true, value = "select * from diploma d " +
            "join enrollee_info ei on ei.id = d.enrollee_info_id " +
            "join users u on ei.user_id = u.id where u.phone_number = ?1 ")
    List<Diploma> findAllDiplomaByEnrollee(String phoneNumber);


    @Query("select d from  Diploma  as d where d.institutionId=?1 and d.isActive=true ")
    Page<Diploma> getAllDiplomebyUAdmin(Integer id, Pageable pageable);


    @Query("select d from  Diploma  as d where d.institutionId=?1 and d.isActive=true and d.id=?2 ")
    Optional<Diploma> getByIdDiplomebyUAdmin(Integer institutionId, Integer diplomaId);


    @Query("select d from  Diploma as d where d.enrolleeInfo.id=?1 and d.isActive=true ")
    Optional<Diploma> getDiplomaByEnrolleeInfoId(Integer id);
}
