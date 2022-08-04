package second.education.model.response;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class AcceptAndRejectAndCheckDiploma {

    private Integer acceptCount;
    private Integer rejectCount;
    private Integer checkCount;
    private String futureInstName;

}
