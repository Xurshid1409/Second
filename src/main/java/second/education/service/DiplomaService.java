package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.api_model.diplom_api.DiplomaResponseInfo;
import second.education.domain.*;
import second.education.domain.classificator.Country;
import second.education.model.request.DiplomaRequest;
import second.education.model.request.DiplomaStatusRequest;
import second.education.model.response.*;
import second.education.repository.*;
import second.education.service.api.DiplomaApi;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiplomaService {

    private final DiplomaRepository diplomaRepository;
    private final DiplomaApi diplomaApi;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final ApplicationRepository applicationRepository;

//    @Transactional
//    public void saveDiplomaByApi(String pinfl, EnrolleeInfo enrolleeInfo) {
//            List<DiplomaResponseInfo> diplomas = diplomaApi.getDiploma(pinfl);
//            List<Diploma> diplomaList = new ArrayList<>();
//            diplomas.forEach(d -> {
//                Diploma diploma = new Diploma();
//                diploma.setCountryName("O'zbekiston");
//                diploma.setInstitutionId(d.getInstitutionId());
//                diploma.setInstitutionName(d.getInstitutionName());
//                diploma.setInstitutionOldNameId(d.getInstitutionOldNameId());
//                diploma.setInstitutionOldName(d.getInstitutionOldName());
//                diploma.setSpecialityId(d.getSpecialityId());
//                diploma.setSpecialityName(d.getSpecialityName());
//                diploma.setDiplomaSerialAndNumber(d.getDiplomaSerial()+d.getDiplomaNumber());
//                diploma.setDegreeId(d.getDegreeId());
//                diploma.setDegreeName(d.getDegreeName());
//                diploma.setEduFormId(d.getEduFormId());
//                diploma.setEduFormName(d.getEduFormName());
//                diploma.setEduFinishingDate(d.getEduFinishingDate());
//                diploma.setEnrolleeInfo(enrolleeInfo);
//                diplomaList.add(diploma);
//            });
//            if (diplomaList.size()>0){
//                diplomaRepository.saveAll(diplomaList);
//            }
//    }

    // Admin panel

    @Transactional
    public Result updateDiplomaStatus(DiplomaStatusRequest request) {
        try {
            Application application = applicationRepository.findById(request.getApplicationId()).get();
            application.setDiplomaStatus(request.getDiplomaStatus());
            application.setMessage(request.getMessage());
            applicationRepository.save(application);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception exception) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional(readOnly = true)
    public Page<DiplomaResponse> getAllDiploma(int page, int size) {

        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return diplomaRepository.findAll(pageable).map(DiplomaResponse::new);
    }

    @Transactional(readOnly = true)
    public DiplomaResponse getDiplomaById(int diplomaId) {

        try {
            Diploma diploma = diplomaRepository.findById(diplomaId).get();
            return new DiplomaResponse(diploma);
        } catch (Exception ex) {
            return new DiplomaResponse();
        }
    }

    @Transactional
    public Result deleteDiploma(int diplomaId) {
        try {
            diplomaRepository.deleteById(diplomaId);
            return new Result(ResponseMessage.SUCCESSFULLY_DELETED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_DELETED.getMessage(), false);
        }
    }

    private User getCurrentUser(Principal principal) {
        return userRepository.findByPhoneNumber(principal.getName()).get();
    }

    @Transactional(readOnly = true)
    public List<Country> getAllCountry() {
        return countryRepository.findAll();
    }
}
