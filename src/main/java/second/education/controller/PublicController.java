package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import second.education.model.response.FutureInstitutionResponse;
import second.education.model.response.UniversityResponse;
import second.education.service.FutureInstitutionService;
import second.education.service.api.DiplomaApi;
import second.education.service.api.OneIdServiceApi;

import java.util.List;

@RestController
@RequestMapping("api/public")
@RequiredArgsConstructor
public class PublicController {

    private final DiplomaApi diplomaApi;
    private final FutureInstitutionService futureInstitutionService;

    @GetMapping("getUniversities")
    public ResponseEntity<?> getUniversities() {
        List<UniversityResponse> universities = diplomaApi.getUniversities();
        return ResponseEntity.status(!universities.isEmpty() ? 200 : 404).body(universities);
    }

    @GetMapping("futureInstitution")
    public ResponseEntity<?> getAllFutureInstitution() {
        List<FutureInstitutionResponse> allFutureInstitution = futureInstitutionService.getAllFutureInstitution();
        return ResponseEntity.ok(allFutureInstitution);
    }

}
