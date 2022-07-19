package second.education.service;

import lombok.RequiredArgsConstructor;
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
import second.education.repository.*;

import java.util.ArrayList;
import java.util.List;

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
            User user = new User();
            user.setPhoneNumber(request.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(request.getPinfl()));
            Role role = roleRepository.findByName(DefaultRole.ROLE_UADMIN.getMessage()).get();
            user.setRole(role);
            User saveUser = userRepository.save(user);
            AdminEntity adminEntity = new AdminEntity();
            FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
            adminEntity.setFutureInstitution(futureInstitution);
            List<University> universities = new ArrayList<>();
            request.getUniversitiesId().forEach(u -> {
                University university = universityRepository.findById(u).get();
                universities.add(university);
            });
            adminEntity.setUniversities(universities);
            adminEntity.setUser(saveUser);
            adminEntityRepository.save(adminEntity);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result updateInstitutionAdmin(UserRequest request) {

        try {
            User user = new User();
            user.setPhoneNumber(request.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(request.getPinfl()));
            User saveUser = userRepository.save(user);
            AdminEntity adminEntity = new AdminEntity();
            FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
            adminEntity.setFutureInstitution(futureInstitution);
            adminEntity.getUniversities().clear();
            List<University> universities = new ArrayList<>();
            request.getUniversitiesId().forEach(u -> {
                University university = universityRepository.findById(u).get();
                universities.add(university);
            });
            adminEntity.setUniversities(universities);
            adminEntity.setUser(saveUser);
            adminEntityRepository.save(adminEntity);
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }


}
