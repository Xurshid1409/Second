package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisDirectionResponse {

    private Integer directionId;
    private String directionName;
    private List<StatisEduFormResponse> statisEduFormResponses;

    public StatisDirectionResponse(Integer directionId, String directionName) {
        this.directionId = directionId;
        this.directionName = directionName;
    }
}
