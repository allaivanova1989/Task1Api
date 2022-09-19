package api.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationAndLoginModel {
    private String email;
    private String password;
}
