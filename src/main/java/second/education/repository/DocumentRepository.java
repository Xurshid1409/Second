package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
}
