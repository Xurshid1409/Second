package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.model.request.IIBRequest;
import second.education.model.response.StatisDirectionResponse;
import second.education.model.response.StatisEduFormResponse;
import second.education.model.response.StatisLanguageResponse;
import second.education.repository.ApplicationRepository;
import second.education.repository.DirectionRepository;
import second.education.repository.EduFormRepository;
import second.education.repository.LanguageRepository;
import second.education.service.api.IIBServiceApi;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {

    private final ApplicationRepository applicationRepository;
    private final DirectionRepository directionRepository;
    private final EduFormRepository eduFormRepository;
    private final LanguageRepository languageRepository;
    private final IIBServiceApi iibServiceApi;

    @Transactional(readOnly = true)
    public List<StatisDirectionResponse> getAllStatis(Integer futureInstId){
        List<StatisDirectionResponse> directions = directionRepository.getAllDirectionByFutureInst(futureInstId);
//        directions.forEach(d -> {
//            List<StatisEduFormResponse> eduForm = eduFormRepository.findAllByDirectionId(d.getDirectionId());
//
//            eduForm.forEach(e -> {
//                List<StatisLanguageResponse> languages = applicationRepository.getStatisByEduForm(e.getEduFormId());
//            });
//        });
       return directions;
    }



    @Transactional
    public String checkIIB(IIBRequest request) {
        try {
             return iibServiceApi.checkIIB(request);
        } catch (Exception ex) {
            return "Pinfl noto'gri kiritilgan, yoki qaytadan urinib ko'ring";
        }
    }
}
