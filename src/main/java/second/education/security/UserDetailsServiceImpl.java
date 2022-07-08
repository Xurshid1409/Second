package second.education.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import second.education.domain.User;
import second.education.model.response.ResponseMessage;
import second.education.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new UsernameNotFoundException("User with:" + phoneNumber + " " + ResponseMessage.NOT_FOUND.getMessage()));
        return UserDetailsImpl.build(user);
    }
}
