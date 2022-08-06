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
public class CountByUAdmin {

    private List<CountApp> countAppByDiplomaStatus;
    private List<CountApp> countApp;
    private List<CountApp> countForeignDiploma;
    private List<CountApp> countDiploma;
}
