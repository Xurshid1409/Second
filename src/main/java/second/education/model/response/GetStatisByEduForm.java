package second.education.model.response;

import java.util.List;

public interface GetStatisByEduForm {

    Integer getEduFormId();
    String getEduFormName();
    List<GetStatisByLanguage> getStatisByLanguage();
}
