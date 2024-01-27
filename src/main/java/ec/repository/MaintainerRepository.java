package ec.repository;

import ec.model.Maintainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintainerRepository extends JpaRepository<Maintainer, Integer> {
}
