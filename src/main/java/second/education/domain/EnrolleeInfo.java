package second.education.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrolleeInfo extends AbstractData<Integer> {


    private String firstname;
    private String middleName;
    private String lastname;
    private String dateOfBirth;
    private String gender;
    private String passportSerial;
    private String passportNumber;
    private String passportGivenDate;
    private String pinfl;
    private String phoneNumber;
    private String nationality;
    private String permanentRegion;
    private String permanentDistrict;
    private String permanentAddress;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private User user;
}
