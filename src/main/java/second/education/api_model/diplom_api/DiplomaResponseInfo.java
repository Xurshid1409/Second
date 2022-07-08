package second.education.api_model.diplom_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiplomaResponseInfo {

	private Integer id;
	@JsonProperty("institution_id")
	private Integer institutionId;
	@JsonProperty("institution_name")
	private String institutionName;
	@JsonProperty("institution_old_name_id")
	private Integer institutionOldNameId;
	@JsonProperty("institution_old_name")
	private String institutionOldName;
	@JsonProperty("degree_id")
	private Integer degreeId;
	@JsonProperty("degree_name")
	private String degreeName;
	@JsonProperty("edu_form_id")
	private Integer eduFormId;
	@JsonProperty("edu_form_name")
	private String eduFormName;
	@JsonProperty("speciality_id")
	private Integer specialityId;
	@JsonProperty("speciality_name")
	private String specialityName;
	@JsonProperty("edu_finishing_date")
	private String eduFinishingDate;
	@JsonProperty("diploma_serial_id")
	private Integer diplomaSerialId;
	@JsonProperty("diploma_serial")
	private String diplomaSerial;
	@JsonProperty("diploma_number")
	private Integer diplomaNumber;
}