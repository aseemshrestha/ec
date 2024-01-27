package ec.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import jakarta.persistence.Id;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Role implements Serializable {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role;

    public Role() {
    }
}