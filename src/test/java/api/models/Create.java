package api.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Create {
    private String name;
    private String job;
}
