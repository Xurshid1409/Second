package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.classificator.EduForm;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EduFormResponse {

    private Integer id;
    private String name;

    public EduFormResponse(EduForm eduForm) {
        this.id = eduForm.getId();
        this.name = eduForm.getName();
    }
}
