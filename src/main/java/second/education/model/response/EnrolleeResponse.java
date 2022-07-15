package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.EnrolleeInfo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrolleeResponse {

    private String citizenship;
    private String firstname;
    private String middleName;
    private String lastname;
    private String dateOfBirth;
    private String gender;
    private String passportSerialAndNumber;
    private String passportGivenDate;
    private String pinfl;
    private String phoneNumber;
    private String nationality;
    private String permanentRegion;
    private String permanentDistrict;
    private String permanentAddress;
//    private ApplicationResponse applicationResponse;


    public EnrolleeResponse(EnrolleeInfo enrolleeInfo) {
        this.citizenship = enrolleeInfo.getCitizenship();
        this.nationality = enrolleeInfo.getNationality();
        this.passportSerialAndNumber = enrolleeInfo.getPassportSerialAndNumber();
        this.pinfl = enrolleeInfo.getPinfl();
        this.firstname = enrolleeInfo.getFirstname();
        this.lastname = enrolleeInfo.getLastname();
        this.middleName = enrolleeInfo.getMiddleName();
        this.dateOfBirth = enrolleeInfo.getDateOfBirth();
        this.permanentRegion = enrolleeInfo.getPermanentRegion();
        this.permanentDistrict = enrolleeInfo.getPermanentDistrict();
        this.permanentAddress = enrolleeInfo.getPermanentAddress();
        this.gender = enrolleeInfo.getGender();
        this.phoneNumber = enrolleeInfo.getUser().getPhoneNumber();
    }

}
