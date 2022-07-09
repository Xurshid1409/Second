package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.Application;
import second.education.domain.EnrolleeInfo;
import second.education.domain.classificator.EduForm;
import second.education.domain.classificator.Language;
import second.education.model.request.ApplicationRequest;
import second.education.model.request.ApplicationStatus;
import second.education.model.response.ApplicationResponse;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.*;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final LanguageRepository languageRepository;
    private final EduFormRepository eduFormRepository;
    private final ApplicationRepository applicationRepository;
    private final EnrolleInfoRepository enrolleInfoRepository;

    @Transactional
    public Result createApplication(Principal principal, ApplicationRequest request) {

        try {
            EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByUser(principal.getName()).get();
            Application application = new Application();
            Language language = languageRepository.findById(request.getLanguageId()).get();
            application.setLanguage(language);
            EduForm eduForm = eduFormRepository.findById(request.getEduFormId()).get();
            application.setEduForm(eduForm);
            application.setEnrolleeInfo(enrolleeInfo);
            application.setStatus(ApplicationStatus.DEFAULT_STATUS.getMessage());
            applicationRepository.save(application);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result updateApplication(Principal principal, ApplicationRequest request) {

        try {
            EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByUser(principal.getName()).get();
            Application application = applicationRepository.findByEnrolleeInfoId(enrolleeInfo.getId()).get();
            Language language = languageRepository.findById(request.getLanguageId()).get();
            application.setLanguage(language);
            EduForm eduForm = eduFormRepository.findById(request.getEduFormId()).get();
            application.setEduForm(eduForm);
            application.setEnrolleeInfo(enrolleeInfo);
            application.setStatus(ApplicationStatus.DEFAULT_STATUS.getMessage());
            applicationRepository.save(application);
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

    public ApplicationResponse getApplicationByPrincipal(Principal principal) {
        EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByUser(principal.getName()).get();
        return applicationRepository.findByAppByPrincipal(enrolleeInfo.getId()).get();
    }
}
