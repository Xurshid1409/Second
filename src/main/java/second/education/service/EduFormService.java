package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.classificator.Direction;
import second.education.domain.classificator.EduForm;
import second.education.domain.classificator.Language;
import second.education.model.request.EduFormRequest;
import second.education.model.response.EduFormResponse;
import second.education.model.response.LanguageResponse;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.DirectionRepository;
import second.education.repository.EduFormRepository;
import second.education.repository.LanguageRepository;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EduFormService {

    private final DirectionRepository directionRepository;
    private final EduFormRepository eduFormRepository;
    private final LanguageRepository languageRepository;

    @Transactional
    public Result createEduForm(EduFormRequest request) {
       try {
           EduForm eduForm = new EduForm();
           eduForm.setName(request.getName());
           Direction direction = directionRepository.findById(request.getDirectionId()).get();
           eduForm.setDirection(direction);
           EduForm saveEduform = eduFormRepository.save(eduForm);
           createLanguageAndQuota(request, saveEduform);
           return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
       } catch (Exception ex) {
           return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
       }
    }

    @Transactional
    public Result updateEduForm(EduFormRequest request) {
        try {
            EduForm eduForm = eduFormRepository.findById(request.getId()).get();
            eduForm.setName(request.getName());
            Direction direction = directionRepository.findById(request.getDirectionId()).get();
            eduForm.setDirection(direction);
            EduForm saveEduform = eduFormRepository.save(eduForm);
            List<Language> languages = new ArrayList<>();
            request.getLanguages().forEach(e -> {
                Language language = languageRepository.findById(e.getId()).get();
                language.setLanguage(e.getName());
                language.setKvotaSoni(e.getKvota());
                language.setEduForm(saveEduform);
                languages.add(language);
            });
            languageRepository.saveAll(languages);
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

    private void createLanguageAndQuota(EduFormRequest request, EduForm saveEduform) {
        List<Language> languages = new ArrayList<>();
        request.getLanguages().forEach(l -> {
            Language language = new Language();
            language.setLanguage(l.getName());
            language.setKvotaSoni(l.getKvota());
            language.setEduForm(saveEduform);
            languages.add(language);
        });
        languageRepository.saveAll(languages);
    }

    @Transactional(readOnly = true)
    public EduFormResponse eduFormResponse(Integer eduFormId) {
        try {
            EduForm eduForm = eduFormRepository.findById(eduFormId).get();
            List<LanguageResponse> languageResponses = eduFormRepository.findAllLanguageByEduForm(eduFormId)
                    .stream().map(LanguageResponse::new).toList();
            return new EduFormResponse(eduForm, languageResponses);
        } catch (Exception ex) {
            return new EduFormResponse();
        }
    }

    @Transactional(readOnly = true)
    public List<EduFormResponse> getAllEduFormResponse() {
        List<EduFormResponse> eduFormResponses = eduFormRepository.findAll()
                .stream().map(EduFormResponse::new).toList();
        eduFormResponses.forEach(e -> {
            EduForm eduForm = eduFormRepository.findById(e.getId()).get();
            List<LanguageResponse> languageResponses = eduFormRepository.findAllLanguageByEduForm(eduForm.getId())
                    .stream().map(LanguageResponse::new).toList();
            e.setLanguages(languageResponses);
        });
        return eduFormResponses;
    }
}