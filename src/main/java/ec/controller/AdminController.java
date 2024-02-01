package ec.controller;

import ec.constants.UserStatus;
import ec.constants.VStatus;
import ec.model.Student;
import ec.model.StudentDTO;
import ec.service.StudentService;
import ec.util.DateFormatter;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin
public class AdminController {

    private final StudentService studentService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        return null;
    }

    @PostMapping("/secured/addStudent")
    public ResponseEntity<?> addStudent(@RequestBody @Valid Student student, HttpServletRequest request) throws Exception {

        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        student.setInterviewDate(null);
        student.setService(student.getService());
        student.setIp(ip);
        student.setLastUpdated(new Date());
        student.setBrowser(userAgent.getBrowser().getName() + "-" + userAgent.getOperatingSystem());
        student.setCreated(new Date());
        student.setIsActive(UserStatus.ACTIVE.get());
        student.setEnteredBy(request.getUserPrincipal().getName());
        student.setVisaStatus(VStatus.NOT_APPLIED.get());

        studentService.addStudent(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @GetMapping("/secured/showAll")
    public ResponseEntity<List<StudentDTO>> showStudent() {
        List<Student> allStudents = studentService.getAllActiveStudents();
        List<StudentDTO> studentDTOS = new ArrayList<>();
        allStudents.forEach(s -> {
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setId(s.getId());
            studentDTO.setFirstName(s.getFirstName());
            studentDTO.setLastName(s.getLastName());
            studentDTO.setEmail(s.getEmail());
            studentDTO.setPhone(s.getPhone());
            studentDTO.setUniversityApplied(s.getUniName());
            studentDTO.setI20Status(s.getService());
            if (s.getInterviewDate() == null) {
                studentDTO.setVisaInterviewDate("Not Applied");
            } else {
                studentDTO.setVisaInterviewDate(s.getInterviewDate().toString());
            }
            studentDTO.setVisaStatus(s.getVisaStatus());
            studentDTO.setCreatedDate(DateFormatter.formatDateToString(s.getCreated(), "MMM dd yyyy", "EST"));
            studentDTOS.add(studentDTO);
        });

        return new ResponseEntity<>(studentDTOS, HttpStatus.OK);
    }

    @PatchMapping("/updateStudent")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student, @PathVariable Long id, HttpServletRequest request) {
        Optional<Student> studentById = this.studentService.findStudentById(id);
        if (studentById.isEmpty()) {
            throw new UsernameNotFoundException("Student Not Found");
        }
        student.setFirstName(student.getFirstName());
        student.setLastName(student.getLastName());
        student.setUniName(student.getUniName());
        student.setEmail(student.getEmail());
        student.setVisaStatus(student.getVisaStatus());
        student.setInterviewDate(student.getInterviewDate());
        student.setVisaStatus(student.getVisaStatus());
        student.setAdditionalComments(student.getAdditionalComments());
        student.setLastUpdated(student.getLastUpdated());
        student.setIsActive(student.getIsActive());
        student.setService(student.getService());
        student.setEnteredBy(request.getUserPrincipal().getName());
        studentService.addStudent(student);
        return new ResponseEntity<Student>(student, HttpStatus.OK);
    }

    @PostMapping("/secured/deleteStudent")
    public ResponseEntity<?> deleteStudent(@RequestBody StudentDTO student, HttpServletRequest request) {
        Optional<Student> studentById = this.studentService.findStudentById(student.getId());
        if (studentById.isEmpty()) {
            throw new UsernameNotFoundException("Student Not Found");
        }
        studentService.deleteById(student.getId());

        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }
}
