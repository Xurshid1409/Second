package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.classificator.EduForm;
import second.education.domain.classificator.Language;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EduFormResponse {

    private Integer id;
    private String name;
    private List<LanguageResponse> languages;
    private Integer directionId;
    private String directionName;

    public EduFormResponse(EduForm eduForm, List<LanguageResponse> languages) {
        this.id = eduForm.getId();
        this.name = eduForm.getName();
        this.directionId = eduForm.getDirection().getId();
        this.directionName = eduForm.getDirection().getName();
        this.languages = languages;
    }

    public EduFormResponse(EduForm eduForm) {
        this.id = eduForm.getId();
        this.name = eduForm.getName();
        this.directionId = eduForm.getDirection().getId();
        this.directionName = eduForm.getDirection().getName();
    }
}
