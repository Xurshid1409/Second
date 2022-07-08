package second.education.api_model.ins_api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstitutionResponse {

	private Data data;
	private Boolean success;
	private String message;
}
