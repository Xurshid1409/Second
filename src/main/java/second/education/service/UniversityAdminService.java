package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.*;
import second.education.domain.classificator.University;
import second.education.model.response.*;
import second.education.repository.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UniversityAdminService {

    private final DiplomaRepository diplomaRepository;
    private final FutureInstitutionRepository futureInstitutionRepository;
    private final UniversityRepository universityRepository;
    private final UserRepository userRepository;
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
            Page<Application> allDiplomebyUAdmin = applicationRepository.getAppDiplomaByEnrollAppDiplomStatusNull(institutionId, pageable);
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
    public Page<DiplomResponseAdmin> getForeignDiplomas(Principal principal,String status, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        List<DiplomResponseAdmin> diplomResponseAdmins = new ArrayList<>();

        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            Page<Application> allDiplomebyUAdmin = applicationRepository.getAppDipForeign(adminEntity.getFutureInstitution().getId(), aBoolean, pageable);
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
            Page<Application> allDiplomebyUAdmin = applicationRepository.getAppForeignDipStatusNull(adminEntity.getFutureInstitution().getId(), pageable);
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
    //horijiy diplomning bir donasi id orqali
    @Transactional(readOnly = true)
    public Result getForeignDiplomaById(Integer diplomaId, Principal principal) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Optional<Application> application = applicationRepository.getAppAndForeignDiplomaById(adminEntity.getFutureInstitution().getId(), diplomaId);
        if (application.isEmpty()) {
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        }
        EnrolleeResponse enrolleeResponse = new EnrolleeResponse(application.get().getEnrolleeInfo());
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
        EnrolleeResponse enrolleeResponse = new EnrolleeResponse(application.get().getEnrolleeInfo());
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
            appResponse.setEnrolleeResponse(new EnrolleeResponse(application.getEnrolleeInfo()));
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
        Optional<Application> optional = applicationRepository.getAppOne(adminEntity.getFutureInstitution().getId(), AppId);
        if (optional.isEmpty()) {
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        }
        AppResponse appResponse = new AppResponse(optional.get());
        appResponse.setEnrolleeResponse(new EnrolleeResponse(optional.get().getEnrolleeInfo()));
        Diploma diploma = diplomaRepository.getDiplomaByEnrolleeInfoId(optional.get().getEnrolleeInfo().getId()).get();
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

}
