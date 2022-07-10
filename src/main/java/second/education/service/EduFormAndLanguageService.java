package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.classificator.Direction;
import second.education.domain.classificator.EduForm;
import second.education.domain.classificator.Language;
import second.education.model.request.EduFormRequest;
import second.education.model.request.LanguageRequest;
import second.education.model.response.EduFormResponse;
import second.education.model.response.LanguageResponse;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.DirectionRepository;
import second.education.repository.EduFormRepository;
import second.education.repository.LanguageRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EduFormAndLanguageService {

    private final EduFormRepository eduFormRepository;
    private final LanguageRepository languageRepository;
    private final DirectionRepository directionRepository;

    @Transactional
    public Result createEduForm(EduFormRequest request) {

        try {
            EduForm eduForm = new EduForm();
            eduForm.setName(request.getName());
            Direction direction = directionRepository.findById(request.getDirectionId()).get();
            eduForm.setDirection(direction);
            eduFormRepository.save(eduForm);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result updateEduForm(int eduFormId, EduFormRequest request) {
        try {
            EduForm eduForm = eduFormRepository.findById(eduFormId).get();
            eduForm.setName(request.getName());
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

//    public List<EduFormResponse> getAllEduForm(Integer directionId) {
//        return eduFormRepository.findAllByDirectionId(directionId).stream().map(EduFormResponse::new).toList();
//    }

    public EduFormResponse getEduFormById(int eduFormId) {
        try {
            EduForm eduForm = eduFormRepository.findById(eduFormId).get();
            return new EduFormResponse(eduForm);
        } catch (Exception ex) {
            return new EduFormResponse();
        }
    }

    public Result deleteEduForm(int eduFormId) {
        try {
            eduFormRepository.deleteById(eduFormId);
            return new Result(ResponseMessage.SUCCESSFULLY_DELETED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_DELETED.getMessage(), false);
        }
    }

    @Transactional
    public Result createLanguage(LanguageRequest request) {
        try {
            Language language = new Language();
            language.setLanguage(request.getName());
            languageRepository.save(language);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result updateLanguage(int languageId, LanguageRequest request) {
        try {
            Language language = languageRepository.findById(languageId).get();
            language.setLanguage(request.getName());
            languageRepository.save(language);
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

    public List<LanguageResponse> getAllLanguages(Integer directionId) {
        return languageRepository.findAllByDirectionId(directionId).stream().map(LanguageResponse::new).toList();
    }

    public LanguageResponse getLanguageById(int languageId) {
        try {
            Language language = languageRepository.findById(languageId).get();
            return new LanguageResponse(language);
        } catch (Exception ex) {
            return new LanguageResponse();
        }
    }

    public Result deleteLanguage(int languageId) {
        try {
            languageRepository.deleteById(languageId);
            return new Result(ResponseMessage.SUCCESSFULLY_DELETED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_DELETED.getMessage(), false);
        }
    }
}
