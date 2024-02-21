package ec.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "user", indexes = {@Index(name = "idxemail", columnList = "email", unique = true), @Index(name = "idxusername", columnList = "username", unique = true)})
@Data
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)

    private String lastName;

    @Column(nullable = false, unique = true, length = 200)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false, unique = true, length = 200)
    private String email;
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

    @ManyToOne
    @JoinColumn
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Role role;




}