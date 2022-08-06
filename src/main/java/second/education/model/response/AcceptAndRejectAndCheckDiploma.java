package second.education.model.response;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class AcceptAndRejectAndCheckDiploma {

    private Integer acceptAppCount;
    private Integer rejectAppCount;
    private Integer checkDiplomaCount;
    private Integer AcceptDiplomaCount;
    private String futureInstName;
    private Integer allAppCount;

}
