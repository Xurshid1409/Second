package second.education.model.response;

import java.util.List;

public interface StatisDirectionResponse {

    Integer getDirectionId();
    String getDirectionName();
    List<StatisEduFormResponse> getStatisEduFormResponses();

}
