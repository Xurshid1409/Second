package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.AdminEntity;
import second.education.domain.classificator.FutureInstitution;
import second.education.model.request.IIBRequest;
import second.education.model.response.*;
import second.education.repository.*;
import second.education.service.api.IIBServiceApi;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

        List<StatisDirectionResponseByFutureInst> statisDirectionResponses = new ArrayList<>();
        List<StatisDirectionResponseByFutureInst> directions = directionRepository.getAllDirectionByFutureInstitutions();
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

        List<FutureInstitution> futureInstitutions = futureInstitutionRepository.findAll();
        List<AcceptAndRejectAndCheckDiploma> list = new ArrayList<>();
        futureInstitutions.forEach(futureInstitution -> {
            List<AcceptAndRejectApp> acceptAndRejectApp = applicationRepository.getAcceptAndRejectApp(futureInstitution.getId());
            AcceptAndRejectAndCheckDiploma statistic = new AcceptAndRejectAndCheckDiploma();
            acceptAndRejectApp.forEach(acceptAndRejectApp1 -> {
                if (acceptAndRejectApp1.getStatus().equals("Ariza qabul qilindi")) {
                    statistic.setAcceptAppCount(acceptAndRejectApp1.getCount());
                } else if (acceptAndRejectApp1.getStatus().equals("Ariza rad etildi")) {
                    statistic.setRejectAppCount(acceptAndRejectApp1.getCount());
                }
            });
            Optional<AcceptAndRejectApp> acceptAndRejectApp2 = applicationRepository.getcheckDiploma(futureInstitution.getId());
            acceptAndRejectApp2.ifPresent(andRejectApp -> statistic.setCheckDiplomaCount(andRejectApp.getCount()));
            statistic.setFutureInstName(futureInstitution.getName());
            Optional<AcceptAndRejectApp> acceptDiploma = applicationRepository.getAcceptDiploma(futureInstitution.getId());
            acceptDiploma.ifPresent(acceptDiploma1 -> statistic.setAcceptDiplomaCount(acceptDiploma1.getCount()));
            list.add(statistic);
        });

        return list;
    }
    @Transactional(readOnly = true)
    public List<GetCountAppallDate> getCountAppandTodayByAdmin() {
        return applicationRepository.getAppCountTodayBAdmin();
    }

}
