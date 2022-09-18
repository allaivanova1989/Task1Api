package api.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationAndLogin {
    private String email;
    private String password;
}
