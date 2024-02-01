package ec.model;

import lombok.Data;

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

    private String createdDate;

}
