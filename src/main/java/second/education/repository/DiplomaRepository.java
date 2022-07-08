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
            "join users u on ei.user_id = u.id where u.phone_number = ?1")
    List<Diploma> findAllByEnrolleeInfo(String phoneNumber);

    @Query(nativeQuery = true, value = "select * from diploma d " +
            "join enrollee_info ei on ei.id = d.enrollee_info_id " +
            "join users u on ei.user_id = u.id where u.phone_number = ?1 and d.id = ?2")
    Optional<Diploma> findByIdAndEnrolleeInfo(String phoneNumber, Integer diplomaId);


    @Query(nativeQuery = true, value = "select * from diploma d " +
            "join enrollee_info ei on ei.id = d.enrollee_info_id " +
            "join users u on ei.user_id = u.id where u.phone_number = ?1")
    Optional<Diploma> getDiplomaByPrincipal(String phoneNumber);
}
