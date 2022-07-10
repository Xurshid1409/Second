package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.Application;
import second.education.domain.EnrolleeInfo;
import second.education.domain.classificator.EduForm;
import second.education.domain.classificator.FutureInstitution;
import second.education.domain.classificator.Language;
import second.education.model.request.ApplicationRequest;
import second.education.model.request.ApplicationStatus;
import second.education.model.response.ApplicationResponse;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final EduFormRepository eduFormRepository;
    private final LanguageRepository languageRepository;
    private final ApplicationRepository applicationRepository;
    private final EnrolleInfoRepository enrolleInfoRepository;
    private final FutureInstitutionRepository futureInstitutionRepository;

    @Transactional
    public Result createApplication(Principal principal, ApplicationRequest request) {

        try {
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
            application.setModifiedDate(LocalDateTime.now());
            applicationRepository.save(application);
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

    @Transactional
    public ApplicationResponse getApplicationByPrincipal(Principal principal) {
        EnrolleeInfo enrolleeInfo = enrolleInfoRepository.findByEnrolle(principal.getName()).get();
        Optional<ApplicationResponse> applicationResponse = applicationRepository.findByAppByPrincipal(enrolleeInfo.getId());
        return applicationResponse.orElse(null);
    }
}
