package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import second.education.model.response.GetStatisByDirection;
import second.education.model.response.GetStatisByEduForm;
import second.education.model.response.GetStatisByLanguage;
import second.education.repository.ApplicationRepository;
import second.education.repository.DirectionRepository;
import second.education.repository.EduFormRepository;
import second.education.repository.LanguageRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {

    private final ApplicationRepository applicationRepository;
    private final DirectionRepository directionRepository;
    private final EduFormRepository eduFormRepository;
    private final LanguageRepository languageRepository;

    public List<GetStatisByDirection> getStatisByDirections(Integer futureInstId) {
        List<GetStatisByDirection> directionList = directionRepository
                .getAllByFutureInstitutionId(futureInstId);
        directionList.forEach(d -> {
            List<GetStatisByEduForm> eduForms = eduFormRepository.findAllByDirectionId(d.getDirectionId());
            eduForms.forEach(eduForm -> {
                List<GetStatisByLanguage> languages = languageRepository.findAllByEduFormId(eduForm.getEduFormId());
                languages.forEach(l -> {
                    eduForm.getStatisByLanguage().add(l);
                });
                d.getStatisByEduForm().add(eduForm);
            });
        });
        return directionList;
    }
}
