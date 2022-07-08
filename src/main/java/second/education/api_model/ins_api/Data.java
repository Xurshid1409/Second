package second.education.api_model.ins_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Data{

	@JsonProperty("institution_old_names")
	private InstitutionsOldNames institutionsOldNames;
	@JsonProperty("institution_types")
	private InstitutionTypes institutionTypes;
}
