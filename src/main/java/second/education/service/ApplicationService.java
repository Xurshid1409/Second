package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.Application;
import second.education.domain.EnrolleeInfo;
import second.education.domain.StoryMessage;
import second.education.domain.classificator.EduForm;
import second.education.domain.classificator.FutureInstitution;
import second.education.domain.classificator.Language;
import second.education.model.request.ApplicationRequest;
import second.education.model.request.ApplicationStatus;
import second.education.model.response.ApplicationResponse;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.model.response.StoryMessageResponse;
import second.education.repository.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final EduFormRepository eduFormRepository;
    private final LanguageRepository languageRepository;
    private final ApplicationRepository applicationRepository;
    private final EnrolleInfoRepository enrolleInfoRepository;
    private final FutureInstitutionRepository futureInstitutionRepository;
    private final StoryMessageRepository storyMessageRepository;

    @Transactional
    public Result createApplication(Principal principal, ApplicationRequest request) {

        try {
            Optional<Application> checkApp = applicationRepository.checkApp(principal.getName());
            if (checkApp.isEmpty()) {
                Application application = new Application();
                EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByEnrolle(principal.getName()).get();
                application.setEnrolleeInfo(enrolleeInfo);
                Language language = languageRepository.findById(request.getLanguageId()).get();
                application.setLanguage(language);
                EduForm eduForm = eduFormRepository.findById(request.getEduFormId()).get();
                application.setEduForm(eduForm);
                FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstitutionId()).get();
                application.setFutureInstitution(futureInstitution);
                application.setStatus(ApplicationStatus.DEFAULT_STATUS.getMessage());
                applicationRepository.save(application);
                return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
            }
            return new Result(ResponseMessage.ALREADY_EXISTS.getMessage(), false);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result updateApplication(Principal principal, ApplicationRequest request) {
        try {
            EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByEnrolle(principal.getName()).get();
            Application application = applicationRepository.findByEnrolleeInfoId(enrolleeInfo.getId()).get();
            application.setEnrolleeInfo(enrolleeInfo);
            Language language = languageRepository.findById(request.getLanguageId()).get();
            application.setLanguage(language);
            EduForm eduForm = eduFormRepository.findById(request.getEduFormId()).get();
            application.setEduForm(eduForm);
            FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstitutionId()).get();
            application.setFutureInstitution(futureInstitution);
            application.setStatus(ApplicationStatus.DEFAULT_STATUS.getMessage());
            application.setMessage(null);
            application.setModifiedDate(LocalDateTime.now());
            Application save = applicationRepository.save(application);

            StoryMessage storyMessage = new StoryMessage();
            storyMessage.setMessage(save.getMessage());
            storyMessage.setStatus(save.getStatus());
            storyMessage.setPinfl(principal.getName());
            storyMessage.setFirstname(enrolleeInfo.getFirstname());
            storyMessage.setLastname(enrolleeInfo.getLastname());
            storyMessage.setApplication(save);
            storyMessageRepository.save(storyMessage);

            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

    @Transactional
    public ApplicationResponse getApplicationByPrincipal(Principal principal) {
        Optional<Application> checkApp = applicationRepository.checkApp(principal.getName());
        if (checkApp.isEmpty()) {
            ApplicationResponse applicationResponse = null;
            return applicationResponse;
        }
        EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByEnrolle(principal.getName()).get();
        Optional<ApplicationResponse> applicationResponse = applicationRepository.findByAppByPrincipal(enrolleeInfo.getId());

     /*   List<StoryMessage> messages = storyMessageRepository.getAllStoryByAppId(checkApp.get().getId());
        if (messages.size() > 0) {
            messages.forEach(storyMessage -> {
                StoryMessageResponse storyMessageResponse = new StoryMessageResponse();
                if (storyMessage.getStatus().equals("null") || storyMessage.getStatus().equals("true") || storyMessage.getStatus().equals("false")) {
                    storyMessageResponse.setDiplomMessage(storyMessage.getMessage());
                    storyMessageResponse.setDiplomaStatus(storyMessage.getStatus());
                } else {
                    storyMessageResponse.setAppMessage(storyMessage.getMessage());
                    storyMessageResponse.setAppStatus(storyMessage.getStatus());
                }
                applicationResponse.get().getStoryMessageResponse().add(storyMessageResponse);
            });
        }*/
        return applicationResponse.get();
    }
}
