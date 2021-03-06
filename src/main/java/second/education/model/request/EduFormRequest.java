package second.education.model.request;

import lombok.Getter;
import lombok.Setter;
import second.education.domain.classificator.Language;

import java.util.List;

@Getter
@Setter
public class EduFormRequest {

    private String name;
    private Integer directionId;
    private List<LanguageRequest> languages;
}
