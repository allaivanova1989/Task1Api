package api.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAndUpdateModel {
    private String name;
    private String job;
}
