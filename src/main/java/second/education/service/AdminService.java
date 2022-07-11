package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.User;
import second.education.model.response.Result;
import second.education.repository.UserRepository;

//@Service
//@RequiredArgsConstructor
public class AdminService {

//    private final UserRepository userRepository;

    @Transactional
    public Result createInstitutionAdmin() {
        User user = new User();
        return null;
    }
}
