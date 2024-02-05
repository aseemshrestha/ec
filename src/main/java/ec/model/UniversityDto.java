package ec.model;

import lombok.Data;

import java.util.Date;

@Data
public class UniversityDto {

    private Long id;

    private String universityName;

    private String universityAddress;

    private String universityAddress1;
    private String contactPerson;

    private String phone;
    private String additionalComments;
    private String ip;
    private String browser;
    private int isActive;
    private String approvalDate;
    private String created;
    private String lastUpdated;
    private String enteredBy;
    private String status;

    private String email;
}
