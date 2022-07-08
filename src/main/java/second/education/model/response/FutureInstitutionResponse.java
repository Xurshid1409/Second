package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.classificator.FutureInstitution;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FutureInstitutionResponse {

    private Integer id;
    private String name;

    public FutureInstitutionResponse(FutureInstitution futureInstitution) {
        this.id = futureInstitution.getId();
        this.name = futureInstitution.getName();
    }
}
