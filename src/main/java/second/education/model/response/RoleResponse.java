package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import second.education.domain.classificator.Role;

@Getter
@Setter
@AllArgsConstructor
public class RoleResponse {

    private int id;
    private String name;

    public RoleResponse(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }
}
