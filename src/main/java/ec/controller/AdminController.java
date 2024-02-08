package ec.controller;

import ec.constants.UserStatus;
import ec.constants.VStatus;
import ec.exceptions.AlreadyExistsException;
import ec.exceptions.ResourceNotFoundException;
import ec.model.*;
import ec.service.StudentService;
import ec.service.UniversityService;
import ec.util.DateFormatter;
import ec.util.IpAndBrowserGetter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final UniversityService universityService;

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
            throw new ResourceNotFoundException("Student Not Found");
        }
        var student = studentById.get();
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
            throw new ResourceNotFoundException("Student Not Found");
        }
        studentService.deleteById(student.getId());

        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

    @PostMapping("/secured/deleteUniversity")
    public ResponseEntity<?> deleteUniversity(@RequestBody UniversityDto universityDto) {
        this.universityService.deleteUniversityById(universityDto.getId());
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

    @PostMapping("/secured/searchStudent")
    public ResponseEntity<?> searchStudent(@RequestBody Search search) throws Exception {
        if (StringUtils.isBlank(search.getKeyword())) {
            throw new Exception("Search keyword not found");
        }
        Optional<List<Student>> allStudents = this.studentService.findStudentByFirstNameEmailPhone(search.getKeyword());
        if (allStudents.isEmpty()) {
            throw new ResourceNotFoundException("Student not Found");
        }
        List<StudentDTO> studentDTOS = EntityModelMapper.studentModelToDto(allStudents.get());
        return new ResponseEntity<>(studentDTOS, HttpStatus.OK);
    }

    @PostMapping("/secured/addUniversity")
    public ResponseEntity<?> addUniversity(@RequestBody @Valid University university, HttpServletRequest request) throws Exception {
        University universityByName = universityService.getUniversityByName(university.getUniversityName());
        if (universityByName != null) {
            throw new ResourceNotFoundException("University already exists");
        }
        university.setIp(IpAndBrowserGetter.getIp(request));
        university.setBrowser(IpAndBrowserGetter.getBrowserAndOs(request));
        university.setLastUpdated(new Date());
        university.setCreated(new Date());
        university.setEnteredBy(request.getUserPrincipal().getName());
        university.setApprovalDate(null);
        university.setIsActive(UserStatus.ACTIVE.get());
        universityService.saveUniversity(university);
        return new ResponseEntity<>(university, HttpStatus.CREATED);
    }

    @GetMapping("/secured/showUniversities")
    public ResponseEntity<List<UniversityDto>> listUniversities() {
        List<University> universities = universityService.getAllActiveUniversities();
        List<UniversityDto> universityDtos = EntityModelMapper.universityEntityToDto(universities);
        return new ResponseEntity<>(universityDtos, HttpStatus.OK);
    }

    @PutMapping("/secured/updateUniversity")
    public ResponseEntity<University> updateUniversity(@RequestBody UniversityDto universityDto,
                                                       HttpServletRequest request) throws ParseException {
        Optional<University> universityById = this.universityService.findUniversityById(universityDto.getId());
        if (universityById.isEmpty()) {
            throw new ResourceNotFoundException("University Not Found");
        }
        var university = universityById.get();
        university.setId(universityDto.getId());
        university.setUniversityAddress(universityDto.getUniversityAddress());
        university.setUniversityAddress1(universityDto.getUniversityAddress1());
        university.setPhone(universityDto.getPhone());
        university.setContactPerson(universityDto.getContactPerson());
        university.setAdditionalComments(universityDto.getAdditionalComments());
        university.setApprovalDate(DateFormatter.formatStringToDateOnly(universityDto.getApprovalDate(),"yyyy-MM-dd"));
        university.setEmail(universityDto.getEmail());
        university.setService(universityDto.getStatus());
        university.setEnteredBy(request.getUserPrincipal().getName());
        university.setLastUpdated(new Date());
        university.setIp(IpAndBrowserGetter.getIp(request));
        university.setIsActive(UserStatus.ACTIVE.get());
        university.setBrowser(IpAndBrowserGetter.getBrowserAndOs(request));
        universityService.saveUniversity(university);

        return new ResponseEntity<>(university, HttpStatus.OK);
    }
}
