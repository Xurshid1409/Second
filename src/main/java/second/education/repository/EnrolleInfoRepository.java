package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.EnrolleeInfo;
import second.education.domain.User;

import java.util.Optional;

@Repository
public interface EnrolleInfoRepository extends JpaRepository<EnrolleeInfo, Integer> {

    Optional<EnrolleeInfo> findByUser(User user);
}
