package second.education.service.api;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import second.education.api_model.diplom_api.DiplomaResponseInfo;
import second.education.api_model.ins_api.DataItem;
import second.education.api_model.ins_api.InstitutionResponse;
import second.education.api_model.spec_api.SpecialistResponse;
import second.education.domain.classificator.Specialities;
import second.education.domain.classificator.University;
import second.education.model.response.ResponseMessage;
import second.education.model.response.SpecialitiesResponse;
import second.education.model.response.UniversityResponse;
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
    String token = "jfqubdpWtR8qAOiUrFq5iPRlU8j1uOEaVWqHw6iLLgdV5Q_0-MBWEm3du3GjsZ726RFyuFSB_qRZrDlQj4kBAf3cBDxlpMwr_Ezp8LzbumTMIe5jTFsVCMuD7O3QjJtBWInZvjyzIs_8nEzFRVEBKsK4MJDSbEz56v58fAoRxk6HQpu9uuXUpgHcQaoTTtmuY21-Rtc";
    private static final Logger LOG = LoggerFactory.getLogger(DiplomaApi.class);

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

    @Transactional
    public void saveInstitution() {

        try {
            InstitutionResponse institutions = getInstitutions();
            List<DataItem> dataItems = institutions.getData().getInstitutionsOldNames().getData();
            List<University> universities = new ArrayList<>();
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
            LOG.info(ResponseMessage.SUCCESSFULLY_SAVED.getMessage());
        } catch (Exception ex) {
            LOG.error(ResponseMessage.ERROR_SAVED.getMessage(), ex);
        }
    }

    @Transactional
    public void saveSpecialities() {
        try {
            SpecialistResponse specialistResponse = getSpecialities();
            List<second.education.api_model.spec_api.DataItem> dataItems = specialistResponse.getData().getSpecialities().getData();
            List<Specialities> specialitiesList = new ArrayList<>();
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
        } catch (Exception e) {
            LOG.error(ResponseMessage.ERROR_SAVED.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<UniversityResponse> getUniversities() {
        List<University> universities = institutionRepository.findAll();
        if (universities.isEmpty()) {
            saveInstitution();
        }
        List<UniversityResponse> universityResponses = new ArrayList<>(1000);
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
        if (specialities.isEmpty()) {
            saveSpecialities();
        }
        List<SpecialitiesResponse> specialitiesResponses = new ArrayList<>(1000);
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
