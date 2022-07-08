package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.classificator.Language;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LanguageResponse {

    private Integer id;
    private String name;

    public LanguageResponse(Language language) {
        this.id = language.getId();
        this.name = language.getLanguage();
    }
}
