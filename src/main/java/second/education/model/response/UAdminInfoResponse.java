package second.education.model.response;

import lombok.Getter;
import lombok.Setter;
import second.education.domain.AdminEntity;

@Getter
@Setter
public class UAdminInfoResponse {

    private Integer id;
    private String firstname;
    private String lastname;

    public UAdminInfoResponse(AdminEntity adminEntity) {
        this.id = adminEntity.getId();
        this.firstname = adminEntity.getFistName();
        this.lastname = adminEntity.getLastname();
    }
}
