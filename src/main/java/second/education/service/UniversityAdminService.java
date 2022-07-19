package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import second.education.model.response.DiplomaResponse;
import second.education.repository.DiplomaRepository;
import second.education.repository.FutureInstitutionRepository;
import second.education.repository.UniversityRepository;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UniversityAdminService {

    private final DiplomaRepository diplomaRepository;
    private final FutureInstitutionRepository futureInstitutionRepository;
    private final UniversityRepository universityRepository;

    public Page<DiplomaResponse> getDiplomas(Principal principal) {
        return null;
    }
}
