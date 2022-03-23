package spring.rest.api.doc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.rest.api.doc.domain.Weapon;

@Repository
public interface WeaponRepository extends JpaRepository<Weapon, Long> {
}
