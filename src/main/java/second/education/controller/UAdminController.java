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
import java.util.List;


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

    @GetMapping("/getAllAppByDiplomaStatusAndAppStatus")
    public ResponseEntity<?> getAllAppByDiplomaStatus(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "30") int size,
                                                      @RequestParam(value = "appStatus") String appStatus,
                                                      @RequestParam(value = "diplomaStatus") String diplomaStatus,
                                                      Principal principal) {
        Page<AppResponse> allAppByUAdmin = universityAdminService.getAllAppDplomaStatusByUAdmin(principal, diplomaStatus, appStatus, page, size);
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
    @PutMapping("/cancelDiplomabyAcceptD")
    public ResponseEntity<?> updateDiplomStatusbyApp(@PathVariable Integer diplomaId,
                                                 @RequestBody UpdateDiplomaStatus updateDiplomaStatus,
                                                 Principal principal) {

        Result result = universityAdminService.updateDiplomStatusbyApp(principal, updateDiplomaStatus, diplomaId);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @PutMapping("/updateForeignDiplomaStatus/{diplomaId}")
    public ResponseEntity<?> updateForeignDiplomaStatus(@PathVariable Integer diplomaId,
                                                        @RequestBody UpdateDiplomaStatus updateDiplomaStatus,
                                                        Principal principal) {

        Result result = universityAdminService.updateStatusForeignDiploma(principal, updateDiplomaStatus, diplomaId);
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

    @GetMapping("/searchAppByDiplomaStatus")
    public ResponseEntity<?> searchAppByDiplomaStatus(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
            @RequestParam(value = "appStatus") String appStatus,
            @RequestParam(value = "diplomaStatus") String diplomaStatus,
            @RequestParam(value = "text") String text,
            Principal principal) {
        Page<AppResponse> appResponses = universityAdminService.searchAllAppByStatus(principal, diplomaStatus, appStatus, text, page, size);
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

    @GetMapping("/countAll")
    public ResponseEntity<?> countAll(Principal principal) {
        CountByUAdmin allCountByUAdmin = universityAdminService.getAllCountByUAdmin(principal);
        return ResponseEntity.ok(allCountByUAdmin);
    }


    @GetMapping("/countAppAndToday")
    public ResponseEntity<?> countAppAndToday(Principal principal) {
        List<GetCountAppallDate> countAppandTodayByUAdmin = universityAdminService.getCountAppandTodayByUAdmin(principal);
        return ResponseEntity.ok(countAppandTodayByUAdmin);
    }

    @GetMapping("/countAppAndGender")
    public ResponseEntity<?> countAppAndGender(Principal principal) {
        List<GetAppByGender> countAppandTodayByUAdmin = universityAdminService.getCountAppandGenderByUAdmin(principal);
        return ResponseEntity.ok(countAppandTodayByUAdmin);
    }

    @GetMapping("/countDilomaAndForegnToday")
    public ResponseEntity<?> countDilomaAndForegnToday(Principal principal) {
        CountGenderAndDiplomaAndApp all = universityAdminService.getCountForeignAndDiplomaandGenderAndTodayByUAdmin(principal);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/exportDiploma")
    public ResponseEntity<?> exportDiploma(Principal principal, @RequestParam(value = "status") String status) {
        List<GetDiplomasToExcel> diplomasToExcel = universityAdminService.getDiplomasToExcel(principal, status);
        return ResponseEntity.ok(diplomasToExcel);
    }

    @GetMapping("/exportForeignDiploma")
    public ResponseEntity<?> exportForeignDiploma(Principal principal, @RequestParam(value = "status") String status) {
        List<GetDiplomasToExcel> diplomasToExcel = universityAdminService.getForeignDiplomasToExcel(principal, status);
        return ResponseEntity.ok(diplomasToExcel);
    }


    @GetMapping("/exportApp")
    public ResponseEntity<?> exportApp(Principal principal, @RequestParam(value = "diplomaStatus") String diplomaStatus,
                                       @RequestParam(value = "appStatus") String appStatus) {
        List<GetAppToExcel> diplomasToExcel = universityAdminService.getAppToExcel(principal, appStatus, diplomaStatus);
        return ResponseEntity.ok(diplomasToExcel);
    }

}
