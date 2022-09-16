package api.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Update {
    private String name;
    private String job;
}
