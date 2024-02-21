package ec.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Data
public class Docs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String docsLocation;
    private String docsDescription;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, updatable = false)
    @CreatedDate
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastUpdated", nullable = false)
    @LastModifiedDate
    private Date lastUpdated;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Student student;

}
