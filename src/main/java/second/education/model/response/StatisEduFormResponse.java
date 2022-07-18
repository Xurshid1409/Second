package second.education.model.response;

import java.util.List;

public interface StatisEduFormResponse {

    Integer getEduFormId();
    String getEduFormName();
    List<StatisLanguageResponse> getStatisLanguageResponses();

}
