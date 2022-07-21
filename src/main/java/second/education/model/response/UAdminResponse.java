package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UAdminResponse {

    private Integer Id;
    private String pinfl;
    private Integer futureInstId;
    private String futureInstName;
    private List<UniversityResponse> universityResponses;

}
