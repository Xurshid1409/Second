package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import second.education.model.request.UpdateAppStatus;
import second.education.model.request.UpdateDiplomaStatus;
import second.education.model.response.*;
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
            @RequestParam(value = "status", defaultValue = "0", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size, Principal principal) {

        Page<DiplomResponseAdmin> diplomas = universityAdminService.getDiplomas(principal, status, page, size);
        return ResponseEntity.ok(diplomas);
    }

    @GetMapping("/getDiplom/{id}")
    public ResponseEntity<?> getDiplomById(@PathVariable Integer id, Principal principal) {

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
    }

    @GetMapping("/getAppOne/{appId}")
    public ResponseEntity<?> getAppById(@PathVariable Integer appId,
                                        Principal principal) {
        Result appById = universityAdminService.getAppById(appId, principal);
        return ResponseEntity.status(appById.isSuccess() ? 200 : 400).body(appById);
    }

    @GetMapping("/uadmininfo")
    public ResponseEntity<?> getUAdminInfo(Principal principal) {
        UAdminInfoResponse uAdmin = universityAdminService.getUAdmin(principal);
        return ResponseEntity.status(uAdmin != null ? 200 : 404).body(uAdmin);
    }

    @GetMapping("/getForeignDiploma")
    public ResponseEntity<?> getAllForeignDiploma(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "30") int size,
                                                  @RequestParam(value = "status") String status,
                                                  Principal principal) {
        Page<DiplomResponseAdmin> diplomas = universityAdminService.getForeignDiplomas(principal, status, page, size);
        return ResponseEntity.ok(diplomas);
    }

    @GetMapping("/getForeignDiplom/{id}")
    public ResponseEntity<?> getForeignDiplomById(@PathVariable Integer id, Principal principal) {
        Result diplomaById = universityAdminService.getForeignDiplomaById(id, principal);
        return ResponseEntity.ok(diplomaById);
    }

    @PutMapping("/updateDiplomaStatus/{diplomaId}")
    public ResponseEntity<?> updateDiplomaStatus(@PathVariable Integer diplomaId,
                                                 @RequestBody UpdateDiplomaStatus updateDiplomaStatus,
                                                 Principal principal) {

        Result result = universityAdminService.updateStatusDiploma(principal, updateDiplomaStatus, diplomaId);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @PutMapping("/updateAppStatus/{appId}")
    public ResponseEntity<?> updateAppStatus(@PathVariable Integer appId,
                                             @RequestBody UpdateAppStatus updateAppStatus,
                                             Principal principal) {

        Result result = universityAdminService.updateStatusApp(principal, updateAppStatus, appId);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @GetMapping("/searchApp")
    public ResponseEntity<?> searchAppByUAdmin(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
            @RequestParam(value = "status") String status,
            @RequestParam(value = "text") String text,
            Principal principal) {
        Page<AppResponse> appResponses = universityAdminService.searchAllAppByUAdmin(principal, status, text, page, size);
        return ResponseEntity.ok(appResponses);
    }

    @GetMapping("/searchDiploma")
    public ResponseEntity<?> searchDiplomaByUAdmin(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
            @RequestParam(value = "status") String status,
            @RequestParam(value = "text") String text,
            Principal principal) {
        Page<DiplomResponseAdmin> diplomResponseAdmins = universityAdminService.searchDiplomasByUAdmin(principal, status, text, page, size);
        return ResponseEntity.ok(diplomResponseAdmins);
    }

    @GetMapping("/searchForeignDiploma")
    public ResponseEntity<?> searchForeignDiplomaByUAdmin(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
            @RequestParam(value = "status") String status,
            @RequestParam(value = "text") String text,
            Principal principal) {
        Page<DiplomResponseAdmin> diplomResponseAdmins = universityAdminService.searchForeignDiplomas(principal, status, text, page, size);
        return ResponseEntity.ok(diplomResponseAdmins);
    }


}
