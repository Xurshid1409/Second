package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import second.education.domain.Diploma;
import second.education.domain.Document;
import second.education.domain.EnrolleeInfo;
import second.education.domain.classificator.University;
import second.education.model.response.*;
import second.education.repository.*;

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
    private final FileService fileService;
    private final DocumentRepository documentRepository;

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
            Diploma diploma = new Diploma();
            diploma.setCountryName(countryName);
            University university = institutionRepository.findById(institutionId).get();
            diploma.setInstitutionId(university.getInstitutionId());
            diploma.setInstitutionName(university.getInstitutionName());
            University old = institutionRepository.findById(id).get();
            diploma.setInstitutionOldNameId(old.getId());
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
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result updateDiploma(
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
            Diploma diploma = diplomaRepository.findById(diplomaId).get();
            diploma.setCountryName(countryName);
            diploma.setInstitutionId(institutionId);
            University university = institutionRepository.findById(institutionId).get();
            diploma.setInstitutionName(university.getInstitutionName());
            University old = institutionRepository.findById(id).get();
            diploma.setInstitutionOldNameId(old.getId());
            diploma.setInstitutionOldName(university.getNameOz());
            diploma.setEduFormName(eduFormName);
            diploma.setEduFinishingDate(eduFinishingDate);
            diploma.setSpecialityName(speciality);
            diploma.setDiplomaSerialAndNumber(diplomaNumberAndSerial);
            diploma.setModifiedDate(LocalDateTime.now());
            Diploma diplomaSave = diplomaRepository.save(diploma);
//            documentService.documentUpdate(diplomaSave, diplomaCopyId, diplomaIlovaId, diplomaCopy, diplomaIlova);
            documentUpdate(diplomaSave, diplomaCopyId, diplomaIlovaId, diplomaCopy, diplomaIlova);
//            FileResponse fileResponse = documentService.getFileResponse(diplomaSave.getId());
           return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
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
            //            Optional<ApplicationResponse> applicationResponse = applicationRepository.findByAppByPrincipal(enrolleeInfo.getId());
//            if (applicationResponse.isPresent()) {
//                enrolleeResponse.setApplicationResponse(applicationResponse.get());
//            }else {
//                enrolleeResponse.setApplicationResponse(null);
//            }
            return new EnrolleeResponse(enrolleeInfo);
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
    public Result documentSave(int diplomaId, MultipartFile diplomaCopy, MultipartFile diplomaIlova){

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
    public Result documentUpdate(Diploma diploma, Integer docDiplomId, Integer docIlovaId, MultipartFile docDiplom, MultipartFile docIlova){
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
            if (docIlovaId != null ) {
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

    private String getCurrentUrl(String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/download/")
                .path(fileName).toUriString();
    }
}
