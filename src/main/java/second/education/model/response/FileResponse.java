package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {

    private DiplomaCopyResponse diplomaCopyResponse;
    private DiplomaIlovaResponse diplomaIlovaResponse;
}
