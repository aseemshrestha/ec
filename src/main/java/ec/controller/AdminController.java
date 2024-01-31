package ec.controller;

import ec.constants.UserStatus;
import ec.model.Student;
import ec.service.StudentService;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/secured")
@RequiredArgsConstructor
@CrossOrigin
public class AdminController {

    private final StudentService studentService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        return null;
    }

    @PostMapping("/addStudent")
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

        studentService.addStudent(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }
}
