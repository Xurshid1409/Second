package second.education.service.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import second.education.api_model.actual_inst_api.ActualInstitutionResponse;
import second.education.api_model.actual_inst_api.Institutions;
import second.education.api_model.diplom_api.DiplomaResponseInfo;
import second.education.api_model.ins_api.DataItem;
import second.education.api_model.ins_api.InstitutionResponse;
import second.education.api_model.spec_api.SpecialistResponse;
import second.education.domain.ActiveUniversity;
import second.education.domain.classificator.Specialities;
import second.education.domain.classificator.University;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.model.response.SpecialitiesResponse;
import second.education.model.response.UniversityResponse;
import second.education.repository.ActiveUniversityRepository;
import second.education.repository.InstitutionRepository;
import second.education.repository.SpecialistRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiplomaApi {

    private final WebClient webClient;
    private final InstitutionRepository institutionRepository;
    private final SpecialistRepository specialistRepository;
    private final ActiveUniversityRepository activeUniversityRepository;

    String token = "jfqubdpWtR8qAOiUrFq5iPRlU8j1uOEaVWqHw6iLLgdV5Q_0-MBWEm3du3GjsZ726RFyuFSB_qRZrDlQj4kBAf3cBDxlpMwr_Ezp8LzbumTMIe5jTFsVCMuD7O3QjJtBWInZvjyzIs_8nEzFRVEBKsK4MJDSbEz56v58fAoRxk6HQpu9uuXUpgHcQaoTTtmuY21-Rtc";

    public List<DiplomaResponseInfo> getDiploma(String pinfl) {

        String DIPLOMA_URL = "http://172.18.10.10/api/v2/diploma/get?pinfl=" + pinfl;
        return this.webClient.get()
                .uri(DIPLOMA_URL)
                .headers(httpHeader -> httpHeader.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(DiplomaResponseInfo.class)
                .collectList()
                .block();
    }

    private InstitutionResponse getInstitutions() {

        String INSTITUTION_URL = "172.18.10.10/api/v2/reference/institution-old-names";
        return this.webClient.get()
                .uri(INSTITUTION_URL)
                .headers(httpHeader -> httpHeader.setBearerAuth(token))
                .retrieve()
                .bodyToMono(InstitutionResponse.class)
                .block();
    }

    private SpecialistResponse getSpecialities() {
        String SPECIALITIES_URL = "http://172.18.10.10/api/v2/reference/specialities";
        return this.webClient.get()
                .uri(SPECIALITIES_URL)
                .headers(httpHeader -> httpHeader.setBearerAuth(token))
                .retrieve()
                .bodyToMono(SpecialistResponse.class)
                .block();
    }

    private ActualInstitutionResponse getActualInstitutions() {
        String ACTUAL_INSTITUTION_URL = "http://172.18.10.10/api/v2/reference/institutions";
        return webClient.get()
                .uri(ACTUAL_INSTITUTION_URL)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(ActualInstitutionResponse.class)
                .block();
    }

    @Transactional
    public Result saveActualInstitution() {
        try {

            List<ActiveUniversity> activeUniversityList = activeUniversityRepository.findAll();
            if (activeUniversityList.isEmpty()) {
                Institutions institutions = getActualInstitutions().getData().getInstitutions();
                List<ActiveUniversity> activeUniversities = new ArrayList<>();
                institutions.getData().forEach(institution -> {
                    ActiveUniversity activeUniversity = new ActiveUniversity();
                    activeUniversity.setInstitutionId(institution.getId());
                    activeUniversity.setNameUz(institution.getNameUz());
                    activeUniversity.setNameOz(institution.getNameOz());
                    activeUniversity.setNameRu(institution.getNameRu());
                    activeUniversity.setNameEn(institution.getNameEn());
                    activeUniversity.setRegionName(institution.getRegionName());
                    activeUniversities.add(activeUniversity);
                });
                activeUniversityRepository.saveAll(activeUniversities);
                return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
            }
            return new Result(ResponseMessage.ALREADY_EXISTS.getMessage(), false);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }


    @Transactional
    public Result saveInstitution() {

        try {
            InstitutionResponse institutions = getInstitutions();
            List<DataItem> dataItems = institutions.getData().getInstitutionsOldNames().getData();
            List<University> universities = institutionRepository.findAll();
            if (universities.isEmpty()) {
                dataItems.forEach(dataItem -> {
                    University university = new University();
                    university.setInstitutionId(dataItem.getInstitutionId());
                    university.setInstitutionName(dataItem.getInstitutionName());
                    university.setInstitutionTypeId(dataItem.getInstitutionTypeId());
                    university.setInstitutionTypeName(dataItem.getInstitutionTypeName());
                    university.setNameOz(dataItem.getNameOz());
                    university.setNameUz(dataItem.getNameUz());
                    university.setNameRu(dataItem.getNameRu());
                    university.setNameEn(dataItem.getNameEn());
                    university.setRegionSoatoId(dataItem.getRegionSoatoId());
                    university.setRegionName(dataItem.getRegionName());
                    university.setStatusName(dataItem.getStatusName());
                    universities.add(university);
                });
                institutionRepository.saveAll(universities);
                return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
            }
                return new Result(ResponseMessage.ALREADY_EXISTS.getMessage(), false);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result saveSpecialities() {
        try {
            SpecialistResponse specialistResponse = getSpecialities();
            List<second.education.api_model.spec_api.DataItem> dataItems = specialistResponse.getData().getSpecialities().getData();
            List<Specialities> specialitiesList = specialistRepository.findAll();
            if (specialitiesList.isEmpty()) {
                dataItems.forEach(dataItem -> {
                    Specialities specialities = new Specialities();
                    specialities.setSpecialitiesId(dataItem.getId());
                    specialities.setInstitutionId(dataItem.getInstitutionId());
                    specialities.setNameOz(dataItem.getNameOz());
                    specialities.setNameUz(dataItem.getNameUz());
                    specialities.setNameEn(dataItem.getNameEn());
                    specialities.setNameRu(dataItem.getNameRu());
                    specialities.setStatusId(dataItem.getStatusId());
                    specialities.setBeginYear(dataItem.getBeginYear());
                    specialitiesList.add(specialities);
                });
                specialistRepository.saveAll(specialitiesList);
                return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
            }
            return new Result(ResponseMessage.ALREADY_EXISTS.getMessage(), false);
        } catch (Exception e) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional(readOnly = true)
    public List<UniversityResponse> getActiveUniversities() {
        List<ActiveUniversity> activeUniversityList = activeUniversityRepository.findAll();
        List<UniversityResponse> universityResponses = new ArrayList<>();
        activeUniversityList.forEach(activeUniversity -> {
            UniversityResponse universityResponse = new UniversityResponse();
            universityResponse.setId(activeUniversity.getId());
            universityResponse.setInstitutionId(activeUniversity.getInstitutionId());
            universityResponse.setNameUz(activeUniversity.getNameUz());
            universityResponse.setNameOz(activeUniversity.getNameOz());
            universityResponse.setNameRu(activeUniversity.getNameRu());
            universityResponse.setNameEn(activeUniversity.getNameEn());
            universityResponse.setRegionName(activeUniversity.getRegionName());
            universityResponses.add(universityResponse);
        });
        return universityResponses;
    }

    @Transactional(readOnly = true)
    public List<UniversityResponse> getUniversities() {
        List<University> universities = institutionRepository.findAll();
        List<UniversityResponse> universityResponses = new ArrayList<>();
        universities.forEach(university -> {
            UniversityResponse universityResponse = new UniversityResponse();
            universityResponse.setId(university.getId());
            universityResponse.setInstitutionId(university.getInstitutionId());
            universityResponse.setInstitutionName(university.getInstitutionName());
            universityResponse.setInstitutionTypeId(university.getInstitutionTypeId());
            universityResponse.setStatusName(university.getStatusName());
            universityResponse.setNameOz(university.getNameOz());
            universityResponse.setNameUz(university.getNameUz());
            universityResponse.setNameRu(university.getNameRu());
            universityResponse.setNameEn(university.getNameEn());
            universityResponse.setRegionSoatoId(university.getRegionSoatoId());
            universityResponse.setRegionName(university.getRegionName());
            universityResponses.add(universityResponse);
        });
        return universityResponses;
    }

    public List<SpecialitiesResponse> getSpecialitiesByUniversityId(Integer universityId) {
        List<Specialities> specialities = specialistRepository.findAllByInstitutionId(universityId);
        List<SpecialitiesResponse> specialitiesResponses = new ArrayList<>();
        specialities.forEach(speciality -> {
            SpecialitiesResponse specialitiesResponse = new SpecialitiesResponse();
            specialitiesResponse.setId(speciality.getId());
            specialitiesResponse.setSpecialitiesId(speciality.getSpecialitiesId());
            specialitiesResponse.setBeginYear(speciality.getBeginYear());
            specialitiesResponse.setInstitutionId(speciality.getInstitutionId());
            specialitiesResponse.setNameOz(speciality.getNameOz());
            specialitiesResponse.setNameUz(speciality.getNameUz());
            specialitiesResponse.setNameRu(speciality.getNameRu());
            specialitiesResponse.setNameEn(speciality.getNameEn());
            specialitiesResponse.setCreatorId(speciality.getCreatorId());
            specialitiesResponses.add(specialitiesResponse);
        });
        return specialitiesResponses;
    }
}
