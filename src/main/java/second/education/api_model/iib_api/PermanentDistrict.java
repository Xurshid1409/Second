package second.education.api_model.iib_api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermanentDistrict {
	private String code;
	private String name;
	private Region region;
}
