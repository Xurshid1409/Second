package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import second.education.model.response.AppResponse;
import second.education.model.response.DiplomResponseAdmin;
import second.education.model.response.DiplomaResponse;
import second.education.model.response.Result;
import second.education.service.UniversityAdminService;

import java.security.Principal;


@RestController
@RequestMapping("/uadmin")
@RequiredArgsConstructor
@SecurityRequirement(name = "second")
public class UAdminController {
    private final UniversityAdminService universityAdminService;

    @GetMapping("/getAllDiploma")
    public ResponseEntity<?> getAll(
                                @RequestParam(value = "status", defaultValue = "0",required = false) String status,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "30") int size,Principal principal) {

        Page<DiplomResponseAdmin> diplomas = universityAdminService.getDiplomas(principal, status,page, size);
        return ResponseEntity.ok(diplomas);
    }
    @GetMapping("/getDiplom/{id}")
    public ResponseEntity<?> getDiplomById(@PathVariable Integer id ,Principal principal) {

        Result diplomaById = universityAdminService.getDiplomaById(id, principal);
        return ResponseEntity.ok(diplomaById);
    }
    @GetMapping("/getAllApp")
    public ResponseEntity<?> getAllApp(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "30") int size,
                                       @RequestParam(value = "status") String status,
                                       Principal principal) {
        Page<AppResponse> allAppByUAdmin = universityAdminService.getAllAppByUAdmin(principal, status, page, size);
        return ResponseEntity.ok(allAppByUAdmin);
    }    @GetMapping("/getAppOne/{appId}")
    public ResponseEntity<?> getAppById(@PathVariable Integer appId,
                                       Principal principal) {
        Result appById = universityAdminService.getAppById(appId, principal);
        return ResponseEntity.status(appById.isSuccess()?200:400).body(appById);
    }
}
