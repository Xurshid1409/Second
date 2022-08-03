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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {

    private final ApplicationRepository applicationRepository;
    private final DirectionRepository directionRepository;
    private final EduFormRepository eduFormRepository;
    private final LanguageRepository languageRepository;
    private final IIBServiceApi iibServiceApi;

//    @Transactional(readOnly = true)
    public List<StatisDirectionResponse> getAllStatis(Integer futureInstId){

        List<StatisDirectionResponse> directions = directionRepository.getAllDirectionByFutureInst(futureInstId);
        List<StatisDirectionResponse> statisDirectionResponses = new ArrayList<>();

            directions.forEach(d -> {
            List<StatisEduFormResponse> formResponses = eduFormRepository.findAllByDirectionId(d.getDirectionId());
            StatisDirectionResponse statisDirectionResponse = new StatisDirectionResponse() {
                @Override
                public Integer getDirectionId() {
                    return d.getDirectionId();
                }

                @Override
                public String getDirectionName() {
                    return d.getDirectionName();
                }
                @Override
                public List<StatisEduFormResponse> getStatisEduFormResponses() {
                    List<StatisEduFormResponse> eduFormResponses = new ArrayList<>();
                    formResponses.forEach(f -> {
                        List<StatisLanguageResponse> statisByEduForm = applicationRepository.getStatisByEduForm(f.getEduFormId());
                        StatisEduFormResponse statisEduFormResponse = new StatisEduFormResponse() {
                            @Override
                            public Integer getEduFormId() {
                                return f.getEduFormId();
                            }
                            @Override
                            public String getEduFormName() {
                                return f.getEduFormName();
                            }
                            @Override
                            public List<StatisLanguageResponse> getStatisLanguageResponses() {
                                return statisByEduForm;
                            }
                        };
                        eduFormResponses.add(statisEduFormResponse);
                    });
                    return eduFormResponses;
                }
            };
            statisDirectionResponses.add(statisDirectionResponse);
    });
        return statisDirectionResponses;
    }

    public void getStatisticToAdmin() {

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
