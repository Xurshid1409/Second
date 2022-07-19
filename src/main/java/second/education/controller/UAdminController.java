package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import second.education.model.response.DiplomaResponse;
import second.education.service.UniversityAdminService;

import java.security.Principal;


@RestController
@RequestMapping("/uadmin/")
@RequiredArgsConstructor
@SecurityRequirement(name = "second")
public class UAdminController {
    private final UniversityAdminService universityAdminService;

    @GetMapping("/getAllDiploma")
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "30") int size,Principal principal) {

        Page<DiplomaResponse> diplomas = universityAdminService.getDiplomas(principal, page, size);
        return ResponseEntity.ok(diplomas);
    }
}
