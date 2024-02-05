package ec.service;

import ec.model.Student;
import ec.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    @Transactional(rollbackFor = Exception.class)
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> findStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public Optional<Student> findStudentById(Long id) {
        return studentRepository.findById(id);
    }


    public List<Student> getAllActiveStudents() {
        return studentRepository.findAllActiveStudents();
    }

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    public void deleteById(Long id) {
        studentRepository.deleteStudentById(id);
    }

    public Optional<List<Student>> findStudentByFirstNameEmailPhone(String keyword) {
        return studentRepository.findStudentByFirstNameOrEmailOrPhone(keyword);
    }
}
