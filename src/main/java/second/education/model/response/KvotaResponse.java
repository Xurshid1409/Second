package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.classificator.Kvota;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KvotaResponse {

    private Integer id;
    private String language;
    private String talimShakli;
    private Integer directionId;
    private String directionName;

    public KvotaResponse(Kvota kvota) {
        this.id = kvota.getId();
        this.language = kvota.getLanguage();
        this.talimShakli = kvota.getTalimShakli();
        this.directionId = kvota.getDirection().getId();
        this.directionName = kvota.getDirection().getName();
    }
}
