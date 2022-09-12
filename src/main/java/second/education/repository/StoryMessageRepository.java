package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.StoryMessage;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryMessageRepository extends JpaRepository<StoryMessage, Integer> {

    @Query(value = "select sm from StoryMessage  as sm where sm.application.id=?1")
    List<StoryMessage> getAllStoryByAppId(Integer id);


    @Query(value = "select sm from  StoryMessage as sm where sm.status='Ariza qabul qilindi' and sm.application.id=?1")
    Optional<StoryMessage> getTest(Integer id);

}
