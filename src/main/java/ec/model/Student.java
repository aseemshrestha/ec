package ec.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "student")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, length = 200)
    private String email;

    @Column(nullable = false)
    private String uniName;
    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, name="i20Status")
    private String service;

    @Column(name = "interviewDate")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date interviewDate;
    private String additionalComments;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String ip;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String browser;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int isActive;
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
