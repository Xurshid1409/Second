package second.education.model.response;

import java.util.List;

public interface GetStatisByEduForm {

    Integer getEduFormId();
    String getEduFormNAme();
    List<GetStatisByLanguage> getStatisByLanguage();
}
