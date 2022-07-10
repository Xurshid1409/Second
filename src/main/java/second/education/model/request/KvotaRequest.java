package second.education.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KvotaRequest {

    private Integer id;
    private String language;
    private String talimShakli;
    private Integer soni;
    private Integer directionId;
}
