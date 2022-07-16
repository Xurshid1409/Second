package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisLanguageResponse {

    private String languageName;
    private Integer kvotaSoni;
    private Integer count;
}
