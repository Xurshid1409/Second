package second.education.api_model.one_id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OneIdResponseToken {

    private long expires_in;
    private String access_token;
    private String refresh_token;
    private String scope;
}
