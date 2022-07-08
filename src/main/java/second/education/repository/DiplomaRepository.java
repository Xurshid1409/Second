package second.education.repository;

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
            "join users u on ei.user_id = u.id where u.id = ?1")
    List<Diploma> findAllByEnrolleeInfo(Integer userId);

    @Query(nativeQuery = true, value = "select * from diploma d " +
            "join enrollee_info ei on ei.id = d.enrollee_info_id " +
            "join users u on ei.user_id = u.id where u.id = ?1 and d.id = ?2")
    Optional<Diploma> findByIdAndEnrolleeInfo(Integer userId, Integer diplomaId);


    @Query("select d from  Diploma as d where d.enrolleeInfo.user.phoneNumber=?1")
    Optional<Diploma> getDiplomaByPrincipal(String phoneNumber);
}
