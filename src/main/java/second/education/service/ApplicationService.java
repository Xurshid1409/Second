package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import second.education.repository.DiplomaRepository;
import second.education.service.api.DiplomaApi;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final DiplomaApi diplomaApi;
    private final DiplomaRepository diplomaRepository;
/*
    public Result saveDiploma(String pinfl) {
        try {
            List<DiplomaResponseInfo> diplomasResponse = diplomaApi.getDiploma(pinfl);
            if (diplomasResponse == null) {
                return new Result("Diplom " + ResponseMessage.NOT_FOUND.getMessage(), false);
            }
            List<Diploma> diplomas = new ArrayList<>();
            diplomasResponse.forEach(responseItem -> {
                Diploma diploma = new Diploma();
                diploma.set(responseItem.getDiplomaSerial());
                diploma.setDiplomaNumber(responseItem.getDiplomaNumber());
                diploma.setDegreeId(responseItem.getDegreeId());
                diploma.setDegreeName(responseItem.getDegreeName());
                diploma.setEduFinishingDate(responseItem.getEduFinishingDate());
                diploma.setEduFormId(responseItem.getEduFormId());
                diploma.setEduFormName(responseItem.getEduFormName());
                diploma.setEduStartingDate(responseItem.getEduStartingDate());
                diploma.setInstitutionId(responseItem.getInstitutionId());
                diploma.setInstitutionName(responseItem.getInstitutionName());
                diploma.setInstitutionOldNameId(responseItem.getInstitutionOldNameId());
                diploma.setInstitutionOldName(responseItem.getInstitutionOldName());
                diploma.setInstitutionTypeId(responseItem.getInstitutionTypeId());
                diploma.setInstitutionTypeName(responseItem.getInstitutionTypeName());
                diploma.setSpecialityId(responseItem.getSpecialityId());
                diploma.setSpecialityName(responseItem.getSpecialityName());
                diploma.setSpecialityOldId(responseItem.getSpecialityOldId());
                diploma.setEnrolleeInfo(null);
                diplomas.add(diploma);
            });
            diplomaRepository.saveAll(diplomas);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception e) {
            return new Result("Ma'lumot " + ResponseMessage.NOT_FOUND.getMessage(), false);
        }
    }*/
}
