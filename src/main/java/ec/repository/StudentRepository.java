package ec.repository;

import ec.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s from Student s where s.email = :email and s.isActive = 1")
    Optional<Student> findByEmail(String email);

    @Query("SELECT s from Student s where s.isActive = 1")
    List<Student> findAllActiveStudents();

    @Query("UPDATE Student s SET s.isActive = 4 where s.id = :id")
    @Modifying
    void deleteStudentById(@Param("id") Long id);

    @Query("SELECT s FROM Student s WHERE s.firstName LIKE %:keyword% or s.phone LIKE %:keyword% or s.email LIKE %:keyword%")
    Optional<List<Student>> findStudentByFirstNameOrEmailOrPhone(String keyword);


}
