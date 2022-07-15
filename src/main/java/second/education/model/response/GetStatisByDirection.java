package second.education.model.response;

import java.util.List;

public interface GetStatisByDirection {

    Integer getDirectionId();
    String getDirectionName();
    List<GetStatisByEduForm> getStatisByEduForm();
}
