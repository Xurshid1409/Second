package second.education.model.response;

import java.util.List;
public interface StatisDirectionResponseByFutureInst {

    String getFutureInstName();
    Integer getDirectionId();
    String getDirectionName();
    List<StatisEduFormResponse> getStatisEduFormResponses();

}
