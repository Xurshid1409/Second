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
            IIBResponse iibResponse = iibServiceApi.iibResponse(iibRequest);
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
    public Result validateUser(String phoneNumber, String code) {

        try {
            List<CheckSMSEntity> checkSMSEntity = checkSMSRepository.findByPhoneNumber(phoneNumber);
            CheckSMSEntity check = new CheckSMSEntity();
            for (CheckSMSEntity smsEntity : checkSMSEntity) {
                boolean matches = passwordEncoder.matches(code, smsEntity.getCode());
                if (matches) {
                    check = smsEntity;
                }
            }
            if (check.getPhoneNumber() == null) {
                return new Result("Kiritilgan kod xato", false);
            }
            User user = new User();
            user.setPhoneNumber(phoneNumber);
            user.setPassword(check.getCode());
            user.setRole(getRole());
            User saveUser = userRepository.save(user);
            IIBRequest iibRequest = new IIBRequest();
            iibRequest.setPinfl(check.getPinfl());
            iibRequest.setGiven_date(check.getGivenDate());
            IIBResponse iibResponse = iibServiceApi.iibResponse(iibRequest);
            Data data = iibResponse.getData();
            EnrolleeInfo enrolleeInfo = saveEnrolleeInfo(saveUser, data);
            diplomaService.saveDiplomaByApi(enrolleeInfo.getPinfl(), enrolleeInfo);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(phoneNumber + " " + ResponseMessage.NOT_FOUND, false);
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
            checkSMSEntity.setCode(passwordEncoder.encode(smsApiRequest.getCode().toString()));
            checkSMSRepository.save(checkSMSEntity);
            return new Result("Sms junatildi", true);
        }
        return new Result("Telefon raqam " + phoneNumber + " " + ResponseMessage.NOT_FOUND.getMessage(), false);
    }

    @Transactional
    public Result validateCheckCode(String code, String phoneNumber) {

        List<CheckSMSEntity> byPhoneNumber = checkSMSRepository.findByPhoneNumber(phoneNumber);
        CheckSMSEntity getSmsEntity = new CheckSMSEntity();
        for (CheckSMSEntity checkSMSEntity : byPhoneNumber) {
            boolean matches = passwordEncoder.matches(code, checkSMSEntity.getCode());
            if (matches) {
                getSmsEntity = checkSMSEntity;
                User user = userRepository.findByPhoneNumber(getSmsEntity.getPhoneNumber()).get();
                user.setPassword(getSmsEntity.getCode());
                user.setModifiedDate(LocalDateTime.now());
                userRepository.save(user);
                return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
            }
        }
        if (getSmsEntity.getPhoneNumber() == null) {
            return new Result("Kiritilgan kod xato", false);
        }
        return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
    }

    private EnrolleeInfo saveEnrolleeInfo(User saveUser, Data data) {
        EnrolleeInfo enrolleeInfo = new EnrolleeInfo();
        enrolleeInfo.setFirstname(data.getFirstName());
        enrolleeInfo.setLastname(data.getLastName());
        enrolleeInfo.setMiddleName(data.getMiddleName());
        enrolleeInfo.setPinfl(data.getPinfl());
        enrolleeInfo.setPhoneNumber(saveUser.getPhoneNumber());
        enrolleeInfo.setGender(data.getGender());
        enrolleeInfo.setDateOfBirth(data.getBirthDate());
        enrolleeInfo.setPassportSerialAndNumber(data.getPassportSerial()+data.getPassportNumber());
        enrolleeInfo.setPermanentRegion(data.getPermanentDistrict().getRegion().getName());
        enrolleeInfo.setPermanentDistrict(data.getPermanentDistrict().getName());
        enrolleeInfo.setPermanentAddress(data.getPermanentAddress());
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