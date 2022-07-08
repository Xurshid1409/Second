package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private String message;
    private boolean isSuccess;
    private Object object;

    public Result(String message, boolean isSuccess) {
        this.message = message;
        this.isSuccess = isSuccess;
    }
}
