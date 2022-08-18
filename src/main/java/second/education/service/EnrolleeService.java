package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import second.education.api_model.iib_api.Data;
import second.education.api_model.iib_api.IIBResponse;
import second.education.domain.*;
import second.education.domain.classificator.University;
import second.education.model.request.IIBRequest;
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
public class EnrolleeService {

    private final EnrolleInfoRepository enrolleInfoRepository;
    private final DiplomaRepository diplomaRepository;
    private final DocumentService documentService;
    private final ApplicationRepository applicationRepository;
    private final InstitutionRepository institutionRepository;
    private final IIBServiceApi iibServiceApi;
    private final FileService fileService;
    private final DocumentRepository documentRepository;
    private final StoryMessageRepository storyMessageRepository;

    @Transactional
    public Result createDiploma(Principal principal,
                                String countryName,
                                Integer institutionId,
                                Integer id,
                                String eduFormName,
                                String eduFinishingDate,
                                String speciality,
                                String diplomaNumberAndSerial,
                                MultipartFile diplomaCopy,
                                MultipartFile diplomaIlova) {

        try {
            List<Diploma> allDiplomaByEnrollee = diplomaRepository.findAllDiplomaByEnrollee(principal.getName());
            if (allDiplomaByEnrollee.size() > 1) {
                return new Result(ResponseMessage.ALREADY_EXISTS.getMessage(), false);
            }
            Diploma diploma = new Diploma();
            diploma.setCountryName(countryName);
            University university = institutionRepository.findById(institutionId).get();
            diploma.setInstitutionId(institutionId);
            diploma.setInstitutionName(university.getInstitutionName());
//            University old = institutionRepository.findById(id).get();
            diploma.setInstitutionOldNameId(id);
            diploma.setInstitutionOldName(university.getNameOz());
            diploma.setEduFormName(eduFormName);
            diploma.setEduFinishingDate(eduFinishingDate);
            diploma.setSpecialityName(speciality);
            diploma.setDiplomaSerialAndNumber(diplomaNumberAndSerial);
            diploma.setDegreeId(2);
            diploma.setDegreeName("Bakalavr");
            EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByEnrolle(principal.getName()).get();
            diploma.setEnrolleeInfo(enrolleeInfo);
            Diploma diplomaSave = diplomaRepository.save(diploma);
//            documentService.documentSave(diplomaSave.getId(), diplomaCopy, diplomaIlova);
            documentSave(diplomaSave.getId(), diplomaCopy, diplomaIlova);
//            FileResponse fileResponse = documentService.getFileResponse(diplomaSave.getId());
//            FileResponse fileResponse = getFileResponse(diplomaSave.getId());
            Optional<Application> application = applicationRepository.checkApp(principal.getName());
            if (application.isPresent()) {
                application.get().setDiplomaMessage(null);
                application.get().setDiplomaStatus(null);
                applicationRepository.save(application.get());
            }
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result updateDiploma(
            Principal principal,
            int diplomaId,
            String countryName,
            Integer institutionId,
            Integer id,
            String eduFormName,
            String eduFinishingDate,
            String speciality,
            String diplomaNumberAndSerial,
            Integer diplomaCopyId,
            MultipartFile diplomaCopy,
            Integer diplomaIlovaId,
            MultipartFile diplomaIlova) {
        try {

            List<Diploma> diplomas = diplomaRepository.findAllDiplomaByEnrollee(principal.getName());
            for (Diploma diploma : diplomas) {
                if (diploma.getId() == diplomaId) {
                    diploma.setCountryName(countryName);
                    diploma.setInstitutionId(institutionId);
                    University university = institutionRepository.findById(institutionId).get();
                    diploma.setInstitutionName(university.getInstitutionName());
                    diploma.setInstitutionOldNameId(id);
                    diploma.setInstitutionOldName(university.getNameOz());
                    diploma.setEduFormName(eduFormName);
                    diploma.setEduFinishingDate(eduFinishingDate);
                    diploma.setSpecialityName(speciality);
                    diploma.setDiplomaSerialAndNumber(diplomaNumberAndSerial);
                    Optional<Application> appByDiplomId = applicationRepository.getAppByDiplomId(diplomaId);
                    if (appByDiplomId.isPresent()) {
                        appByDiplomId.get().setDiplomaStatus(null);
                        appByDiplomId.get().setDiplomaMessage(null);
                        Application save = applicationRepository.save(appByDiplomId.get());

                        StoryMessage storyMessage = new StoryMessage();
                        storyMessage.setMessage(save.getDiplomaMessage());
                        String status = String.valueOf(save.getDiplomaStatus());
                        storyMessage.setStatus(status);
                        storyMessage.setPinfl(principal.getName());
                        storyMessage.setFirstname(save.getEnrolleeInfo().getFirstname());
                        storyMessage.setLastname(save.getEnrolleeInfo().getLastname());
                        storyMessage.setApplication(save);
                        storyMessageRepository.save(storyMessage);
                    }
                    diploma.setModifiedDate(LocalDateTime.now());
                    Diploma diplomaSave = diplomaRepository.save(diploma);
                    documentUpdate(diplomaSave, diplomaCopyId, diplomaIlovaId, diplomaCopy, diplomaIlova);
                }
                return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
            }
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }


//by admin
    @Transactional
    public Result updateDiplomaByAdmin(
            int diplomaId,
            String countryName,
            Integer institutionId,
            Integer id,
            String eduFormName,
            String eduFinishingDate,
            String speciality,
            String diplomaNumberAndSerial,
            Integer diplomaCopyId,
            MultipartFile diplomaCopy,
            Integer diplomaIlovaId,
            MultipartFile diplomaIlova) {
        try {
            Optional<Diploma> diploma = diplomaRepository.findById(diplomaId);
            if (diploma.isPresent()) {
                diploma.get().setCountryName(countryName);
                diploma.get().setInstitutionId(institutionId);
                University university = institutionRepository.findById(institutionId).get();
                diploma.get().setInstitutionName(university.getInstitutionName());
                diploma.get().setInstitutionOldNameId(id);
                diploma.get().setInstitutionOldName(university.getNameOz());
                diploma.get().setEduFormName(eduFormName);
                diploma.get().setEduFinishingDate(eduFinishingDate);
                diploma.get().setSpecialityName(speciality);
                diploma.get().setDiplomaSerialAndNumber(diplomaNumberAndSerial);
                Optional<Application> appByDiplomId = applicationRepository.getAppByDiplomId(diplomaId);
                if (appByDiplomId.isPresent()) {
                    appByDiplomId.get().setDiplomaStatus(null);
                    appByDiplomId.get().setDiplomaMessage(null);
                    Application save = applicationRepository.save(appByDiplomId.get());
                }
                diploma.get().setModifiedDate(LocalDateTime.now());
                Diploma diplomaSave = diplomaRepository.save(diploma.get());
               documentUpdate(diplomaSave, diplomaCopyId, diplomaIlovaId, diplomaCopy, diplomaIlova);
                return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
            }
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        }catch(Exception ex){
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }


    @Transactional
    public DiplomaResponse createForeignDiploma(Principal principal,
                                                String countryName,
                                                String institutionName,
                                                String eduFormName,
                                                String eduFinishingDate,
                                                String speciality,
                                                String diplomaNumberAndSerial,
                                                MultipartFile diplomaCopy,
                                                MultipartFile diplomaIlova) {
        try {
            Diploma diploma = new Diploma();
            diploma.setCountryName(countryName);
            diploma.setInstitutionName(institutionName);
            diploma.setEduFormName(eduFormName);
            diploma.setEduFinishingDate(eduFinishingDate);
            diploma.setSpecialityName(speciality);
            diploma.setDiplomaSerialAndNumber(diplomaNumberAndSerial);
            diploma.setDegreeId(2);
            diploma.setDegreeName("Bakalavr");
            EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByEnrolle(principal.getName()).get();
            diploma.setEnrolleeInfo(enrolleeInfo);
            Diploma save = diplomaRepository.save(diploma);
//            documentService.documentSave(save.getId(), diplomaCopy, diplomaIlova);
            documentSave(save.getId(), diplomaCopy, diplomaIlova);
//            FileResponse fileResponse = documentService.getFileResponse(diploma.getId());
            FileResponse fileResponse = getFileResponse(save.getId());
            Optional<Application> application = applicationRepository.checkApp(principal.getName());
            if (application.isPresent()) {
                application.get().setDiplomaMessage(null);
                application.get().setDiplomaStatus(null);
                applicationRepository.save(application.get());
            }
            return new DiplomaResponse(diploma, fileResponse);
        } catch (Exception ex) {
            return new DiplomaResponse();
        }
    }

    @Transactional
    public DiplomaResponse updateForeignDiploma(Integer diplomaId,
                                                String countryName,
                                                String institutionName,
                                                String eduFormName,
                                                String eduFinishingDate,
                                                String speciality,
                                                String diplomaNumberAndSerial,
                                                Integer diplomaCopyId,
                                                MultipartFile diplomaCopy,
                                                Integer diplomaIlovaId,
                                                MultipartFile diplomaIlova) {
        try {
            Diploma diploma = diplomaRepository.findById(diplomaId).get();
            diploma.setCountryName(countryName);
            diploma.setInstitutionName(institutionName);
            diploma.setEduFormName(eduFormName);
            diploma.setEduFinishingDate(eduFinishingDate);
            diploma.setSpecialityName(speciality);
            diploma.setDiplomaSerialAndNumber(diplomaNumberAndSerial);

            Optional<Application> appByDiplomId = applicationRepository.getAppByDiplomId(diplomaId);
            if (appByDiplomId.isPresent()) {
                appByDiplomId.get().setDiplomaStatus(null);
                appByDiplomId.get().setDiplomaMessage(null);
                applicationRepository.save(appByDiplomId.get());
            }
            Diploma save = diplomaRepository.save(diploma);
//            documentService.documentUpdate(save, diplomaCopyId, diplomaIlovaId, diplomaCopy, diplomaIlova);
            documentUpdate(save, diplomaCopyId, diplomaIlovaId, diplomaCopy, diplomaIlova);
//            FileResponse fileResponse = documentService.getFileResponse(diploma.getId());
            FileResponse fileResponse = getFileResponse(save.getId());
            return new DiplomaResponse(diploma, fileResponse);
        } catch (Exception ex) {
            return new DiplomaResponse();
        }
    }

    @Transactional
    public Result checkDiploma(Principal principal, int diplomaId) {
        try {
            List<Diploma> diplomas = diplomaRepository.findAllDiplomaByEnrollee(principal.getName());
            diplomas.forEach(diploma -> {
                diploma.setIsActive(Boolean.FALSE);
                diplomaRepository.save(diploma);
            });
            Diploma diploma = diplomaRepository.findById(diplomaId).get();
            diploma.setIsActive(Boolean.TRUE);
            diplomaRepository.save(diploma);
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

    @Transactional(readOnly = true)
    public EnrolleeResponse getEnrolleeResponse(Principal principal) {
        try {
            EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByEnrolle(principal.getName()).get();
            IIBRequest iibRequest = new IIBRequest();
            iibRequest.setPinfl(enrolleeInfo.getPinfl());
            iibRequest.setGiven_date(enrolleeInfo.getPassportGivenDate());
            IIBResponse iibResponse = iibServiceApi.iibResponse(iibRequest);
            Data data = iibResponse.getData();
            ImageResponse imageResponse = new ImageResponse();
            if (!data.getPhoto().isEmpty()) {
                imageResponse.setImage(data.getPhoto());
            }
            //            Optional<ApplicationResponse> applicationResponse = applicationRepository.findByAppByPrincipal(enrolleeInfo.getId());
//            if (applicationResponse.isPresent()) {
//                enrolleeResponse.setApplicationResponse(applicationResponse.get());
//            }else {
//                enrolleeResponse.setApplicationResponse(null);
//            }
            return new EnrolleeResponse(enrolleeInfo, imageResponse);
        } catch (Exception ex) {
            return new EnrolleeResponse();
        }
    }

    @Transactional(readOnly = true)
    public List<DiplomaResponse> getDiplomasByEnrolleeInfo(Principal principal) {
        List<Diploma> diplomas = diplomaRepository.findAllByEnrolleeInfo(principal.getName());
        List<DiplomaResponse> diplomaResponses = new ArrayList<>();
        diplomas.forEach(diploma -> {
            DiplomaResponse diplomaResponse = new DiplomaResponse(diploma);
            FileResponse fileResponse = documentService.getFileResponse(diploma.getId());
            if (fileResponse != null) {
                diplomaResponse.setFileResponse(fileResponse);
            }
            diplomaResponses.add(diplomaResponse);
        });
        return diplomaResponses;
    }

    @Transactional(readOnly = true)
    public DiplomaResponse getDiplomaByIdAndEnrolleInfo(Principal principal, Integer diplomaId) {
        try {
            Diploma diploma = diplomaRepository.findByIdAndEnrolleeInfo(principal.getName(), diplomaId).get();
            FileResponse fileResponse = documentService.getFileResponse(diploma.getId());
            return new DiplomaResponse(diploma, fileResponse);
        } catch (Exception ex) {
            return new DiplomaResponse();
        }
    }

    @Transactional(readOnly = true)
    public DiplomaResponse getDiplomaProfile(Principal principal) {
        try {
            Diploma diploma = diplomaRepository.getDiplomaProfile(principal.getName()).get();
            FileResponse fileResponse = documentService.getFileResponse(diploma.getId());
            return new DiplomaResponse(diploma, fileResponse);
        } catch (Exception ex) {
            return new DiplomaResponse();
        }
    }

    @Transactional
    public Result documentSave(int diplomaId, MultipartFile diplomaCopy, MultipartFile diplomaIlova) {

        try {
            Diploma diploma = diplomaRepository.findById(diplomaId).get();
            List<Document> documents = new ArrayList<>();
            if (diplomaCopy != null) {
                Document diplom = new Document();
                String diplomName = fileService.upload(diplomaCopy, "Diplom");
                String currentUrl = getCurrentUrl(diplomName);
                diplom.setUrl(currentUrl);
                diplom.setFileName(diplomName);
                diplom.setDiploma(diploma);
                documents.add(diplom);
            }
            if (diplomaIlova != null) {
                Document diplomIlova = new Document();
                String ilovaName = fileService.upload(diplomaIlova, "Ilova");
                String currentUrl = getCurrentUrl(ilovaName);
                diplomIlova.setUrl(currentUrl);
                diplomIlova.setFileName(ilovaName);
                diplomIlova.setDiploma(diploma);
                documents.add(diplomIlova);
            }
            documentRepository.saveAll(documents);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result documentUpdate(Diploma diploma, Integer docDiplomId, Integer docIlovaId, MultipartFile docDiplom, MultipartFile docIlova) {
        try {
            List<Document> documents = new ArrayList<>();
            if (docDiplomId != null) {
                if (docDiplom != null) {
                    Document document = documentRepository.findById(docDiplomId).get();
                    fileService.deleteDocument(document);
                    String diplomName = fileService.upload(docDiplom, "Diplom");
                    String currentUrl = getCurrentUrl(diplomName);
                    document.setUrl(currentUrl);
                    document.setFileName(diplomName);
                    document.setModifiedDate(LocalDateTime.now());
                    documents.add(document);
                }
            } else {
                Document document = new Document();
                String diplomName = fileService.upload(docDiplom, "Diplom");
                String currentUrl = getCurrentUrl(diplomName);
                document.setFileName(diplomName);
                document.setUrl(currentUrl);
                document.setDiploma(diploma);
                documents.add(document);
            }
            if (docIlovaId != null) {
                if (docIlova != null) {
                    Document documentIlova = documentRepository.findById(docIlovaId).get();
                    fileService.deleteDocument(documentIlova);
                    String diplomIlovaName = fileService.upload(docIlova, "Ilova");
                    String currentUrl = getCurrentUrl(diplomIlovaName);
                    documentIlova.setUrl(currentUrl);
                    documentIlova.setFileName(diplomIlovaName);
                    documentIlova.setModifiedDate(LocalDateTime.now());
                    documents.add(documentIlova);
                }
            } else {
                Document documentIlova = new Document();
                String diplomIlovaName = fileService.upload(docIlova, "Ilova");
                String currentUrl = getCurrentUrl(diplomIlovaName);
                documentIlova.setFileName(diplomIlovaName);
                documentIlova.setUrl(currentUrl);
                documentIlova.setDiploma(diploma);
                documents.add(documentIlova);
            }

            if (documents.size() > 0) {
                documentRepository.saveAll(documents);
            }
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

    @Transactional
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

    @Transactional
    public Result deleteDiploma(Integer diplomaId, Principal principal) {
        try {
            Optional<Diploma> byId = diplomaRepository.findById(diplomaId);
            EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByEnrolle(principal.getName()).get();
            if (byId.isPresent()) {
                if (byId.get().getEnrolleeInfo().getId().equals(enrolleeInfo.getId())) {
                    diplomaRepository.deleteById(diplomaId);
                    return new Result(ResponseMessage.SUCCESSFULLY_DELETED.getMessage(), true);
                }
                return new Result("bu sizning diplomingiz emas", false);
            }
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        } catch (Exception e) {
            return new Result(ResponseMessage.ERROR_DELETED.getMessage(), false);
        }
    }

    private String getCurrentUrl(String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/download/")
                .path(fileName).toUriString();
    }
}
