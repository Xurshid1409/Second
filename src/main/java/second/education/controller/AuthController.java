package second.education.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import second.education.model.request.IIBRequest;
import second.education.model.request.LoginRequest;
import second.education.model.request.ValidateCodeRequest;
import second.education.model.response.JwtResponse;
import second.education.model.response.Result;
import second.education.security.JwtTokenProvider;
import second.education.security.UserDetailsImpl;
import second.education.service.AuthService;
import second.education.service.api.OneIdServiceApi;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final OneIdServiceApi oneIdServiceApi;

    @PostMapping("/checkUser")
    public ResponseEntity<?> checkUser(@RequestBody IIBRequest iibRequest) {
        Result result = authService.checkUser(iibRequest);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @PostMapping("/validateUser")
    public ResponseEntity<?> validateUser(@RequestBody ValidateCodeRequest request) {
        Result result = authService.validateUser(request);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest){

            try {
                Authentication authenticate = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getPhoneNumber(), loginRequest.getPassword()));
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
            } catch (Exception ex) {
                return ResponseEntity.status(400).body(new Result("Telefon raqam yoki parol hato kiritilgan, " +
                        "iltimos tekshirib qayta urinib ko'ring", false));
            }
    }

    @PostMapping("update/code")
    public ResponseEntity<?> updateCode(@RequestParam(value = "phoneNumber") String phoneNumber) {
        Result result = authService.updateCheckCode(phoneNumber);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @PostMapping("validate/code")
    public ResponseEntity<?> validateCode(@RequestBody ValidateCodeRequest request) {
        Result result = authService.validateCheckCode(request);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @GetMapping("/oneId")
    public ResponseEntity<?> getOneId() {
        URI uri = oneIdServiceApi.redirectOneIdUrl();
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(uri)
                .build();
    }

    @PostMapping("/oneId/signIn")
    public ResponseEntity<?> signIn(@RequestParam(value = "code") String code) {
        Result result = authService.validateUsersOneId(code);
        return ResponseEntity.status(result.isSuccess() ? 200 : 404).body(result);
    }
}
