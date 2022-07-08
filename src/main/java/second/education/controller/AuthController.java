package second.education.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import second.education.model.request.IIBRequest;
import second.education.model.request.LoginRequest;
import second.education.model.response.JwtResponse;
import second.education.model.response.Result;
import second.education.security.JwtTokenProvider;
import second.education.security.UserDetailsImpl;
import second.education.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/checkUser")
    public ResponseEntity<?> checkUser(@RequestBody IIBRequest iibRequest) {
        Result result = authService.checkUser(iibRequest);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @PostMapping("/validateUser")
    public ResponseEntity<?> validateUser(@RequestParam(value = "phoneNumber") String phoneNumber,
                                          @RequestParam(value = "code") String code) {
        Result result = authService.validateUser(phoneNumber, code);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest){

            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getPhoneNumber(),loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
            String jwtToken = jwtTokenProvider.generateToken(userDetails);
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            return ResponseEntity.ok(new JwtResponse(
                    userDetails.getId(),
                    userDetails.getUsername(),
                    jwtToken,
                    roles
            ));
    }
}
