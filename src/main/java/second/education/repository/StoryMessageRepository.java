package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.StoryMessage;

@Repository
public interface StoryMessageRepository extends JpaRepository<StoryMessage, Integer> {
}
