package spring.rest.api.doc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.rest.api.doc.domain.Characters;

@Repository
public interface CharacterRepository extends JpaRepository<Characters, Long> {
}
