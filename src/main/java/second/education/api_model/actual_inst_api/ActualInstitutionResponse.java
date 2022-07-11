package second.education.api_model.actual_inst_api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActualInstitutionResponse {

	private Data data;
	@JsonIgnore
	private Boolean success;
	@JsonIgnore
	private String message;
}
