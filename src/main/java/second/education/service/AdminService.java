package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.api_model.iib_api.Data;
import second.education.api_model.iib_api.IIBResponse;
import second.education.domain.*;
import second.education.domain.classificator.FutureInstitution;
import second.education.domain.classificator.Role;
import second.education.domain.classificator.University;
import second.education.model.request.DefaultRole;
import second.education.model.request.IIBRequest;
import second.education.model.request.UpdateAppStatus;
import second.education.model.request.UserRequest;
import second.education.model.response.*;
import second.education.repository.*;
import second.education.service.api.IIBServiceApi;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final FutureInstitutionRepository futureInstitutionRepository;
    private final AdminEntityRepository adminEntityRepository;
    private final UniversityRepository universityRepository;
    private final ApplicationRepository applicationRepository;
    private final DiplomaRepository diplomaRepository;
    private final IIBServiceApi iibServiceApi;
    private final StoryMessageRepository storyMessageRepository;
    private final DocumentRepository documentRepository;

    @Transactional
    public Result createInstitutionAdmin(UserRequest request) {

        try {
            Optional<User> byPhoneNumber = userRepository.findByPhoneNumber(request.getPinfl());
            if (byPhoneNumber.isEmpty()) {
                User user = new User();
                user.setPhoneNumber(request.getPinfl());
                user.setPassword(passwordEncoder.encode(request.getPinfl()));
                Role role = roleRepository.findByName(DefaultRole.ROLE_UADMIN.getMessage()).get();
                user.setRole(role);
                User saveUser = userRepository.save(user);
                AdminEntity adminEntity = new AdminEntity();
                if (request.getFutureInstId() == null) {
                    adminEntity.setFutureInstitution(null);
                } else {
                    FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
                    adminEntity.setFutureInstitution(futureInstitution);
                }
                List<University> universities = universityRepository.findAllByInstitutionId(request.getInstitutionId());
                adminEntity.setUniversities(universities);
                adminEntity.setUser(saveUser);
                adminEntityRepository.save(adminEntity);
                return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
            }
            return new Result(ResponseMessage.ALREADY_EXISTS.getMessage(), false);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result updateInstitutionAdmin(Integer adminEntityId, UserRequest request) {

        try {
            AdminEntity adminEntity = adminEntityRepository.findById(adminEntityId).get();
            Optional<User> byPhoneNumber = userRepository.findByPhoneNumber(request.getPinfl());
            if (byPhoneNumber.isPresent()) {
                if (adminEntity.getUser().getId().equals(byPhoneNumber.get().getId())) {
                    adminEntity.getUser().setPhoneNumber(request.getPinfl());
                    adminEntity.getUser().setPassword(passwordEncoder.encode(request.getPinfl()));
                    User save = userRepository.save(byPhoneNumber.get());
                    if (request.getFutureInstId() == null) {
                        adminEntity.setFutureInstitution(null);
                    } else {
                        FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
                        adminEntity.setFutureInstitution(futureInstitution);
                    }
                    adminEntity.getUniversities().clear();
                    List<University> universities = universityRepository.findAllByInstitutionId(request.getInstitutionId());
                    adminEntity.setUniversities(universities);
                    adminEntity.setUser(save);
                    adminEntityRepository.save(adminEntity);
                    return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
                }
                return new Result("bu pinfl oldin qo'shilgan", false);
            }
            adminEntity.getUser().setPhoneNumber(request.getPinfl());
            adminEntity.getUser().setPassword(passwordEncoder.encode(request.getPinfl()));
            User save = userRepository.save(adminEntity.getUser());
            FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
            adminEntity.setFutureInstitution(futureInstitution);
            adminEntity.getUniversities().clear();
            List<University> universities = universityRepository.findAllByInstitutionId(request.getInstitutionId());
            adminEntity.setUniversities(universities);
            adminEntity.setUser(save);
            adminEntity.setModifiedDate(LocalDateTime.now());
            adminEntityRepository.save(adminEntity);
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

    @Transactional(readOnly = true)
    public Page<UAdminResponse> getUAdmins(int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<AdminEntity> allAdmins = adminEntityRepository.getAllAdmins(pageable);
        List<UAdminResponse> uAdminResponses = new ArrayList<>();
        allAdmins.forEach(adminEntity -> {
            UAdminResponse uAdminResponse = new UAdminResponse();
            uAdminResponse.setId(adminEntity.getId());
            uAdminResponse.setPinfl(adminEntity.getUser().getPhoneNumber());
            if (adminEntity.getFutureInstitution() != null) {
                uAdminResponse.setFutureInstId(adminEntity.getFutureInstitution().getId());
                uAdminResponse.setFutureInstName(adminEntity.getFutureInstitution().getName());
            } else {
                uAdminResponse.setFutureInstId(0);
                uAdminResponse.setFutureInstName(null);
            }
            uAdminResponse.setUniversityResponses(adminEntity.getUniversities().stream().map(UniversityResponse::new).toList());
            uAdminResponses.add(uAdminResponse);
        });
        return new PageImpl<>(uAdminResponses, pageable, allAdmins.getTotalElements());
    }

    @Transactional(readOnly = true)
    public UAdminResponse getUAdminById(int adminEntityId) {

        AdminEntity adminEntity = adminEntityRepository.getAdminById(adminEntityId).get();
        UAdminResponse uAdminResponse = new UAdminResponse();
        uAdminResponse.setId(adminEntity.getId());
        uAdminResponse.setPinfl(adminEntity.getUser().getPhoneNumber());
        if (adminEntity.getFutureInstitution() != null) {
            uAdminResponse.setFutureInstId(adminEntity.getFutureInstitution().getId());
            uAdminResponse.setFutureInstName(adminEntity.getFutureInstitution().getName());
        } else {
            uAdminResponse.setFutureInstId(0);
            uAdminResponse.setFutureInstName(null);
        }
        uAdminResponse.setUniversityResponses(adminEntity.getUniversities().stream().map(UniversityResponse::new).toList());
        return uAdminResponse;
    }

    @Transactional(readOnly = true)
    public Page<UAdminResponse> searchUAdmin(String text, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<AdminEntity> allAdmins = adminEntityRepository.searchUAdmin(text, pageable);
        List<UAdminResponse> uAdminResponses = new ArrayList<>();
        allAdmins.forEach(adminEntity -> {
            UAdminResponse uAdminResponse = new UAdminResponse();
            uAdminResponse.setId(adminEntity.getId());
            uAdminResponse.setPinfl(adminEntity.getUser().getPhoneNumber());
            if (adminEntity.getFutureInstitution() != null) {
                uAdminResponse.setFutureInstId(adminEntity.getFutureInstitution().getId());
                uAdminResponse.setFutureInstName(adminEntity.getFutureInstitution().getName());
            } else {
                uAdminResponse.setFutureInstId(0);
                uAdminResponse.setFutureInstName(null);
            }
            uAdminResponse.setUniversityResponses(adminEntity.getUniversities().stream().map(UniversityResponse::new).toList());
            uAdminResponses.add(uAdminResponse);
        });
        return new PageImpl<>(uAdminResponses, pageable, allAdmins.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<GetDiplomasToExcel> getDiplomasToAdmin(String status, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            return applicationRepository.getAllDiplomaToAdmin(aBoolean, pageable);
        }
        return applicationRepository.getAllDiplomaNullToAdmin(pageable);
    }

    @Transactional(readOnly = true)
    public Result getDiplomaById(Integer diplomaId) {
        Optional<Application> application = applicationRepository.getAppAndDiplomaByAdmin(diplomaId);
        if (application.isEmpty()) {
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        }
        IIBRequest iibRequest = new IIBRequest();
        iibRequest.setPinfl(application.get().getEnrolleeInfo().getPinfl());
        iibRequest.setGiven_date(application.get().getEnrolleeInfo().getPassportGivenDate());
        IIBResponse iibResponse = iibServiceApi.iibResponse(iibRequest);
        Data data = iibResponse.getData();
        ImageResponse imageResponse = new ImageResponse();
        if (!data.getPhoto().isEmpty()) {
            imageResponse.setImage(data.getPhoto());
        }
        EnrolleeResponse enrolleeResponse = new EnrolleeResponse(application.get().getEnrolleeInfo(), imageResponse);
        Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.get().getEnrolleeInfo().getId()).get();

        FileResponse fileResponse = getFileResponse(diploma.getId());
        DiplomResponseAdmin diplomResponseAdmin = new DiplomResponseAdmin(diploma, fileResponse);
        diplomResponseAdmin.setEnrolleeResponse(enrolleeResponse);
        diplomResponseAdmin.setDiplomaStatus(String.valueOf(application.get().getDiplomaStatus()));
        return new Result("diploma", true, diplomResponseAdmin);
    }

    @Transactional(readOnly = true)
    public Page<GetDiplomasToExcel> getForeignDiplomasToAdmin(String status, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            return applicationRepository.getAllForeignDiplomaToAdmin(aBoolean, pageable);
        }
        return applicationRepository.getAllForeignDiplomaNullToAdmin(pageable);
    }

    @Transactional(readOnly = true)
    public Result getForeignDiplomaById(Integer diplomaId) {
        Optional<Application> application = applicationRepository.getAppAndForeignDiplomaByIdByAdmin(diplomaId);
        if (application.isEmpty()) {
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        }
        IIBRequest iibRequest = new IIBRequest();
        iibRequest.setPinfl(application.get().getEnrolleeInfo().getPinfl());
        iibRequest.setGiven_date(application.get().getEnrolleeInfo().getPassportGivenDate());
        IIBResponse iibResponse = iibServiceApi.iibResponse(iibRequest);
        Data data = iibResponse.getData();
        ImageResponse imageResponse = new ImageResponse();
        if (!data.getPhoto().isEmpty()) {
            imageResponse.setImage(data.getPhoto());
        }
        EnrolleeResponse enrolleeResponse = new EnrolleeResponse(application.get().getEnrolleeInfo(), imageResponse);
        Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.get().getEnrolleeInfo().getId()).get();

        FileResponse fileResponse = getFileResponse(diploma.getId());
        DiplomResponseAdmin diplomResponseAdmin = new DiplomResponseAdmin(diploma, fileResponse);
        diplomResponseAdmin.setEnrolleeResponse(enrolleeResponse);
        diplomResponseAdmin.setDiplomaStatus(String.valueOf(application.get().getDiplomaStatus()));

        return new Result("diploma", true, diplomResponseAdmin);
    }

    @Transactional(readOnly = true)
    public Page<GetAppToExcel> getAllAppToAdmin(String appStatus, String diplomaStatus, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        if (diplomaStatus.equals("true") || diplomaStatus.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(diplomaStatus);
            return applicationRepository.getAllAppDiplomaTrueToAdmin(aBoolean, pageable);
        } else if (diplomaStatus.equals("null")) {
            return applicationRepository.getAllAppByDiplomaNullToAdmin(pageable);
        } else {
            return applicationRepository.getAllAppToAdmin(appStatus, pageable);
        }
    }

    @Transactional(readOnly = true)
    public Result getAppById(Integer AppId) {
        Optional<Application> application = applicationRepository.getAppOneByAdmin(AppId);
        if (application.isEmpty()) {
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        }
        AppResponse appResponse = new AppResponse(application.get());
        IIBRequest iibRequest = new IIBRequest();
        iibRequest.setPinfl(application.get().getEnrolleeInfo().getPinfl());
        iibRequest.setGiven_date(application.get().getEnrolleeInfo().getPassportGivenDate());
        IIBResponse iibResponse = iibServiceApi.iibResponse(iibRequest);
        Data data = iibResponse.getData();
        ImageResponse imageResponse = new ImageResponse();
        if (!data.getPhoto().isEmpty()) {
            imageResponse.setImage(data.getPhoto());
        }
        StoryMessageResponse response = new StoryMessageResponse();
        List<StoryM> app = new ArrayList<>();
        List<StoryM> diplomaResponse = new ArrayList<>();
        List<StoryMessage> messages = storyMessageRepository.getAllStoryByAppId(application.get().getId());
        if (messages.size() > 0) {
            messages.forEach(storyMessage -> {
                if (storyMessage.getStatus().equals("Ariza qabul qilindi") || storyMessage.getStatus().equals("Ariza rad etildi")) {
                    StoryM storyMessageResponse = new StoryM();
                    storyMessageResponse.setMessage(storyMessage.getMessage());
                    storyMessageResponse.setStatus(storyMessage.getStatus());
                    storyMessageResponse.setTime(storyMessage.getCreatedDate());
                    storyMessageResponse.setCreateBy(storyMessage.getFirstname() + " " + storyMessage.getLastname());
                    app.add(storyMessageResponse);
                } else if (storyMessage.getStatus().equals("true") || storyMessage.getStatus().equals("false")) {
                    StoryM storyMessageResponse = new StoryM();
                    storyMessageResponse.setMessage(storyMessage.getMessage());
                    storyMessageResponse.setStatus(storyMessage.getStatus());
                    storyMessageResponse.setTime(storyMessage.getCreatedDate());
                    storyMessageResponse.setCreateBy(storyMessage.getFirstname() + " " + storyMessage.getLastname());
                    diplomaResponse.add(storyMessageResponse);
                }
            });
            response.setApp(app);
            response.setDiploma(diplomaResponse);
            appResponse.setStoryMessageResponse(response);
        }
        appResponse.setEnrolleeResponse(new EnrolleeResponse(application.get().getEnrolleeInfo(), imageResponse));
        Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.get().getEnrolleeInfo().getId()).get();
        FileResponse fileResponse = getFileResponse(diploma.getId());
        appResponse.setDiplomaResponse(new DiplomaResponse(diploma, fileResponse));
        return new Result("one app", true, appResponse);
    }

    public FileResponse getFileResponse(Integer diplomaId) {
        List<Document> documents = documentRepository.findAllByDiplomaId(diplomaId);
        FileResponse fileResponse = new FileResponse();
        documents.forEach(document -> {
            if (document.getFileName() != null && document.getFileName().startsWith("Diplom")) {
                DiplomaCopyResponse diplomaCopyResponse = new DiplomaCopyResponse();
                diplomaCopyResponse.setId(document.getId());
                diplomaCopyResponse.setUrl(document.getUrl());
                fileResponse.setDiplomaCopyResponse(diplomaCopyResponse);
            }
            if (document.getFileName() != null && document.getFileName().startsWith("Ilova")) {
                DiplomaIlovaResponse diplomaIlovaResponse = new DiplomaIlovaResponse();
                diplomaIlovaResponse.setId(document.getId());
                diplomaIlovaResponse.setUrl(document.getUrl());
                fileResponse.setDiplomaIlovaResponse(diplomaIlovaResponse);
            }
        });
        return fileResponse;
    }

    @Transactional(readOnly = true)
    public List<GetDiplomasToExcel> exportDiplomasToAdmin(String status) {
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            return applicationRepository.exportAllDiplomaToAdmin(aBoolean);
        }
        return applicationRepository.exportAllDiplomaNullToAdmin();
    }

    @Transactional(readOnly = true)
    public List<GetDiplomasToExcel> exportForeignDiplomasToAdmin(String status) {
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            return applicationRepository.exportAllForeignDiplomaToAdmin(aBoolean);
        }
        return applicationRepository.exportAllForeignDiplomaNullToAdmin();
    }

    @Transactional(readOnly = true)
    public List<GetAppToExcel> exportAllAppToAdmin(String appStatus, String diplomaStatus) {
        if (diplomaStatus.equals("true")) {
            return applicationRepository.exportAllAppDiplomaTrueToAdmin();
        } else if (diplomaStatus.equals("null")) {
            return applicationRepository.exportAllAppByDiplomaNullToAdmin();
        } else {
            return applicationRepository.exportAllAppToAdmin(appStatus);
        }
    }

    @Transactional
    public Result updateStatusAppToAdmin(Principal principal, UpdateAppStatus updateAppStatus, Integer appId) {
        try {
            Optional<Application> application = applicationRepository.findById(appId);
            if (application.isPresent()) {
                String status = String.valueOf(application.get().getDiplomaStatus());
                switch (status) {
                    case "true":
                        application.get().setStatus(updateAppStatus.getAppStatus());
                        application.get().setMessage(updateAppStatus.getAppMessage());
                        applicationRepository.save(application.get());
                        StoryMessage storyMessage = new StoryMessage();
                        storyMessage.setMessage(updateAppStatus.getAppMessage());
                        storyMessage.setStatus(updateAppStatus.getAppStatus());
                        storyMessage.setFirstname("Super admin tomonidan");
                        storyMessage.setPinfl(principal.getName());
                        storyMessage.setApplication(application.get());
                        storyMessageRepository.save(storyMessage);
                        return new Result("Muvaffaqiyatli tasdiqlandi", true);
                    case "false":
                        return new Result("bu arizaning diplomi rad etilgan", false);
                    case "null":
                        return new Result("ariza diplomi hali tasdiqlanmagan ", false);
                }
            }
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        } catch (Exception e) {
            return new Result("Tasdiqlashda xatolik", false);
        }
    }


    @Transactional(readOnly = true)
    public Page<GetAppToExcel> searchAllAppByStatus(Principal principal , String diplomaStatus, String appStatus, String search, int page, int size) {
        String s = search.toUpperCase();
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        if (diplomaStatus.equals("true") || diplomaStatus.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(diplomaStatus);
           return applicationRepository.
                    searchAppByFirstnameAndLastnameByDiplomastatusByAdmin(appStatus, aBoolean, s, pageable);
        } else {
        return applicationRepository.
                    searchAppByFirstnameAndLastnameByDiplomastatusIsNullByAdmin(appStatus, s, pageable);
        }
    }

    @Transactional(readOnly = true)
    public Page<GetAppToExcel> searchAllAppByAdmin(Principal principal, String status, String search, int page, int size) {
        String s = search.toUpperCase();
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
    return applicationRepository.searchAppByFirstnameAndLastnameByAdmin(status, s, pageable);
       /* allApp.forEach(application -> {
            AppResponse appResponse = new AppResponse(application);

            appResponse.setEnrolleeResponse(new EnrolleeResponse(application.getEnrolleeInfo()));
            Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
            FileResponse fileResponse = getFileResponse(diploma.getId());
            appResponse.setDiplomaResponse(new DiplomaResponse(diploma, fileResponse));
            responses.add(appResponse);
        });*/
    }
}
