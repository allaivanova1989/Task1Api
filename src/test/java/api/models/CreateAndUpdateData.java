package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class CreateAndUpdateData {
    private String name;
    private String job;

    public CreateAndUpdateData(String name, String job) {
        this.name = name;
        this.job = job;
    }
}
