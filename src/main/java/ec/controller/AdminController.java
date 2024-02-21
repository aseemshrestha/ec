package ec.controller;

import ec.cloud.AWSS3Service;
import ec.cloud.AwsConfig;
import ec.constants.UserStatus;
import ec.constants.VStatus;
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
import org.springframework.web.multipart.MultipartFile;

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
    private final UniversityService universityService;

    private final AWSS3Service awss3Service;

    @PostMapping("/secured/addStudent")
    public ResponseEntity<?> addStudent(@RequestBody @Valid Student student, HttpServletRequest request) {

        student.setInterviewDate(null);
        student.setI20Status(student.getI20Status());
        student.setIp(IpAndBrowserGetter.getIp(request));
        student.setLastUpdated(new Date());
        student.setBrowser(IpAndBrowserGetter.getBrowserAndOs(request));
        student.setCreated(new Date());
        student.setIsActive(UserStatus.ACTIVE.get());
        student.setEnteredBy(request.getUserPrincipal().getName());
        student.setVisaStatus(VStatus.NOT_APPLIED.get());
        student.setAdditionalComments(student.getAdditionalComments());
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
    public ResponseEntity<Student> updateStudent(@RequestParam(value = "id") Long Id,
                                                 @RequestParam(value = "firstName", required = true) String firstName,
                                                 @RequestParam(value = "lastName", required = true) String lastName,
                                                 @RequestParam(value = "email", required = true) String email,
                                                 @RequestParam(value = "i20Status", required = false) String i20Status,
                                                 @RequestParam(value = "phone", required = true) String phone,
                                                 @RequestParam(value = "universityApplied", required = true) String universityApplied,
                                                 @RequestParam(value = "visaStatus", required = true) String visaStatus,
                                                 @RequestParam(value = "file", required = false) MultipartFile[] files,
                                                 @RequestParam(value = "additionalComments", required = false) String additionalComments,
                                                 @RequestParam(value = "visaInterviewDate", required = false) String visaInterviewDate,
                                                 HttpServletRequest request) throws Exception {
        Optional<Student> studentById = this.studentService.findStudentById(Id);
        if (studentById.isEmpty()) {
            throw new ResourceNotFoundException("Student Not Found");
        }
        Student student = studentById.get();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setPhone(phone);
        student.setI20Status(i20Status);
        student.setAdditionalComments(additionalComments);
        student.setEnteredBy(request.getUserPrincipal().getName());
        student.setVisaStatus(visaStatus);
        student.setInterviewDate(DateFormatter.formarStringToDate(visaInterviewDate));
        student.setUniName(universityApplied);
        student.setLastUpdated(new Date());
        String path = student.getEmail().substring(0, student.getEmail().indexOf('@'));
        if (files != null) {
           // Thread.sleep(2000);
            List<String> uploadedFiles = awss3Service.uploadFileList(files, path);
            List<Docs> docsList = new ArrayList<>();
            if (uploadedFiles.isEmpty()) {
                throw new Exception("Invalid content type for files");
            }
            uploadedFiles.forEach(f -> {
                Docs docs = new Docs();
                docs.setDocsLocation(AwsConfig.getS3EndPoint() + f);
                // media.setDocsDescription(description);
                docs.setCreated(new Date());
                docs.setLastUpdated(new Date());
                docs.setStudent(student);
                docsList.add(docs);
            });
            student.setDocs(docsList);
        }
        studentService.addStudent(student);
        return new ResponseEntity<>(studentById.get(), HttpStatus.OK);
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
        university.setApprovalDate(DateFormatter.formarStringToDate(universityDto.getApprovalDate()));
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
