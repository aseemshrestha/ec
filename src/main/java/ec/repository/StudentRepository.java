package ec.repository;

import ec.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s from Student s where s.email = :email and s.isActive = 1")
    Optional<Student> findByEmail(String email);
}
