package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.AdminEntity;
import second.education.domain.Diploma;
import second.education.domain.User;
import second.education.domain.classificator.University;
import second.education.model.response.DiplomaResponse;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.*;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UniversityAdminService {

    private final DiplomaRepository diplomaRepository;
    private final FutureInstitutionRepository futureInstitutionRepository;
    private final UniversityRepository universityRepository;
    private final UserRepository userRepository;
    private final AdminEntityRepository adminEntityRepository;

    @Transactional(readOnly = true)
    public Page<DiplomaResponse> getDiplomas(Principal principal, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
        return diplomaRepository.getAllDiplomebyUAdmin(institutionId, pageable).map(DiplomaResponse::new);
    }


    @Transactional(readOnly = true)
    public Result getDiplomaById(Integer diplomaId, Principal principal) {
        AdminEntity adminEntity = adminEntityRepository.getAdminUniversity(principal.getName()).get();
        Integer institutionId = adminEntity.getUniversities().stream().map(University::getInstitutionId).findFirst().get();
        Optional<Diploma> diploma = diplomaRepository.getByIdDiplomebyUAdmin(institutionId, diplomaId);
        if (diploma.isEmpty()) {
            return new Result(ResponseMessage.NOT_FOUND.getMessage(), false);
        }

        return new Result("diploma",true,new DiplomaResponse(diploma.get()));
    }

}
