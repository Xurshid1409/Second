package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.model.request.IIBRequest;
import second.education.model.response.GetStatisByDirection;
import second.education.model.response.GetStatisByEduForm;
import second.education.model.response.GetStatisByLanguage;
import second.education.repository.ApplicationRepository;
import second.education.repository.DirectionRepository;
import second.education.repository.EduFormRepository;
import second.education.repository.LanguageRepository;
import second.education.service.api.IIBServiceApi;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {

    private final ApplicationRepository applicationRepository;
    private final DirectionRepository directionRepository;
    private final EduFormRepository eduFormRepository;
    private final LanguageRepository languageRepository;
    private final IIBServiceApi iibServiceApi;

//    public List<GetStatisByDirection> getStatisByDirections(Integer futureInstId) {
//        List<GetStatisByDirection> directionList = directionRepository.getAllByFutureInstitutionId(futureInstId);
//        List<GetStatisByDirection> getStatisByDirections = new ArrayList<>();
//        directionList.forEach(d -> {
//            List<GetStatisByEduForm> eduForms = eduFormRepository.findAllByDirectionId(d.getDirectionId());
//            eduForms.forEach(eduForm -> {
//                List<GetStatisByLanguage> languages = languageRepository.findAllByEduFormId(eduForm.getEduFormId());
//                languages.forEach(l -> {
//                    eduForm.getStatisByLanguage().add(l);
//                });
//                d.getStatisByEduForm().add(eduForm);
//            });
//            getStatisByDirections.add(d);
//        });
//        return getStatisByDirections;
//    }

    @Transactional
    public String checkIIB(IIBRequest request) {
        try {
             return iibServiceApi.checkIIB(request);
        } catch (Exception ex) {
            return "Pinfl noto'gri kiritilgan, yoki qaytadan urinib ko'ring";
        }
    }
}
