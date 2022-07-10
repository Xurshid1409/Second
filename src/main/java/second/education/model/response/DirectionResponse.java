package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import second.education.domain.classificator.Direction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectionResponse {

    private Integer id;
    private String name;
    private Integer futureInstitutionId;
    private String futureInstitutionName;

    public DirectionResponse(Direction direction) {
        this.id = direction.getId();
        this.name = direction.getName();
    }
}
