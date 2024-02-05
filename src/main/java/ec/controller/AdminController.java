package ec.controller;

import ec.constants.UserStatus;
import ec.constants.VStatus;
import ec.model.EntityModelMapper;
import ec.model.Search;
import ec.model.Student;
import ec.model.StudentDTO;
import ec.service.StudentService;
import ec.util.DateFormatter;
import ec.util.IpAndBrowserGetter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin
public class AdminController {
    private final StudentService studentService;
    @PostMapping("/secured/addStudent")
    public ResponseEntity<?> addStudent(@RequestBody @Valid Student student, HttpServletRequest request) {

        student.setInterviewDate(null);
        student.setService(student.getService());
        student.setIp(IpAndBrowserGetter.getIp(request));
        student.setLastUpdated(new Date());
        student.setBrowser(IpAndBrowserGetter.getBrowserAndOs(request));
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
        List<StudentDTO> studentDTOS = EntityModelMapper.studentModelToDto(allStudents);
        return new ResponseEntity<>(studentDTOS, HttpStatus.OK);
    }

    @PutMapping("/secured/updateStudent")
    public ResponseEntity<Student> updateStudent(@RequestBody StudentDTO studentDto, HttpServletRequest request) throws ParseException {
        Optional<Student> studentById = this.studentService.findStudentById(studentDto.getId());
        if (studentById.isEmpty()) {
            throw new UsernameNotFoundException("Student Not Found");
        }
        var student = new Student();
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
        student.setIp(IpAndBrowserGetter.getIp(request));
        student.setIsActive(UserStatus.ACTIVE.get());
        student.setBrowser(IpAndBrowserGetter.getBrowserAndOs(request));
        studentService.addStudent(student);

        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PostMapping("/secured/deleteStudent")
    public ResponseEntity<?> deleteStudent(@RequestBody StudentDTO student) {
        Optional<Student> studentById = this.studentService.findStudentById(student.getId());
        if (studentById.isEmpty()) {
            throw new UsernameNotFoundException("Student Not Found");
        }
        studentService.deleteById(student.getId());

        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

    @PostMapping("/secured/searchStudent")
    public ResponseEntity<?> searchStudent(@RequestBody Search search) throws Exception {
        if (StringUtils.isBlank(search.getKeyword())) {
            throw new Exception("Search keyword not found");
        }
        Optional<List<Student>> allStudents = this.studentService.findStudentByFirstNameEmailPhone(search.getKeyword());
        if (allStudents.isEmpty()) {
            throw new UsernameNotFoundException("Student Not Found");
        }
        List<StudentDTO> studentDTOS = EntityModelMapper.studentModelToDto(allStudents.get());
        return new ResponseEntity<>(studentDTOS, HttpStatus.OK);
    }
    @PostMapping("/secured/addUniversity")
    public ResponseEntity<?> addUniversity(@RequestBody @Valid Student student, HttpServletRequest request) {

        student.setInterviewDate(null);
        student.setService(student.getService());
        student.setIp(IpAndBrowserGetter.getIp(request));
        student.setLastUpdated(new Date());
        student.setBrowser(IpAndBrowserGetter.getBrowserAndOs(request));
        student.setCreated(new Date());
        student.setIsActive(UserStatus.ACTIVE.get());
        student.setEnteredBy(request.getUserPrincipal().getName());
        student.setVisaStatus(VStatus.NOT_APPLIED.get());
        studentService.addStudent(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }
}
