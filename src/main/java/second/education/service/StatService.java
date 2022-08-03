package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.classificator.FutureInstitution;
import second.education.model.request.IIBRequest;
import second.education.model.response.StatisDirectionResponse;
import second.education.model.response.StatisDirectionResponseByFutureInst;
import second.education.model.response.StatisEduFormResponse;
import second.education.model.response.StatisLanguageResponse;
import second.education.repository.*;
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
    private final FutureInstitutionRepository futureInstitutionRepository;

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

    public List<StatisDirectionResponseByFutureInst> getStatisticToAdmin() {

        List<FutureInstitution> futureInstitutions = futureInstitutionRepository.findAll();
        List<StatisDirectionResponseByFutureInst> statisDirectionResponses = new ArrayList<>();
        futureInstitutions.forEach(futureInstitution -> {
            List<StatisDirectionResponseByFutureInst> directions = directionRepository.getAllDirectionByFutureInstitutions(futureInstitution.getId());
            directions.forEach(d -> {
                List<StatisEduFormResponse> formResponses = eduFormRepository.findAllByDirectionId(d.getDirectionId());
                StatisDirectionResponseByFutureInst statisDirectionResponse = new StatisDirectionResponseByFutureInst() {
                    @Override
                    public String getFutureInstName() {
                        return d.getFutureInstName();
                    }

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
        });
        return statisDirectionResponses;
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
