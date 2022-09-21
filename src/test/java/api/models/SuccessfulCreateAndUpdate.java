package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuccessfulCreateAndUpdate {
    private String name;
    private String job;
    private int id;
    private String createdAt;
    private String updatedAt;
}
