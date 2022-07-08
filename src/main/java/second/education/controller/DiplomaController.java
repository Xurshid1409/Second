package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import second.education.model.response.Result;
import second.education.model.response.SpecialitiesResponse;
import second.education.model.response.UniversityResponse;
import second.education.service.ApplicationService;
import second.education.service.api.DiplomaApi;

import java.util.List;

@RestController
@RequestMapping("diploma/")
@RequiredArgsConstructor
@SecurityRequirement(name = "second")
public class DiplomaController {

    private final DiplomaApi diplomaApi;
    private final ApplicationService applicationService;

 /*   @GetMapping("/create/institution")
    public ResponseEntity<?> createInstitution() {
        Result result = diplomaApi.saveInstitution();
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }*/


    @GetMapping("getUniversities")
    public ResponseEntity<?> getUniversities() {
        List<UniversityResponse> universities = diplomaApi.getUniversities();
        return ResponseEntity.status(!universities.isEmpty() ? 200 : 404).body(universities);
    }

    @GetMapping("getSpecialities/{universityId}")
    public ResponseEntity<?> getSpecialities(@PathVariable int universityId) {
        List<SpecialitiesResponse> specialities = diplomaApi.getSpecialitiesByUniversityId(universityId);
        return ResponseEntity.status(!specialities.isEmpty() ? 200 : 404).body(specialities);
    }

 /*   @PostMapping("saveDiploma")
    public ResponseEntity<?> createDiploma(@RequestParam(value = "pinfl") String pinfl) {
        Result result = applicationService.saveDiploma(pinfl);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }*/


}
