package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.AdminEntity;
import second.education.domain.User;
import second.education.domain.classificator.FutureInstitution;
import second.education.domain.classificator.Role;
import second.education.domain.classificator.University;
import second.education.model.request.DefaultRole;
import second.education.model.request.UserRequest;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.model.response.UniversityResponse;
import second.education.model.response.UAdminResponse;
import second.education.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final FutureInstitutionRepository futureInstitutionRepository;
    private final AdminEntityRepository adminEntityRepository;
    private final UniversityRepository universityRepository;

    @Transactional
    public Result createInstitutionAdmin(UserRequest request) {

        try {
            Optional<User> byPhoneNumber = userRepository.findByPhoneNumber(request.getPinfl());
            if (byPhoneNumber.isEmpty()) {
                User user = new User();
                user.setPhoneNumber(request.getPinfl());
                user.setPassword(passwordEncoder.encode(request.getPinfl()));
                Role role = roleRepository.findByName(DefaultRole.ROLE_UADMIN.getMessage()).get();
                user.setRole(role);
                User saveUser = userRepository.save(user);
                AdminEntity adminEntity = new AdminEntity();
                FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
                adminEntity.setFutureInstitution(futureInstitution);
                List<University> universities = universityRepository.findAllByInstitutionId(request.getInstitutionId());
                adminEntity.setUniversities(universities);
                adminEntity.setUser(saveUser);
                adminEntityRepository.save(adminEntity);
                return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
            }
            return new Result(ResponseMessage.ALREADY_EXISTS.getMessage(), false);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result updateInstitutionAdmin(Integer adminEntityId, UserRequest request) {

        try {
            AdminEntity adminEntity = adminEntityRepository.findById(adminEntityId).get();
            Optional<User> byPhoneNumber = userRepository.findByPhoneNumber(request.getPinfl());
            if (byPhoneNumber.isPresent()) {
                if (adminEntityId.equals(byPhoneNumber.get().getId())) {
                    byPhoneNumber.get().setPhoneNumber(request.getPinfl());
                    byPhoneNumber.get().setPassword(passwordEncoder.encode(request.getPinfl()));
                    User save = userRepository.save(byPhoneNumber.get());
                    FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
                    adminEntity.setFutureInstitution(futureInstitution);
                    adminEntity.getUniversities().clear();
                    List<University> universities = universityRepository.findAllByInstitutionId(request.getInstitutionId());
                    adminEntity.setUniversities(universities);
                    adminEntity.setUser(save);
                    adminEntityRepository.save(adminEntity);
                    return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
                }
                return new Result("bu pinfl oldin qo'shilgan", false);
            }
            byPhoneNumber.get().setPhoneNumber(request.getPinfl());
            byPhoneNumber.get().setPassword(passwordEncoder.encode(request.getPinfl()));
            User save = userRepository.save(byPhoneNumber.get());
            FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
            adminEntity.setFutureInstitution(futureInstitution);
            adminEntity.getUniversities().clear();
            List<University> universities = universityRepository.findAllByInstitutionId(request.getInstitutionId());
            adminEntity.setUniversities(universities);
            adminEntity.setUser(save);
            adminEntityRepository.save(adminEntity);
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

    @Transactional(readOnly = true)
    public Page<UAdminResponse> getUAdmins(int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<AdminEntity> allAdmins = adminEntityRepository.getAllAdmins(pageable);
        List<UAdminResponse> uAdminResponses = new ArrayList<>();
        allAdmins.forEach(adminEntity -> {
            UAdminResponse uAdminResponse = new UAdminResponse();
            uAdminResponse.setId(adminEntity.getId());
            uAdminResponse.setPinfl(adminEntity.getUser().getPhoneNumber());
            uAdminResponse.setFutureInstId(adminEntity.getFutureInstitution().getId());
            uAdminResponse.setFutureInstName(adminEntity.getFutureInstitution().getName());
            uAdminResponse.setUniversityResponses(adminEntity.getUniversities().stream().map(UniversityResponse::new).toList());
            uAdminResponses.add(uAdminResponse);
        });
        return new PageImpl<>(uAdminResponses, pageable, allAdmins.getTotalElements());
    }

    @Transactional(readOnly = true)
    public UAdminResponse getUAdminById(int adminEntityId) {

        AdminEntity adminEntity = adminEntityRepository.getAdminById(adminEntityId).get();
            UAdminResponse uAdminResponse = new UAdminResponse();
            uAdminResponse.setId(uAdminResponse.getId());
            uAdminResponse.setPinfl(adminEntity.getUser().getPhoneNumber());
            uAdminResponse.setFutureInstId(adminEntity.getFutureInstitution().getId());
            uAdminResponse.setFutureInstName(adminEntity.getFutureInstitution().getName());
            uAdminResponse.setUniversityResponses(adminEntity.getUniversities().stream().map(UniversityResponse::new).toList());
           return uAdminResponse;
    }
}
