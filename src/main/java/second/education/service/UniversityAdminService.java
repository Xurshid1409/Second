package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.api_model.iib_api.Data;
import second.education.api_model.iib_api.IIBResponse;
import second.education.domain.*;
import second.education.domain.classificator.University;
import second.education.model.request.IIBRequest;
import second.education.model.request.UpdateAppStatus;
import second.education.model.request.UpdateDiplomaStatus;
import second.education.model.response.*;
import second.education.repository.*;
import second.education.service.api.IIBServiceApi;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UniversityAdminService {

    private final DiplomaRepository diplomaRepository;
    private final IIBServiceApi iibServiceApi;
    private final StoryMessageRepository storyMessageRepository;
    private final ApplicationRepository applicationRepository;
    private final DocumentRepository documentRepository;
    private final AdminEntityRepository adminEntityRepository;

    @Transactional(readOnly = true)
    public Page<DiplomResponseAdmin> getDiplomas(Principal principal, String status, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        List<University> universities = adminEntity.getUniversities();
        Integer institutionId = universities.stream().map(University::getInstitutionId).findFirst().get();
        List<DiplomResponseAdmin> diplomResponseAdmins = new ArrayList<>();
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            Page<Application> allDiplomebyUAdmin = applicationRepository.getAppDiplomaByEnrollId(institutionId, aBoolean, pageable);
            allDiplomebyUAdmin.forEach(application -> {
                EnrolleeResponse enrolleeResponse = new EnrolleeResponse(application.getEnrolleeInfo());
                Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                //   FileResponse fileResponse = getFileResponse(diploma.getId());
                DiplomResponseAdmin diplomResponseAdmin = new DiplomResponseAdmin(diploma);
                diplomResponseAdmin.setEnrolleeResponse(enrolleeResponse);
                diplomResponseAdmin.setDiplomaStatus(String.valueOf(application.getDiplomaStatus()));

                diplomResponseAdmins.add(diplomResponseAdmin);
            });
            return new PageImpl<>(diplomResponseAdmins, pageable, allDiplomebyUAdmin.getTotalElements());

        } else {
            Page<Application> allDiplomebyUAdmin = applicationRepository.getAppDiplomaByEnrollAppDiplomStatusNull(institutionId, pageable);
            allDiplomebyUAdmin.forEach(application -> {
                EnrolleeResponse enrolleeResponse = new EnrolleeResponse(application.getEnrolleeInfo());
                Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                FileResponse fileResponse = getFileResponse(diploma.getId());
                DiplomResponseAdmin diplomResponseAdmin = new DiplomResponseAdmin(diploma, fileResponse);
                diplomResponseAdmin.setEnrolleeResponse(enrolleeResponse);
                diplomResponseAdmin.setDiplomaStatus(String.valueOf(application.getDiplomaStatus()));
                diplomResponseAdmins.add(diplomResponseAdmin);
            });
            return new PageImpl<>(diplomResponseAdmins, pageable, allDiplomebyUAdmin.getTotalElements());
        }

    }


    //horijiy diplomlarni obshiysi
    @Transactional(readOnly = true)
    public Page<DiplomResponseAdmin> getForeignDiplomas(Principal principal, String status, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        List<DiplomResponseAdmin> diplomResponseAdmins = new ArrayList<>();
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        if (adminEntity.getFutureInstitution().getId() != null) {
            if (status.equals("true") || status.equals("false")) {
                Boolean aBoolean = Boolean.valueOf(status);
                Page<Application> allDiplomebyUAdmin = applicationRepository.getAppDipForeign(adminEntity.getFutureInstitution().getId(), aBoolean, pageable);
                allDiplomebyUAdmin.forEach(application -> {
                    EnrolleeResponse enrolleeResponse = new EnrolleeResponse(application.getEnrolleeInfo());
                    Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                    FileResponse fileResponse = getFileResponse(diploma.getId());
                    DiplomResponseAdmin diplomResponseAdmin = new DiplomResponseAdmin(diploma, fileResponse);
                    diplomResponseAdmin.setEnrolleeResponse(enrolleeResponse);
                    diplomResponseAdmin.setDiplomaStatus(String.valueOf(application.getDiplomaStatus()));
                    diplomResponseAdmins.add(diplomResponseAdmin);
                });
                return new PageImpl<>(diplomResponseAdmins, pageable, allDiplomebyUAdmin.getTotalElements());
            } else {
                Page<Application> allDiplomebyUAdmin = applicationRepository.getAppForeignDipStatusNull(adminEntity.getFutureInstitution().getId(), pageable);
                allDiplomebyUAdmin.forEach(application -> {

                    EnrolleeResponse enrolleeResponse = new EnrolleeResponse(application.getEnrolleeInfo());
                    Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                    FileResponse fileResponse = getFileResponse(diploma.getId());
                    DiplomResponseAdmin diplomResponseAdmin = new DiplomResponseAdmin(diploma, fileResponse);
                    diplomResponseAdmin.setEnrolleeResponse(enrolleeResponse);
                    diplomResponseAdmin.setDiplomaStatus(String.valueOf(application.getDiplomaStatus()));

                    diplomResponseAdmins.add(diplomResponseAdmin);
                });
                return new PageImpl<>(diplomResponseAdmins, pageable, allDiplomebyUAdmin.getTotalElements());
            }
        }
        return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }

    //horijiy diplomning bir donasi id orqali
    @Transactional(readOnly = true)
    public Result getForeignDiplomaById(Integer diplomaId, Principal principal) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Optional<Application> application = applicationRepository.getAppAndForeignDiplomaById(adminEntity.getFutureInstitution().getId(), diplomaId);
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
    public Result getDiplomaById(Integer diplomaId, Principal principal) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
        Optional<Application> application = applicationRepository.getAppAndDiplomaById(institutionId, diplomaId);
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
    public Page<AppResponse> getAllAppByUAdmin(Principal principal, String status, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        List<AppResponse> responses = new ArrayList<>();
        if (adminEntity.getFutureInstitution().getId() != null) {
            Page<Application> allApp = applicationRepository.getAllApp(adminEntity.getFutureInstitution().getId(), status, pageable);
            allApp.forEach(application -> {
                AppResponse appResponse = new AppResponse(application);
                appResponse.setEnrolleeResponse(new EnrolleeResponse(application.getEnrolleeInfo()));
                Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                FileResponse fileResponse = getFileResponse(diploma.getId());
                appResponse.setDiplomaResponse(new DiplomaResponse(diploma, fileResponse));
                responses.add(appResponse);
            });
            return new PageImpl<>(responses, pageable, allApp.getTotalElements());
        }
        return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }

    @Transactional(readOnly = true)
    public Page<AppResponse> getAllAppDplomaStatusByUAdmin(Principal principal, String diplomaStatus, String appStatus, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        List<AppResponse> responses = new ArrayList<>();
        if (adminEntity.getFutureInstitution().getId() != null) {
            if (diplomaStatus.equals("true") || diplomaStatus.equals("false")) {
                Boolean aBoolean = Boolean.valueOf(diplomaStatus);
                Page<Application> allApp = applicationRepository.getAllAppByDiplomaStatusAndAppstatus(adminEntity.getFutureInstitution().getId(), aBoolean, appStatus, pageable);
                allApp.forEach(application -> {
                    AppResponse appResponse = new AppResponse(application);
                    appResponse.setEnrolleeResponse(new EnrolleeResponse(application.getEnrolleeInfo()));
                    Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                    FileResponse fileResponse = getFileResponse(diploma.getId());
                    appResponse.setDiplomaResponse(new DiplomaResponse(diploma, fileResponse));
                    responses.add(appResponse);
                });
                return new PageImpl<>(responses, pageable, allApp.getTotalElements());
            } else {
                List<AppResponse> responsese = new ArrayList<>();
                Page<Application> allApp = applicationRepository.getAllAppByDiplomaStatusIsNull(adminEntity.getFutureInstitution().getId(), appStatus, pageable);
                allApp.forEach(application -> {
                    AppResponse appResponse = new AppResponse(application);
                    appResponse.setEnrolleeResponse(new EnrolleeResponse(application.getEnrolleeInfo()));
                    Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                    FileResponse fileResponse = getFileResponse(diploma.getId());
                    appResponse.setDiplomaResponse(new DiplomaResponse(diploma, fileResponse));
                    responsese.add(appResponse);
                });
                return new PageImpl<>(responsese, pageable, allApp.getTotalElements());
            }
        }
        return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }

    @Transactional(readOnly = true)
    public Result getAppById(Integer AppId, Principal principal) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Optional<Application> application = applicationRepository.getAppOne(adminEntity.getFutureInstitution().getId(), AppId);
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
    public UAdminInfoResponse getUAdmin(Principal principal) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        return new UAdminInfoResponse(adminEntity);
    }

    @Transactional
    public Result updateStatusDiploma(Principal principal, UpdateDiplomaStatus updateDiplomaStatus, Integer diplomaId) {
        try {
            AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
            Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
            Optional<Application> application = applicationRepository.getAppAndDiplomaById(institutionId, diplomaId);
            if (application.isPresent()) {
                application.get().setDiplomaStatus(updateDiplomaStatus.getDiplomStatus());
                application.get().setDiplomaMessage(updateDiplomaStatus.getDiplomMessage());
                Application save = applicationRepository.save(application.get());
                StoryMessage storyMessage = new StoryMessage();
                storyMessage.setMessage(updateDiplomaStatus.getDiplomMessage());
                String status1 = String.valueOf(updateDiplomaStatus.getDiplomStatus());
                storyMessage.setStatus(status1);
                storyMessage.setFirstname(adminEntity.getFistName());
                storyMessage.setLastname(adminEntity.getLastname());
                storyMessage.setPinfl(principal.getName());
                storyMessage.setApplication(save);
                storyMessageRepository.save(storyMessage);
                return new Result("Muvaffaqiyatli o'zgartirildi", true);
            }
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        } catch (Exception e) {
            return new Result("O'zgartirishda xatolik", false);
        }
    }

    @Transactional
    public Result updateDiplomStatusbyApp(Principal principal, UpdateDiplomaStatus updateDiplomaStatus, Integer diplomaId) {
        try {
            AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
            Optional<Application> app = applicationRepository.getAppByUadmin(adminEntity.getFutureInstitution().getId(), diplomaId);
            if (app.isPresent()) {
                app.get().setDiplomaStatus(updateDiplomaStatus.getDiplomStatus());
                app.get().setDiplomaMessage(updateDiplomaStatus.getDiplomMessage());
                Application save = applicationRepository.save(app.get());
                StoryMessage storyMessage = new StoryMessage();
                storyMessage.setMessage(updateDiplomaStatus.getDiplomMessage());
                String status1 = String.valueOf(updateDiplomaStatus.getDiplomStatus());
                storyMessage.setStatus(status1);
                storyMessage.setFirstname(adminEntity.getFistName());
                storyMessage.setLastname(adminEntity.getLastname());
                storyMessage.setPinfl(principal.getName());
                storyMessage.setApplication(save);
                storyMessageRepository.save(storyMessage);
                return new Result("Muvaffaqiyatli o'zgartirildi", true);
            }
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        } catch (Exception e) {
            return new Result("O'zgartirishda xatolik", false);
        }
    }

    @Transactional
    public Result updateStatusForeignDiploma(Principal principal, UpdateDiplomaStatus updateDiplomaStatus, Integer diplomaId) {
        try {
            AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
            Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
            Optional<Application> application = applicationRepository.getAppAndForeignDiplomaById(adminEntity.getFutureInstitution().getId(), diplomaId);
            if (application.isPresent()) {
                String status = String.valueOf(application.get().getDiplomaStatus());
                application.get().setDiplomaStatus(updateDiplomaStatus.getDiplomStatus());
                application.get().setDiplomaMessage(updateDiplomaStatus.getDiplomMessage());
                Application save = applicationRepository.save(application.get());
                StoryMessage storyMessage = new StoryMessage();
                storyMessage.setMessage(updateDiplomaStatus.getDiplomMessage());
                String status1 = String.valueOf(updateDiplomaStatus.getDiplomStatus());
                storyMessage.setStatus(status1);
                storyMessage.setFirstname(adminEntity.getFistName());
                storyMessage.setLastname(adminEntity.getLastname());
                storyMessage.setPinfl(principal.getName());
                storyMessage.setApplication(save);
                storyMessageRepository.save(storyMessage);
                return new Result("Muvaffaqiyatli o'zgartirildi", true);
            }
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        } catch (Exception e) {
            return new Result("O'zgartirishda xatolik", false);
        }
    }

    @Transactional
    public Result updateStatusApp(Principal principal, UpdateAppStatus updateAppStatus, Integer appId) {
        try {
            AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
            Optional<Application> application = applicationRepository.getAppOne(adminEntity.getFutureInstitution().getId(), appId);
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
                        storyMessage.setFirstname(adminEntity.getFistName());
                        storyMessage.setLastname(adminEntity.getLastname());
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
    public Page<AppResponse> searchAllAppByUAdmin(Principal principal, String status, String search, int page, int size) {
        String s = search.toUpperCase();
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        List<AppResponse> responses = new ArrayList<>();
        Page<Application> allApp = applicationRepository.searchAppByFirstnameAndLastname(adminEntity.getFutureInstitution().getId(), status, s, pageable);
        allApp.forEach(application -> {
            AppResponse appResponse = new AppResponse(application);

            appResponse.setEnrolleeResponse(new EnrolleeResponse(application.getEnrolleeInfo()));
            Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
            FileResponse fileResponse = getFileResponse(diploma.getId());
            appResponse.setDiplomaResponse(new DiplomaResponse(diploma, fileResponse));
            responses.add(appResponse);
        });
        return new PageImpl<>(responses, pageable, allApp.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<AppResponse> searchAllAppByStatus(Principal principal, String diplomaStatus, String appStatus, String search, int page, int size) {
        String s = search.toUpperCase();
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        List<AppResponse> responses = new ArrayList<>();
        if (diplomaStatus.equals("true") || diplomaStatus.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(diplomaStatus);

            Page<Application> allApp = applicationRepository.
                    searchAppByFirstnameAndLastnameByDiplomastatus(adminEntity.getFutureInstitution().getId(), appStatus, aBoolean, s, pageable);
            allApp.forEach(application -> {
                AppResponse appResponse = new AppResponse(application);
                appResponse.setEnrolleeResponse(new EnrolleeResponse(application.getEnrolleeInfo()));
                Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                FileResponse fileResponse = getFileResponse(diploma.getId());
                appResponse.setDiplomaResponse(new DiplomaResponse(diploma, fileResponse));
                responses.add(appResponse);
            });
            return new PageImpl<>(responses, pageable, allApp.getTotalElements());
        } else {
            Page<Application> applications = applicationRepository.
                    searchAppByFirstnameAndLastnameByDiplomastatusIsNull(adminEntity.getFutureInstitution().getId(), appStatus, s, pageable);
            applications.forEach(application -> {
                AppResponse appResponse = new AppResponse(application);
                appResponse.setEnrolleeResponse(new EnrolleeResponse(application.getEnrolleeInfo()));
                Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                FileResponse fileResponse = getFileResponse(diploma.getId());
                appResponse.setDiplomaResponse(new DiplomaResponse(diploma, fileResponse));
                responses.add(appResponse);
            });
            return new PageImpl<>(responses, pageable, applications.getTotalElements());
        }
    }

    @Transactional(readOnly = true)
    public Page<DiplomResponseAdmin> searchDiplomasByUAdmin(Principal principal, String status, String search, int page, int size) {
        String s = search.toUpperCase();

        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
        List<DiplomResponseAdmin> diplomResponseAdmins = new ArrayList<>();
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            Page<Application> allDiplomebyUAdmin = applicationRepository.searchDiplomaByUAdmin(institutionId, aBoolean, s, pageable);
            allDiplomebyUAdmin.forEach(application -> {
                EnrolleeResponse enrolleeResponse = new EnrolleeResponse(application.getEnrolleeInfo());
                Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                FileResponse fileResponse = getFileResponse(diploma.getId());
                DiplomResponseAdmin diplomResponseAdmin = new DiplomResponseAdmin(diploma, fileResponse);
                diplomResponseAdmin.setEnrolleeResponse(enrolleeResponse);
                if (application.getDiplomaStatus() != null) {
                    diplomResponseAdmin.setDiplomaStatus(application.getDiplomaStatus().toString());
                }
                diplomResponseAdmins.add(diplomResponseAdmin);
            });
            return new PageImpl<>(diplomResponseAdmins, pageable, allDiplomebyUAdmin.getTotalElements());

        } else {
            Page<Application> allDiplomebyUAdmin = applicationRepository.searchDiplomStatusNull(institutionId, s, pageable);
            allDiplomebyUAdmin.forEach(application -> {

                EnrolleeResponse enrolleeResponse = new EnrolleeResponse(application.getEnrolleeInfo());
                Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                FileResponse fileResponse = getFileResponse(diploma.getId());
                DiplomResponseAdmin diplomResponseAdmin = new DiplomResponseAdmin(diploma, fileResponse);
                diplomResponseAdmin.setEnrolleeResponse(enrolleeResponse);
                if (application.getDiplomaStatus() != null) {
                    diplomResponseAdmin.setDiplomaStatus(application.getDiplomaStatus().toString());
                }
                diplomResponseAdmins.add(diplomResponseAdmin);
            });
            return new PageImpl<>(diplomResponseAdmins, pageable, allDiplomebyUAdmin.getTotalElements());
        }
    }

    //horijiy diplomlarni obshiysi
    @Transactional(readOnly = true)
    public Page<DiplomResponseAdmin> searchForeignDiplomas(Principal principal, String status, String search, int page, int size) {
        String s = search.toUpperCase();
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        List<DiplomResponseAdmin> diplomResponseAdmins = new ArrayList<>();
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            Page<Application> allDiplomebyUAdmin = applicationRepository.searchForeignDiplomas(adminEntity.getFutureInstitution().getId(), aBoolean, s, pageable);
            allDiplomebyUAdmin.forEach(application -> {
                IIBRequest iibRequest = new IIBRequest();
                iibRequest.setPinfl(application.getEnrolleeInfo().getPinfl());
                iibRequest.setGiven_date(application.getEnrolleeInfo().getPassportGivenDate());
                IIBResponse iibResponse = iibServiceApi.iibResponse(iibRequest);
                Data data = iibResponse.getData();
                ImageResponse imageResponse = new ImageResponse();
                if (!data.getPhoto().isEmpty()) {
                    imageResponse.setImage(data.getPhoto());
                }
                EnrolleeResponse enrolleeResponse = new EnrolleeResponse(application.getEnrolleeInfo(), imageResponse);
                Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                FileResponse fileResponse = getFileResponse(diploma.getId());
                DiplomResponseAdmin diplomResponseAdmin = new DiplomResponseAdmin(diploma, fileResponse);
                diplomResponseAdmin.setEnrolleeResponse(enrolleeResponse);
                if (application.getDiplomaStatus() != null) {
                    diplomResponseAdmin.setDiplomaStatus(application.getDiplomaStatus().toString());
                }
                diplomResponseAdmins.add(diplomResponseAdmin);
            });
            return new PageImpl<>(diplomResponseAdmins, pageable, allDiplomebyUAdmin.getTotalElements());
        } else {
            Page<Application> allDiplomebyUAdmin = applicationRepository.searchForeignDiplomaStatusNull(adminEntity.getFutureInstitution().getId(), s, pageable);
            allDiplomebyUAdmin.forEach(application -> {
                IIBRequest iibRequest = new IIBRequest();
                iibRequest.setPinfl(application.getEnrolleeInfo().getPinfl());
                iibRequest.setGiven_date(application.getEnrolleeInfo().getPassportGivenDate());
                IIBResponse iibResponse = iibServiceApi.iibResponse(iibRequest);
                Data data = iibResponse.getData();
                ImageResponse imageResponse = new ImageResponse();
                if (!data.getPhoto().isEmpty()) {
                    imageResponse.setImage(data.getPhoto());
                }
                EnrolleeResponse enrolleeResponse = new EnrolleeResponse(application.getEnrolleeInfo(), imageResponse);
                Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
                FileResponse fileResponse = getFileResponse(diploma.getId());
                DiplomResponseAdmin diplomResponseAdmin = new DiplomResponseAdmin(diploma, fileResponse);
                diplomResponseAdmin.setEnrolleeResponse(enrolleeResponse);
                if (application.getDiplomaStatus() != null) {
                    diplomResponseAdmin.setDiplomaStatus(application.getDiplomaStatus().toString());
                }
                diplomResponseAdmins.add(diplomResponseAdmin);
            });
            return new PageImpl<>(diplomResponseAdmins, pageable, allDiplomebyUAdmin.getTotalElements());
        }
    }

    @Transactional(readOnly = true)
    public CountByUAdmin getAllCountByUAdmin(Principal principal) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
        List<CountApp> countDiploma = applicationRepository.getCountDiploma(institutionId);
        CountByUAdmin response = new CountByUAdmin();
        if (adminEntity.getFutureInstitution() != null) {
            List<CountApp> countApp = applicationRepository.getCountApp(adminEntity.getFutureInstitution().getId());
            List<CountApp> countAppByDiplomaStatus = applicationRepository.getCountAppByDiplomaStatus(adminEntity.getFutureInstitution().getId());
            List<CountApp> countForeignDiploma = applicationRepository.getCountForeignDiploma(adminEntity.getFutureInstitution().getId());
            response.setCountApp(countApp);
            response.setCountForeignDiploma(countForeignDiploma);
            response.setCountAppByDiplomaStatus(countAppByDiplomaStatus);
        }
        response.setCountDiploma(countDiploma);
        return response;
    }

    @Transactional(readOnly = true)
    public List<GetCountAppallDate> getCountAppandTodayByUAdmin(Principal principal) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        return applicationRepository.getAppCountTodayByUAdmin(adminEntity.getFutureInstitution().getId());
    }

    @Transactional(readOnly = true)
    public List<GetAppByGender> getCountAppandGenderByUAdmin(Principal principal) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        List<GetAppByGender> counAppAndGenderByUAdmin = applicationRepository.getCounAppAndGenderByUAdmin(adminEntity.getFutureInstitution().getId());

        return counAppAndGenderByUAdmin;
    }

    @Transactional(readOnly = true)
    public CountGenderAndDiplomaAndApp getCountForeignAndDiplomaandGenderAndTodayByUAdmin(Principal principal) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
        List<GetAppByGender> diplomaAndGender = applicationRepository.getCountDiplomaAndGender(institutionId);
        CountGenderAndDiplomaAndApp statistik = new CountGenderAndDiplomaAndApp();
        List<GetCountAppallDate> diplomaCountTodayByUAdmin = applicationRepository.getDiplomaCountTodayByUAdmin(institutionId);
        statistik.setDiplomaGenderCount(diplomaAndGender);
        statistik.setDiplomaCountToday(diplomaCountTodayByUAdmin);
        List<GetCountAppallDate> foreignDiplomaCountTodayByUAdmin = applicationRepository.getForeignDiplomaCountTodayByUAdmin(adminEntity.getFutureInstitution().getId());
        List<GetAppByGender> countForeingDiplomaAndGender = applicationRepository.getCountForeingDiplomaAndGender(adminEntity.getFutureInstitution().getId());
        statistik.setForeigndiplomaGenderCount(countForeingDiplomaAndGender);
        statistik.setForeigndiplomaCountToday(foreignDiplomaCountTodayByUAdmin);
        return statistik;
    }

    @Transactional(readOnly = true)
    public List<GetDiplomasToExcel> getDiplomasToExcel(Principal principal, String status) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            return applicationRepository.exportDiplomaToExcel(institutionId, aBoolean);
        }
        return applicationRepository.exportDiplomaNullToExcel(institutionId);
    }

    @Transactional(readOnly = true)
    public List<GetDiplomasToExcel> getForeignDiplomasToExcel(Principal principal, String status) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            return applicationRepository.exportForeignDiplomaToExcel(adminEntity.getFutureInstitution().getId(), aBoolean);
        }
        return applicationRepository.exportForeignDiplomaNullToExcel(adminEntity.getFutureInstitution().getId());
    }

    @Transactional(readOnly = true)
    public List<GetAppToExcel> getAppToExcel(Principal principal, String appStatus, String diplomaStatus) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
        if (diplomaStatus.equals("true") || diplomaStatus.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(diplomaStatus);
            return applicationRepository.exportAppDiplomaTrueToExcel(adminEntity.getFutureInstitution().getId());
        } else if (diplomaStatus.equals("null")) {
            return applicationRepository.exportAppByDiplomaNullToExcel(adminEntity.getFutureInstitution().getId());
        } else {
            return applicationRepository.exportAppToExcel(adminEntity.getFutureInstitution().getId(), appStatus);
        }
    }











}