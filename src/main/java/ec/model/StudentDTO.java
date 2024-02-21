package ec.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class StudentDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String universityApplied;
    private String email;
    private String phone;
    private String i20Status;
    private String visaInterviewDate;
    private String visaStatus;

    private String additionalComments;

    private String status;

    private String createdDate;

    private MultipartFile[] files;

    private List<String> docs;

}
