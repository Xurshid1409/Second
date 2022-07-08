package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import second.education.service.api.OneIdServiceApi;

@RestController
@RequestMapping("api/public")
@RequiredArgsConstructor
@SecurityRequirement(name = "second")
public class PublicController {

    private final OneIdServiceApi oneIdServiceApi;
   // private final EnrolleeService enrolleeService;

/*    @GetMapping("oneId/redirect")
    public ResponseEntity<?> OneId() {
        URI uri = oneIdServiceApi.redirectOneIdUrl();
        return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
    }*/

/*    @GetMapping("oneId/signIn/qabul.jsp")
    public ResponseEntity<?> signIn(@RequestParam(value = "code") String code) {
        Result result = enrolleeService.signUp(code);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }*/
}
