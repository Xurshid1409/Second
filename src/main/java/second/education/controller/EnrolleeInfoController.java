package second.education.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import second.education.model.request.DiplomaRequest;
import second.education.model.response.DiplomaResponse;
import second.education.model.response.EnrolleeResponse;
import second.education.model.response.Result;
import second.education.service.EnrolleeService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/enrolleeInfo/")
@RequiredArgsConstructor
public class EnrolleeInfoController {

    private final EnrolleeService enrolleeService;

    @GetMapping
    public ResponseEntity<?> getEnrolleInfo(Principal principal) {
        EnrolleeResponse enrolleeResponse = enrolleeService.getEnrolleeResponse(principal);
        return ResponseEntity.ok(enrolleeResponse);
    }

    @PostMapping
    public ResponseEntity<?> createDiploma(Principal principal, @RequestBody DiplomaRequest diplomaRequest) {
        Result result = enrolleeService.createDiploma(principal, diplomaRequest);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

    @PutMapping
    public ResponseEntity<?> updateDiploma(Principal principal, @RequestBody DiplomaRequest diplomaRequest) {
        Result result = enrolleeService.updateDiploma(principal, diplomaRequest);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @GetMapping("diplomas")
    public ResponseEntity<?> getDiplomas(Principal principal) {
        List<DiplomaResponse> diplomasByEnrolleeInfo = enrolleeService.getDiplomasByEnrolleeInfo(principal);
        return ResponseEntity.ok(diplomasByEnrolleeInfo);
    }

    @GetMapping("diplomas/{diplomaId}")
    public ResponseEntity<?> getDiplomaById(Principal principal, @PathVariable int diplomaId) {
        DiplomaResponse diplomaByIdAndEnrolleInfo = enrolleeService.getDiplomaByIdAndEnrolleInfo(principal, diplomaId);
        return ResponseEntity.ok(diplomaByIdAndEnrolleInfo);
    }
}
