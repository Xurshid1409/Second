package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.api_model.iib_api.Data;
import second.education.api_model.iib_api.IIBResponse;
import second.education.api_model.sms_api.SMSAPIRequest;
import second.education.domain.CheckSMSEntity;
import second.education.domain.EnrolleeInfo;
import second.education.domain.User;
import second.education.domain.classificator.Role;
import second.education.model.request.DefaultRole;
import second.education.model.request.IIBRequest;
import second.education.model.request.ValidateCodeRequest;
import second.education.model.response.EnrolleeResponse;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.CheckSMSRepository;
import second.education.repository.EnrolleInfoRepository;
import second.education.repository.RoleRepository;
import second.education.repository.UserRepository;
import second.education.service.api.IIBServiceApi;
import second.education.service.api.SmsServiceApi;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IIBServiceApi iibServiceApi;
    private final SmsServiceApi smsServiceApi;
    private final CheckSMSRepository checkSMSRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EnrolleInfoRepository enrolleInfoRepository;
    private final DiplomaService diplomaService;

    @Transactional
    public Result checkUser(IIBRequest iibRequest) {
        try {

            List<User> users = userRepository.findAllByPhoneNumber(iibRequest.getPhoneNumber());
            if (users.size() > 1) {
                return new Result(iibRequest.getPhoneNumber() + " " + ResponseMessage.ALREADY_EXISTS.getMessage(), true);
            }

            Optional<EnrolleeInfo> byPinfl = enrolleInfoRepository.findByPinfl(iibRequest.getPinfl());
            if (byPinfl.isPresent()) {
                return new Result(iibRequest.getPinfl() + " " + ResponseMessage.ALREADY_EXISTS.getMessage(), true);
            }

            IIBResponse iibResponse = iibServiceApi.iibResponse(iibRequest);
            if (iibResponse == null) {
                return new Result("Shaxsni tasdiqlovchi ma'lumotlar hato kiritilgan, iltimos tekshirib qayta urinib ko'ring yoki IIB ga murojaat qiling", false);
            }

            Data data = iibResponse.getData();
            Optional<User> byPhoneNumber = userRepository.findByPhoneNumberOrPinfl(iibRequest.getPhoneNumber(), data.getPinfl());
            if (byPhoneNumber.isEmpty()) {
                SMSAPIRequest smsApiRequest = new SMSAPIRequest();
                smsApiRequest.setPhone_number(iibRequest.getPhoneNumber());
                smsServiceApi.sendData(smsApiRequest);
                CheckSMSEntity checkSMSEntity = new CheckSMSEntity();
                checkSMSEntity.setPhoneNumber(iibRequest.getPhoneNumber());
                checkSMSEntity.setCode(passwordEncoder.encode(smsApiRequest.getCode().toString()));
                checkSMSEntity.setPinfl(data.getPinfl());
                checkSMSEntity.setGivenDate(data.getPassportGivenDate());
                checkSMSRepository.save(checkSMSEntity);
                return new Result(ResponseMessage.SUCCESSFULLY.getMessage(), true);
            } else if (byPhoneNumber.get().getPhoneNumber().equals(iibRequest.getPhoneNumber())) {
                return new Result(iibRequest.getPhoneNumber() + " " + ResponseMessage.ALREADY_EXISTS.getMessage(), false);
            }
            return new Result(iibRequest.getPinfl() + "  " + ResponseMessage.ALREADY_EXISTS.getMessage(), false);
        } catch (Exception ex) {
            return new Result("Shaxsni tasdiqlovchi ma'lumotlar hato kiritilgan, iltimos tekshirib qayta urinib ko'ring", false);
        }
    }

    @Transactional
    public Result validateUser(ValidateCodeRequest request) {

        try {
            List<CheckSMSEntity> checkSMSEntity = checkSMSRepository.findAllByPhoneNumber(request.getPhoneNumber());
            CheckSMSEntity check = new CheckSMSEntity();
            for (CheckSMSEntity smsEntity : checkSMSEntity) {
                boolean matches = passwordEncoder.matches(request.getCode(), smsEntity.getCode());
                if (matches) {
                    check = smsEntity;
                }
            }
            if (check.getPhoneNumber() == null) {
                return new Result("Kiritilgan kod xato", false);
            }

            List<User> users = userRepository.findAllByPhoneNumber(request.getPhoneNumber());
            if (users.size() > 1) {
                return new Result(request.getPhoneNumber() + " " + ResponseMessage.ALREADY_EXISTS.getMessage(), true);
            }

            Optional<User> byPhoneNumber = userRepository.findByPhoneNumberOrPinfl(check.getPhoneNumber(), check.getPinfl());
            if (byPhoneNumber.isEmpty()) {
                User user = new User();
                user.setPhoneNumber(request.getPhoneNumber());
                user.setPassword(check.getCode());
                user.setRole(getRole());
                User saveUser = userRepository.save(user);
                IIBRequest iibRequest = new IIBRequest();
                iibRequest.setPinfl(check.getPinfl());
                iibRequest.setGiven_date(check.getGivenDate());
                IIBResponse iibResponse = iibServiceApi.iibResponse(iibRequest);
                Data data = iibResponse.getData();
                EnrolleeInfo enrolleeInfo = new EnrolleeInfo();
                enrolleeInfo.setCitizenship(data.getCitizenship().getName());
                enrolleeInfo.setNationality(data.getNationality().getName());
                enrolleeInfo.setFirstname(data.getFirstName());
                enrolleeInfo.setLastname(data.getLastName());
                enrolleeInfo.setMiddleName(data.getMiddleName());
                enrolleeInfo.setPinfl(data.getPinfl());
                enrolleeInfo.setPhoneNumber(saveUser.getPhoneNumber());
                enrolleeInfo.setGender(data.getGender());
                enrolleeInfo.setDateOfBirth(data.getBirthDate());
                enrolleeInfo.setPassportSerialAndNumber(data.getPassportSerial() + data.getPassportNumber());
                if (data.getPermanentDistrict()!=null) {
                    enrolleeInfo.setPermanentDistrict(data.getPermanentDistrict().getName());
                    enrolleeInfo.setPermanentRegion(data.getPermanentDistrict().getRegion().getName());
                    enrolleeInfo.setPermanentAddress(data.getPermanentAddress());
                }
                enrolleeInfo.setPassportGivenDate(data.getPassportGivenDate());
//                enrolleeInfo.setPhoto(data.getPhoto());
                enrolleeInfo.setUser(saveUser);
                enrolleInfoRepository.save(enrolleeInfo);
                return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
            }
            return new Result(ResponseMessage.ALREADY_EXISTS.getMessage(), false);
        } catch (Exception ex) {
            return new Result(request.getPhoneNumber() + " " + ResponseMessage.NOT_FOUND.getMessage(), false);
        }
    }

    @Transactional
    public Result updateCheckCode(String phoneNumber) {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            CheckSMSEntity checkSMSEntity = new CheckSMSEntity();
            SMSAPIRequest smsApiRequest = new SMSAPIRequest();
            smsApiRequest.setPhone_number(phoneNumber);
            smsServiceApi.sendData(smsApiRequest);
            checkSMSEntity.setPhoneNumber(phoneNumber);
            checkSMSEntity.setCode(passwordEncoder.encode(smsApiRequest.getCode().toString()));
            checkSMSRepository.save(checkSMSEntity);
            return new Result("Sms junatildi", true);
        }
        return new Result("Telefon raqam " + phoneNumber + " " + ResponseMessage.NOT_FOUND.getMessage(), false);
    }

    public Result validateCheckCode(ValidateCodeRequest request) {

        List<CheckSMSEntity> byPhoneNumber = checkSMSRepository.findAllByPhoneNumber(request.getPhoneNumber());
        CheckSMSEntity getSmsEntity = new CheckSMSEntity();
        for (CheckSMSEntity checkSMSEntity : byPhoneNumber) {
            boolean matches = passwordEncoder.matches(request.getCode(), checkSMSEntity.getCode());
            if (matches) {
                getSmsEntity = checkSMSEntity;
            }
        }
        if (getSmsEntity.getPhoneNumber() == null) {
            return new Result("Kiritilgan kod xato", false);
        }
        User user = userRepository.findByPhoneNumber(getSmsEntity.getPhoneNumber()).get();
        user.setPassword(getSmsEntity.getCode());
        user.setModifiedDate(LocalDateTime.now());
        userRepository.save(user);
        return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
    }

    @Transactional
    public EnrolleeInfo saveEnrolleeInfo(User saveUser, Data data) {
        EnrolleeInfo enrolleeInfo = new EnrolleeInfo();
        enrolleeInfo.setCitizenship(data.getCitizenship().getName());
        enrolleeInfo.setNationality(data.getNationality().getName());
        enrolleeInfo.setFirstname(data.getFirstName());
        enrolleeInfo.setLastname(data.getLastName());
        enrolleeInfo.setMiddleName(data.getMiddleName());
        enrolleeInfo.setPinfl(data.getPinfl());
        enrolleeInfo.setPhoneNumber(saveUser.getPhoneNumber());
        enrolleeInfo.setGender(data.getGender());
        enrolleeInfo.setDateOfBirth(data.getBirthDate());
        enrolleeInfo.setPassportSerialAndNumber(data.getPassportSerial() + data.getPassportNumber());
        if (data.getPermanentDistrict().getRegion().getName() != null) {
            enrolleeInfo.setPermanentRegion(data.getPermanentDistrict().getRegion().getName());
        }
        if (data.getPermanentDistrict().getName() != null) {
            enrolleeInfo.setPermanentDistrict(data.getPermanentDistrict().getName());
        }
        if (data.getPermanentAddress() != null) {
            enrolleeInfo.setPermanentAddress(data.getPermanentAddress());
        }
        enrolleeInfo.setPassportGivenDate(data.getPassportGivenDate());
        enrolleeInfo.setUser(saveUser);
        return enrolleInfoRepository.save(enrolleeInfo);
    }

    private Role getRole() {
        Optional<Role> optionalRole = roleRepository.findByName(DefaultRole.ROLE_ABITURIYENT.getMessage());
        if (optionalRole.isEmpty()) {
            Role role = new Role();
            role.setName(DefaultRole.ROLE_ABITURIYENT.name());
            roleRepository.save(role);
            return role;
        }
        return optionalRole.get();
    }
}
