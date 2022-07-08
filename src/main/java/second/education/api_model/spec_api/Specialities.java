package second.education.api_model.spec_api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Specialities{

    private Integer total;
    private List<DataItem> data;
}