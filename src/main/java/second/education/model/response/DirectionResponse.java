package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import second.education.domain.classificator.Direction;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class DirectionResponse {

    private Integer id;
    private String name;
//    private Integer futureInstitutionId;
//    private Integer futureInstitutionName;

    public DirectionResponse(Direction direction) {
        this.id = direction.getId();
        this.name = direction.getName();
    }
}
