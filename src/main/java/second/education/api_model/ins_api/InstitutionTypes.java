package second.education.api_model.ins_api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InstitutionTypes{

	private Integer total;
	private List<DataItemType> data;
}