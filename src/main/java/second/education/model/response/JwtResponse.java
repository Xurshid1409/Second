package second.education.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.security.SecurityConstant;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {

    private String token;
    private Integer userId;
    private String username;
    private Long token_expire;
    private List<String> roles;

    public JwtResponse(Integer userId, String username, String token, List<String> roles) {
        this.userId = userId;
        this.username = username;
        this.token = token;
        this.token_expire = SecurityConstant.TOKEN_EXPIRE_AT;
        this.roles = roles;
    }
}
