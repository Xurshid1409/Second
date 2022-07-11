package second.education.api_model.actual_inst_api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Institutions{

	@JsonIgnore
	private Integer total;

	private List<DataItem> data;
}