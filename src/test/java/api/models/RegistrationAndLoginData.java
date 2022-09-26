package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class RegistrationAndLoginData {
    private String email;
    private String password;

    public RegistrationAndLoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
