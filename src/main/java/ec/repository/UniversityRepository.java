package ec.repository;

import ec.model.Student;
import ec.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {

    @Query("SELECT u from University u where u.isActive = 1")
    List<University> findAllActiveUniversities();


    @Query("SELECT u from University u where u.universityName = :universityName and u.isActive = 1")
    University getUniversityByName(String universityName);

}
