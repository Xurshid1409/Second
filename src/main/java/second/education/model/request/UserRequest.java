package second.education.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRequest {

    private String phoneNumber;
    private String password;
    private Integer futureInstId;
    private List<Integer> universitiesId;
    private String pinfl;
}
