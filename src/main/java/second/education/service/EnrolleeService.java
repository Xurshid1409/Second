package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.Diploma;
import second.education.domain.EnrolleeInfo;
import second.education.model.request.DiplomaRequest;
import second.education.model.response.DiplomaResponse;
import second.education.model.response.EnrolleeResponse;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.DiplomaRepository;
import second.education.repository.EnrolleInfoRepository;
import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrolleeService {

    private final EnrolleInfoRepository enrolleInfoRepository;
    private final DiplomaRepository diplomaRepository;

    @Transactional
    public Result createDiploma(Principal principal, DiplomaRequest diplomaRequest) {

        try {
            EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByUser(principal.getName()).get();
            Diploma diploma = new Diploma();
            diploma.setCountryName(diplomaRequest.getCountryName());
            diploma.setInstitutionName(diplomaRequest.getInstitutionName());
            diploma.setEduFormName(diplomaRequest.getEduFormName());
            diploma.setEduFinishingDate(diplomaRequest.getEduFinishingDate());
            diploma.setSpecialityName(diplomaRequest.getSpeciality());
            diploma.setDiplomaSerialAndNumber(diplomaRequest.getDiplomaNumberAndSerial());
            diploma.setEnrolleeInfo(enrolleeInfo);
            diplomaRepository.save(diploma);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result updateDiploma(Principal principal, DiplomaRequest diplomaRequest) {
        try {
            Diploma diploma = diplomaRepository.getDiplomaByPrincipal(principal.getName()).get();
            diploma.setCountryName(diplomaRequest.getCountryName());
            diploma.setInstitutionName(diplomaRequest.getInstitutionName());
            diploma.setEduFormName(diplomaRequest.getEduFormName());
            diploma.setEduFinishingDate(diplomaRequest.getEduFinishingDate());
            diploma.setSpecialityName(diplomaRequest.getSpeciality());
            diploma.setDiplomaSerialAndNumber(diplomaRequest.getDiplomaNumberAndSerial());
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
            return new EnrolleeResponse(enrolleeInfo);
        } catch (Exception ex) {
            return new EnrolleeResponse();
        }
    }

    @Transactional(readOnly = true)
    public List<DiplomaResponse> getDiplomasByEnrolleeInfo(Principal principal) {
        return diplomaRepository.findAllByEnrolleeInfo(principal.getName())
                .stream().map(DiplomaResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public DiplomaResponse getDiplomaByIdAndEnrolleInfo(Principal principal, int diplomaId) {
        try {
            Diploma diploma = diplomaRepository.findByIdAndEnrolleeInfo(principal.getName(), diplomaId).get();
            return new DiplomaResponse(diploma);
        } catch (Exception ex) {
            return new DiplomaResponse();
        }
    }
}
