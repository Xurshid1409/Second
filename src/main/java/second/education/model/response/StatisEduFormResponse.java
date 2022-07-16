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
public class StatisEduFormResponse {

    private Integer eduFormId;
    private String eduFormName;
    private List<StatisLanguageResponse> statisLanguageResponses;
}
