package second.education.service;

/*import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.api_model.one_id.OneIdResponseToken;
import second.education.api_model.one_id.OneIdResponseUserInfo;
import second.education.domain.User;
import second.education.model.response.ResponseMessage;
import second.education.model.response.JwtResponse;
import second.education.model.response.Result;
import second.education.repository.UserRepository;
import second.education.security.JwtTokenProvider;
import second.education.security.UserDetailsImpl;
import second.education.service.api.OneIdServiceApi;
import java.util.List;
import java.util.Optional;*/

/*
@Service
*/
// @RequiredArgsConstructor
public class EnrolleeService {
/*
    private final OneIdServiceApi oneIdServiceApi;
    private final UserRepository userRepository;
  //  private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Result signUp(String code) {

        try {
            OneIdResponseToken token = oneIdServiceApi.getAccessAndRefreshToken(code);
            if (token == null) {
                return new Result("Authorization Token " + ResponseMessage.NOT_FOUND.getMessage(), false);
            }
            OneIdResponseUserInfo userInfo = oneIdServiceApi.getUserInfo(token.getAccess_token());
            if (userInfo == null) {
                return new Result("OneId platformasidan ro'xatdan o'ting", false);
            }
            Optional<User> userOptional = userRepository.findByPhoneNumber(userInfo.getPin());
            if (userOptional.isEmpty()) {
                User user = new User();
                user.setPhoneNumber(userInfo.getMobPhoneNo());
           //     user.setPassword(passwordEncoder.encode(userInfo.getPin()));
                user.setRoles(roleService.create());
                userRepository.save(user);
                return getResultAndToken(user);
            } else {
                return getResultAndToken(userOptional.get());
            }
        }
        catch (Exception e) {
            return new Result("Authorization code " + ResponseMessage.NOT_FOUND.getMessage(), false);
        }
    }

    @Transactional
    public Result signUpAdmin(String code) {

        try {
            OneIdResponseToken token = oneIdServiceApi.getAccessAndRefreshToken(code);
            if (token == null) {
                return new Result("Authorization Token " + ResponseMessage.NOT_FOUND.getMessage(), false);
            }
            OneIdResponseUserInfo userInfo = oneIdServiceApi.getUserInfo(token.getAccess_token());
            if (userInfo == null) {
                return new Result("OneId platformasidan ro'xatdan o'ting", false);
            }
            User user = userRepository.findByPhoneNumber(userInfo.getPin()).orElseThrow(
                    () -> new RuntimeException(
                            String.format("Foydalanuvchi %s ", userInfo.getPin() + "" + ResponseMessage.NOT_FOUND)));
            return getResultAndToken(user);
        }
        catch (Exception e) {
            return new Result("Authorization code " + ResponseMessage.NOT_FOUND.getMessage(), false);
        }
    }

    private Result getResultAndToken(User user) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getPhoneNumber(), user.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        String jwtToken = jwtTokenProvider.generateJWTToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        JwtResponse jwtResponse = new JwtResponse(userDetails.getId() , userDetails.getUsername(), jwtToken, roles);
        return new Result(ResponseMessage.SUCCESSFULLY.getMessage(), true, jwtResponse);
    }*/
}
