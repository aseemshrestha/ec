package ec.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
@Table(name = "university")
@Entity
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    private String universityName;

    @Column(nullable = false)
    private String universityAddress;

    @Column(nullable = true)
    private String universityAddress1;
    @Column(nullable = true)
    private String universityContactPerson;

    @Column(nullable = false)
    private String universityPhone;
    @Column(nullable = false)
    private String phone;
    @Column(name = "additionalComments")
    private String additionalComments;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String ip;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String browser;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int isActive;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "approvalDate")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date approvalDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "lastUpdated", nullable = false)
    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date lastUpdated;

    @Column(nullable = false)
    private String enteredBy;


}
