package ec.controller;

import ec.constants.UserStatus;
import ec.constants.VStatus;
import ec.model.Student;
import ec.model.StudentDTO;
import ec.repository.StudentRepository;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
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
    private final StudentRepository studentRepository;

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
            studentDTO.setVisaInterviewDate(s.getInterviewDate() == null ? null : s.getInterviewDate().toString());
            studentDTO.setVisaStatus(s.getVisaStatus());
            studentDTO.setCreatedDate(DateFormatter.formatDateToString(s.getCreated(), "MMM dd yyyy", "EST"));
            studentDTO.setAdditionalComments(s.getMessage());
            studentDTOS.add(studentDTO);
        });

        return new ResponseEntity<>(studentDTOS, HttpStatus.OK);
    }

    @PutMapping("/secured/updateStudent")
    public ResponseEntity<Student> updateStudent(@RequestBody StudentDTO studentDto, HttpServletRequest request) throws ParseException {
        Optional<Student> studentById = this.studentService.findStudentById(studentDto.getId());
        if (studentById.isEmpty()) {
            throw new UsernameNotFoundException("Student Not Found");
        }
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        Student student = new Student();
        student.setId(studentDto.getId());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setUniName(studentDto.getUniversityApplied());
        student.setEmail(studentDto.getEmail());
        student.setPhone(studentDto.getPhone());
        student.setVisaStatus(studentDto.getVisaStatus());
        student.setMessage(studentDto.getAdditionalComments());

        student.setInterviewDate(DateFormatter.formatStringToDateOnly(studentDto.getVisaInterviewDate(), "yyyy-MM-dd"));
        student.setVisaStatus(studentDto.getVisaStatus());
        student.setService(studentDto.getI20Status());
        student.setEnteredBy(request.getUserPrincipal().getName());

        student.setCreated(studentById.get().getCreated());
        student.setEnteredBy(studentById.get().getEnteredBy());
        student.setLastUpdated(new Date());
        student.setIp(ip);
        student.setIsActive(UserStatus.ACTIVE.get());
        student.setBrowser(userAgent.getBrowser().getName());
        studentService.addStudent(student);



        return new ResponseEntity<>(student, HttpStatus.OK);
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
