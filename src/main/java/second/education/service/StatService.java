package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.classificator.FutureInstitution;
import second.education.model.request.IIBRequest;
import second.education.model.response.*;
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
    public List<StatisDirectionResponse> getAllStatis(Integer futureInstId) {

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


    //statistik All universiteti

    @Transactional(readOnly = true)
    public List<AcceptAndRejectAndCheckDiploma> statisticAllUniversity() {
        List<AcceptAndRejectApp> acceptAndRejectApp = applicationRepository.getAcceptAndRejectApp();
        List<AcceptAndRejectApp> getcheckDiploma = applicationRepository.getcheckDiploma();
        List<AcceptAndRejectAndCheckDiploma> list = new ArrayList<>();
        acceptAndRejectApp.forEach(acceptAndRejectApp1 -> {
            getcheckDiploma.forEach(acceptAndRejectApp2 -> {
                if (acceptAndRejectApp1.getFutureId().equals(acceptAndRejectApp2.getFutureId())) {
                    AcceptAndRejectAndCheckDiploma statistic = new AcceptAndRejectAndCheckDiploma();
                    if (acceptAndRejectApp1.getStatus().equals("Ariza qabul qilindi")) {
                        statistic.setAcceptCount(acceptAndRejectApp1.getCount());
                    } else if (acceptAndRejectApp1.getStatus().equals("Ariza rad etildi")) {
                        statistic.setRejectCount(acceptAndRejectApp1.getCount());
                    }
                    statistic.setCheckCount(acceptAndRejectApp2.getCount());
                    statistic.setFutureInstName(acceptAndRejectApp2.getFutureInstName());
                    list.add(statistic);
                }

            });

        });
        return list;
    }
}
