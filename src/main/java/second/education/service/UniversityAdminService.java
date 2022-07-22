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


    private final ApplicationRepository applicationRepository;
    private final DocumentRepository documentRepository;

    private final AdminEntityRepository adminEntityRepository;

    @Transactional(readOnly = true)
    public Page<DiplomResponseAdmin> getDiplomas(Principal principal, String status, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
        List<DiplomResponseAdmin> diplomResponseAdmins = new ArrayList<>();
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            Page<Application> allDiplomebyUAdmin = applicationRepository.getAppDiplomaByEnrollId(institutionId, aBoolean, pageable);
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
            Page<Application> allDiplomebyUAdmin = applicationRepository.getAppDiplomaByEnrollAppDiplomStatusNull(institutionId, pageable);
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

    //horijiy diplomlarni obshiysi
    @Transactional(readOnly = true)
    public Page<DiplomResponseAdmin> getForeignDiplomas(Principal principal, String status, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        List<DiplomResponseAdmin> diplomResponseAdmins = new ArrayList<>();
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            Page<Application> allDiplomebyUAdmin = applicationRepository.getAppDipForeign(adminEntity.getFutureInstitution().getId(), aBoolean, pageable);
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
            Page<Application> allDiplomebyUAdmin = applicationRepository.getAppForeignDipStatusNull(adminEntity.getFutureInstitution().getId(), pageable);
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


        return new Result("diploma", true, diplomResponseAdmin);
    }

    @Transactional(readOnly = true)
    public Page<AppResponse> getAllAppByUAdmin(Principal principal, String status, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        List<AppResponse> responses = new ArrayList<>();
        Page<Application> allApp = applicationRepository.getAllApp(adminEntity.getFutureInstitution().getId(), status, pageable);
        allApp.forEach(application -> {
            AppResponse appResponse = new AppResponse(application);
            IIBRequest iibRequest = new IIBRequest();
            iibRequest.setPinfl(application.getEnrolleeInfo().getPinfl());
            iibRequest.setGiven_date(application.getEnrolleeInfo().getPassportGivenDate());
            IIBResponse iibResponse = iibServiceApi.iibResponse(iibRequest);
            Data data = iibResponse.getData();
            ImageResponse imageResponse = new ImageResponse();
            if (!data.getPhoto().isEmpty()) {
                imageResponse.setImage(data.getPhoto());
            }
            appResponse.setEnrolleeResponse(new EnrolleeResponse(application.getEnrolleeInfo(), imageResponse));
            Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
            FileResponse fileResponse = getFileResponse(diploma.getId());
            appResponse.setDiplomaResponse(new DiplomaResponse(diploma, fileResponse));
            responses.add(appResponse);
        });
        return new PageImpl<>(responses, pageable, allApp.getTotalElements());
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
                String status = String.valueOf(application.get().getDiplomaStatus());
                if (status.equals("null")) {
                    application.get().setDiplomaStatus(updateDiplomaStatus.getDiplomStatus());
                    application.get().setDiplomaMessage(updateDiplomaStatus.getDiplomMessage());
                    applicationRepository.save(application.get());
                    return new Result("Muvaffaqiyatli tasdiqlandi", true);
                } else if (status.equals("true")) {
                    return new Result("diplom tasdiqlangan", false);
                } else if (status.equals("false")) {
                    return new Result("diplom rad etilgan o'zgarishini kuting", false);
                }

            }
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        } catch (Exception e) {
            return new Result("Tasdiqlashda xatolik", false);
        }
    }

    @Transactional
    public Result updateStatusApp(Principal principal, UpdateAppStatus updateAppStatus, Integer appId) {
        try {
            AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
            Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
            Optional<Application> application = applicationRepository.getAppOne(institutionId, appId);
            if (application.isPresent()) {
                String status = String.valueOf(application.get().getDiplomaStatus());
                switch (status) {
                    case "true":
                        application.get().setStatus(updateAppStatus.getAppStatus());
                        application.get().setMessage(updateAppStatus.getAppMessage());
                        applicationRepository.save(application.get());
                        return new Result("Muvaffaqiyatli tasdiqlandi", true);
                    case "false":
                        return new Result("bu arizaning diplomi rad etilgan", false);
                    case "null":
                        return new Result("ariza diplomi holi tasdiqlanmagan ", false);
                }
            }
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        } catch (Exception e) {
            return new Result("Tasdiqlashda xatolik", false);
        }
    }

    @Transactional(readOnly = true)
    public Page<AppResponse> searchAllAppByUAdmin(Principal principal, String status, String search, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        List<AppResponse> responses = new ArrayList<>();
        Page<Application> allApp = applicationRepository.searchAppByFirstnameAndLastname(adminEntity.getFutureInstitution().getId(), status, search, pageable);
        allApp.forEach(application -> {
            AppResponse appResponse = new AppResponse(application);
            IIBRequest iibRequest = new IIBRequest();
            iibRequest.setPinfl(application.getEnrolleeInfo().getPinfl());
            iibRequest.setGiven_date(application.getEnrolleeInfo().getPassportGivenDate());
            IIBResponse iibResponse = iibServiceApi.iibResponse(iibRequest);
            Data data = iibResponse.getData();
            ImageResponse imageResponse = new ImageResponse();
            if (!data.getPhoto().isEmpty()) {
                imageResponse.setImage(data.getPhoto());
            }
            appResponse.setEnrolleeResponse(new EnrolleeResponse(application.getEnrolleeInfo(), imageResponse));
            Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(application.getEnrolleeInfo().getId()).get();
            FileResponse fileResponse = getFileResponse(diploma.getId());
            appResponse.setDiplomaResponse(new DiplomaResponse(diploma, fileResponse));
            responses.add(appResponse);
        });
        return new PageImpl<>(responses, pageable, allApp.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<DiplomResponseAdmin> searchDiplomasByUAdmin(Principal principal, String status, String search, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
        List<DiplomResponseAdmin> diplomResponseAdmins = new ArrayList<>();
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            Page<Application> allDiplomebyUAdmin = applicationRepository.searchDiplomaByUAdmin(institutionId, aBoolean, search, pageable);
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
            Page<Application> allDiplomebyUAdmin = applicationRepository.searchDiplomStatusNull(institutionId, search, pageable);
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

    //horijiy diplomlarni obshiysi
    @Transactional(readOnly = true)
    public Page<DiplomResponseAdmin> searchForeignDiplomas(Principal principal, String status, String search, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        List<DiplomResponseAdmin> diplomResponseAdmins = new ArrayList<>();
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            Page<Application> allDiplomebyUAdmin = applicationRepository.searchForeignDiplomas(adminEntity.getFutureInstitution().getId(), aBoolean, search, pageable);
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
            Page<Application> allDiplomebyUAdmin = applicationRepository.searchForeignDiplomaStatusNull(adminEntity.getFutureInstitution().getId(), search, pageable);
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


}
