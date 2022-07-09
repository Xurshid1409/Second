package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import second.education.model.request.ApplicationRequest;
import second.education.model.response.ApplicationResponse;
import second.education.model.response.Result;
import second.education.service.ApplicationService;

import java.security.Principal;

@RestController
@RequestMapping("api/application/")
@RequiredArgsConstructor
@SecurityRequirement(name = "second")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<?> createApplication(Principal principal, ApplicationRequest request) {
        Result result = applicationService.createApplication(principal, request);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

    @PutMapping
    public ResponseEntity<?> updateApplication(Principal principal, ApplicationRequest request) {
        Result result = applicationService.updateApplication(principal, request);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @GetMapping
    public ResponseEntity<?> getApplicationPrincipal(Principal principal) {
        ApplicationResponse applicationResponse = applicationService.getApplicationByPrincipal(principal);
        return ResponseEntity.ok(applicationResponse);
    }

}
