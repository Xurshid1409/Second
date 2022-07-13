package second.education.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    private String phoneNumber;
    private String password;
    private Integer futureInstId;
    private Integer roleId;
}
