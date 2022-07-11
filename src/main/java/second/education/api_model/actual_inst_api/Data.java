package second.education.api_model.actual_inst_api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Data{

	private Institutions institutions;
	@JsonIgnore
	private InstitutionTypes institutionTypes;
}
