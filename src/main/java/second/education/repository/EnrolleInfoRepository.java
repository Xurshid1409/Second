package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.EnrolleeInfo;

import java.util.Optional;

@Repository
public interface EnrolleInfoRepository extends JpaRepository<EnrolleeInfo, Integer> {

    @Query(nativeQuery = true, value = "select * from enrollee_info join users u on u.id = enrollee_info.user_id " +
            "where u.phone_number=?1")
    Optional<EnrolleeInfo> findByEnrolle(String phoneNumber);

    @Query("select e from EnrolleeInfo e where e.pinfl = ?1")
    Optional<EnrolleeInfo> findByPinfl(String pinfl);

}
