package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import second.education.domain.Diploma;
import second.education.domain.EnrolleeInfo;
import second.education.domain.classificator.University;
import second.education.model.response.*;
import second.education.repository.ApplicationRepository;
import second.education.repository.DiplomaRepository;
import second.education.repository.EnrolleInfoRepository;
import second.education.repository.InstitutionRepository;

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

    @Transactional
    public DiplomaResponse createDiploma(Principal principal,
                                         String countryName,
                                         Integer institutionId,
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
            diploma.setUniversity(university);
            diploma.setEduFormName(eduFormName);
            diploma.setEduFinishingDate(eduFinishingDate);
            diploma.setSpecialityName(speciality);
            diploma.setDiplomaSerialAndNumber(diplomaNumberAndSerial);
            diploma.setDegreeId(2);
            diploma.setDegreeName("Bakalavr");
            EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByUser(principal.getName()).get();
            diploma.setEnrolleeInfo(enrolleeInfo);
            Diploma diplomaSave = diplomaRepository.save(diploma);
            documentService.documentSave(diplomaSave.getId(), diplomaCopy, diplomaIlova);
            FileResponse fileResponse = documentService.getFileResponse(diplomaSave.getId());
            return new DiplomaResponse(diploma, fileResponse);
        } catch (Exception ex) {
            return new DiplomaResponse();
        }
    }

    @Transactional
    public DiplomaResponse updateDiploma(
            int diplomaId,
            String countryName,
            Integer institutionId,
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
            University university = institutionRepository.findById(institutionId).get();
            diploma.setUniversity(university);
            diploma.setEduFormName(eduFormName);
            diploma.setEduFinishingDate(eduFinishingDate);
            diploma.setSpecialityName(speciality);
            diploma.setDiplomaSerialAndNumber(diplomaNumberAndSerial);
            diploma.setModifiedDate(LocalDateTime.now());
            Diploma diplomaSave = diplomaRepository.save(diploma);
            documentService.documentUpdate(diplomaSave, diplomaCopyId, diplomaIlovaId, diplomaCopy, diplomaIlova);
            FileResponse fileResponse = documentService.getFileResponse(diplomaSave.getId());
            return new DiplomaResponse(diplomaSave, fileResponse);
        } catch (Exception ex) {
            return new DiplomaResponse();
        }
    }

    @Transactional
    public Result checkDiploma(int diplomaId) {
        try {
            List<Diploma> diplomas = diplomaRepository.findAll();
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
            EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByUser(principal.getName()).get();
            EnrolleeResponse enrolleeResponse = new EnrolleeResponse(enrolleeInfo);
            Optional<ApplicationResponse> applicationResponse = applicationRepository.findByAppByPrincipal(enrolleeInfo.getId());
            applicationResponse.ifPresent(enrolleeResponse::setApplicationResponse);
            return enrolleeResponse;
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
}
