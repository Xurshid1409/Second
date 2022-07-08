package second.education.api_model.ins_api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InstitutionsOldNames {

	private Integer total;
	private List<DataItem> data;
}