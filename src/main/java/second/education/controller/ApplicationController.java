package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import second.education.model.request.ApplicationRequest;
import second.education.model.response.*;
import second.education.service.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/application/")
@RequiredArgsConstructor
@SecurityRequirement(name = "second")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final FutureInstitutionService futureInstitutionService;
    private final DirectionService directionService;
    private final EduFormService eduFormService;

    @PostMapping
    public ResponseEntity<?> createApplication(Principal principal, @RequestBody ApplicationRequest request) {
        Result result = applicationService.createApplication(principal, request);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

    @PutMapping
    public ResponseEntity<?> updateApplication(Principal principal, @RequestBody ApplicationRequest request) {
        Result result = applicationService.updateApplication(principal, request);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @GetMapping
    public ResponseEntity<?> getApplicationPrincipal(Principal principal) {
        ApplicationResponse applicationResponse = applicationService.getApplicationByPrincipal(principal);
        return ResponseEntity.ok(applicationResponse);
    }

    @GetMapping("futureInstitution")
    public ResponseEntity<?> getAllFutureInstitution() {
        List<FutureInstitutionResponse> allFutureInstitution = futureInstitutionService.getAllFutureInstitution();
        return ResponseEntity.ok(allFutureInstitution);
    }

    @GetMapping("futureInstitution/{futureInstId}")
    public ResponseEntity<?> getFutureInstitutionById(@PathVariable int futureInstId) {
        FutureInstitutionResponse futureInstitutionById = futureInstitutionService.getFutureInstitutionById(futureInstId);
        return ResponseEntity.ok(futureInstitutionById);
    }

    @GetMapping("direction/futureInstitution/{futureInstitutionId}")
    public ResponseEntity<?> getAllDirection(@PathVariable int futureInstitutionId) {
        List<DirectionResponse> allDirection = directionService.getAllDirectionByFutureInst(futureInstitutionId);
        return ResponseEntity.ok(allDirection);
    }

    @GetMapping("direction/{directionId}")
    public ResponseEntity<?> getDirectionById(@PathVariable int directionId) {
        DirectionResponse directionById = directionService.getDirectionById(directionId);
        return ResponseEntity.ok(directionById);
    }

    @GetMapping("eduForm/direction/{directionId}")
    public ResponseEntity<?> getEduFormByDirection(@PathVariable int directionId) {
        List<EduFormResponse> allEduForm = eduFormService.getAllEduFormByDirection(directionId);
        return ResponseEntity.ok(allEduForm);
    }

    @GetMapping("eduForm/{eduFormId}")
    public ResponseEntity<?> getEduFormById(@PathVariable int eduFormId) {
        EduFormResponse eduFormById = eduFormService.getEduFormResponse(eduFormId);
        return ResponseEntity.ok(eduFormById);
    }

}
