package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.User;
import second.education.domain.classificator.FutureInstitution;
import second.education.domain.classificator.Role;
import second.education.model.request.UserRequest;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.FutureInstitutionRepository;
import second.education.repository.RoleRepository;
import second.education.repository.UserRepository;

//@Service
//@RequiredArgsConstructor
public class AdminService {

//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final RoleRepository roleRepository;
//    private final FutureInstitutionRepository futureInstitutionRepository;
//
//    @Transactional
//    public Result createInstitutionAdmin(UserRequest request) {
//
//        try {
//            User user = new User();
//            user.setPhoneNumber(request.getPhoneNumber());
//            user.setPassword(passwordEncoder.encode(request.getPassword()));
//            Role role = roleRepository.findById(request.getRoleId()).get();
//            user.setRole(role);
//            FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
//            user.setFutureInstitution(futureInstitution);
//            userRepository.save(user);
//            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
//        } catch (Exception ex) {
//            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
//        }
//    }

//    @Transactional
//    public Result updateInstitutionAdmin(Integer userId, UserRequest request) {
//
//        try {
//            User user = userRepository.findById(userId).get();
//            user.setPhoneNumber(request.getPhoneNumber());
//            user.setPassword(passwordEncoder.encode(request.getPassword()));
//            Role role = roleRepository.findById(request.getRoleId()).get();
//            user.setRole(role);
//            FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
//            user.setFutureInstitution(futureInstitution);
//            userRepository.save(user);
//            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
//        } catch (Exception ex) {
//            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
//        }
//    }


}
