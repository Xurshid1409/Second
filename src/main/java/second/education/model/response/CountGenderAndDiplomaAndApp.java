package second.education.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter
public class CountGenderAndDiplomaAndApp {

    List<GetAppByGender> diplomaGenderCount;
    List<GetCountAppallDate> diplomaCountToday;
    List<GetAppByGender> ForeigndiplomaGenderCount;
    List<GetCountAppallDate> ForeigndiplomaCountToday;
}
