package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryMessageResponse {
    private String diplomMessage;
    private String appMessage;
    private String appStatus;
    private String diplomaStatus;
}
