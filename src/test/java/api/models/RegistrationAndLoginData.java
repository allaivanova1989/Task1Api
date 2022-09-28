package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class
RegistrationAndLoginData {
    private String email;
    private String password;


}
