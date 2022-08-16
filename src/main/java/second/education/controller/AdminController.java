package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import second.education.model.request.*;
import second.education.model.response.*;
import second.education.service.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin/")
@RequiredArgsConstructor
@SecurityRequirement(name = "second")
public class AdminController {

    private final FutureInstitutionService futureInstitutionService;
    private final DirectionService directionService;
    private final EduFormService eduFormService;
    private final AdminService adminService;
    private final StatService statService;
    private final EnrolleeService enrolleeService;

    @PostMapping("futureInstitution")
    public ResponseEntity<?> createFutureInstitution(@RequestBody FutureInstitutionRequest request) {
        Result result = futureInstitutionService.createFutureInstitution(request);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

    @PutMapping("futureInstitution/{futureInstId}")
    public ResponseEntity<?> updateFutureInstitution(@PathVariable int futureInstId, @RequestBody FutureInstitutionRequest request) {
        Result result = futureInstitutionService.updateFutureInstitution(futureInstId, request);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
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

    //Ta'lim yo'nalishi

    @PostMapping("direction")
    public ResponseEntity<?> createDirection(@RequestBody DirectionRequest request) {
        Result result = directionService.createDirection(request);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

    @PutMapping("direction/{directionId}")
    public ResponseEntity<?> updateDirection(@PathVariable int directionId, @RequestBody DirectionRequest request) {
        Result result = directionService.updateDirection(directionId, request);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @GetMapping("direction/futureInstitution/{futureInstitutionId}")
    public ResponseEntity<?> getAllDirectionByFutureInst(@PathVariable int futureInstitutionId) {
        List<DirectionResponse> allDirection = directionService.getAllDirectionByFutureInst(futureInstitutionId);
        return ResponseEntity.ok(allDirection);
    }

    @GetMapping("direction/{directionId}")
    public ResponseEntity<?> getDirectionById(@PathVariable int directionId) {
        DirectionResponse directionById = directionService.getDirectionById(directionId);
        return ResponseEntity.ok(directionById);
    }

    @GetMapping("direction")
    public ResponseEntity<?> getAlldirection() {
        List<DirectionResponse> allDirection = directionService.getAllDirection();
        return ResponseEntity.ok(allDirection);
    }

    @PostMapping("eduForm")
    public ResponseEntity<?> createEduForm(@RequestBody EduFormRequest eduFormRequest) {
        Result result = eduFormService.createEduForm(eduFormRequest);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

    @PutMapping("eduForm/update/{eduFormId}")
    public ResponseEntity<?> updateEduForm(@PathVariable int eduFormId, @RequestBody EduFormRequest request) {
        Result result = eduFormService.updateEduForm(eduFormId, request);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @GetMapping("eduForm/{eduFormId}")
    public ResponseEntity<?> getEduFromById(@PathVariable Integer eduFormId) {
        EduFormResponse eduFormResponse = eduFormService.getEduFormResponse(eduFormId);
        return ResponseEntity.ok(eduFormResponse);
    }

    @GetMapping("eduForm")
    public ResponseEntity<?> getAllEduForm() {
        List<EduFormResponse> allEduFormResponse = eduFormService.getAllEduFormResponse();
        return ResponseEntity.ok(allEduFormResponse);
    }

    @GetMapping("eduForm/direction/{directionId}")
    public ResponseEntity<?> getEduFormByDirection(@PathVariable int directionId) {
        List<EduFormResponse> allEduFormByDirection = eduFormService.getAllEduFormByDirection(directionId);
        return ResponseEntity.ok(allEduFormByDirection);
    }

    @GetMapping("searchEduForm/{text}")
    public ResponseEntity<?> searchEduForm(@PathVariable String text,
                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "30") int size) {
        Page<EduFormResponse> search = eduFormService.search(text, page, size);
        return ResponseEntity.ok(search);
    }


    @GetMapping("/getFutureInstitutions")
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "30") int size) {
        Page<FutureInstitutionResponse> allPage = futureInstitutionService.getAllPage(page, size);
        return ResponseEntity.ok(allPage);

    }

    @GetMapping("/getDirections")
    public ResponseEntity<?> getAllDirectionPageble(@RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "30") int size) {
        Page<DirectionResponse> directionPageable = directionService.getDirectionPageable(page, size);
        return ResponseEntity.ok(directionPageable);

    }

    @GetMapping("/getEduForms")
    public ResponseEntity<?> getAllEduFormPageble(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "30") int size) {
        Page<EduFormResponse> allEduFormPage = eduFormService.getAllEduFormPage(page, size);
        return ResponseEntity.ok(allEduFormPage);
    }

    @GetMapping("/searchDirection/{text}")
    public ResponseEntity<?> searchDirection(@PathVariable String text,
                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "30") int size) {
        Page<DirectionResponse> search = directionService.search(text, page, size);
        return ResponseEntity.ok(search);
    }

    @DeleteMapping("eduForm{eduFormId}")
    public ResponseEntity<?> deleteEduForm(@PathVariable int eduFormId) {
        Result result = eduFormService.deleteEduForm(eduFormId);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @PostMapping("/uadmin")
    public ResponseEntity<?> createUAdmin(@RequestBody UserRequest request) {
        Result result = adminService.createInstitutionAdmin(request);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

    @PutMapping("update/uadmin/{adminEntityId}")
    public ResponseEntity<?> updateUAdmin(@PathVariable int adminEntityId, @RequestBody UserRequest request) {
        Result result = adminService.updateInstitutionAdmin(adminEntityId, request);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @GetMapping("/getAllUAdmins")
    public ResponseEntity<?> getAllUAdmins(@RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "30") int size) {
        Page<UAdminResponse> uAdmins = adminService.getUAdmins(page, size);
        return ResponseEntity.ok(uAdmins);
    }

    @GetMapping("/getUAdmin/{id}")
    public ResponseEntity<?> getUAdmin(@PathVariable Integer id) {
        UAdminResponse uAdminById = adminService.getUAdminById(id);
        return ResponseEntity.ok(uAdminById);
    }

    @GetMapping("/searchFutureInst")
    public ResponseEntity<?> searchFutureInst(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
            @RequestParam(value = "text", defaultValue = "0") String text) {
        Page<FutureInstitutionResponse> futureInstitutionResponses = futureInstitutionService.searchFutureInst(text, page, size);
        return ResponseEntity.ok(futureInstitutionResponses);
    }

    @GetMapping("/searchUAdmin")
    public ResponseEntity<?> searchUAdmin(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
            @RequestParam(value = "text", defaultValue = "0") String text) {
        Page<UAdminResponse> uAdminResponses = adminService.searchUAdmin(text, page, size);
        return ResponseEntity.ok(uAdminResponses);
    }

    @GetMapping("/getDiplomasToAdmin")
    public ResponseEntity<?> getDiplomasToAdmin(@RequestParam(value = "status") String status,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "30") int size) {
        Page<GetDiplomasToExcel> diplomasToExcel = adminService.getDiplomasToAdmin(status, page, size);
        return ResponseEntity.ok(diplomasToExcel);
    }

    @GetMapping("/getForeignDiplomasToAdmin")
    public ResponseEntity<?> getForeignDiplomasToAdmin(@RequestParam(value = "status") String status,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "30") int size) {
        Page<GetDiplomasToExcel> diplomasToExcel = adminService.getForeignDiplomasToAdmin(status, page, size);
        return ResponseEntity.ok(diplomasToExcel);
    }

    @GetMapping("/getAllAppToAdmin")
    public ResponseEntity<?> getAllAppToAdmin(@RequestParam(value = "diplomaStatus") String diplomaStatus,
                                              @RequestParam(value = "appStatus") String appStatus,
                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "30") int size) {
        Page<GetAppToExcel> diplomasToExcel = adminService.getAllAppToAdmin(appStatus, diplomaStatus, page, size);
        return ResponseEntity.ok(diplomasToExcel);
    }

    @GetMapping("/getAppOne/{appId}")
    public ResponseEntity<?> getAppById(@PathVariable Integer appId) {
        Result appById = adminService.getAppById(appId);
        return ResponseEntity.status(appById.isSuccess() ? 200 : 400).body(appById);
    }

    @GetMapping("/getForeignDiplom/{id}")
    public ResponseEntity<?> getForeignDiplomById(@PathVariable Integer id) {
        Result diplomaById = adminService.getForeignDiplomaById(id);
        return ResponseEntity.ok(diplomaById);
    }

    @GetMapping("/getDiplom/{id}")
    public ResponseEntity<?> getDiplomById(@PathVariable Integer id) {

        Result diplomaById = adminService.getDiplomaById(id);
        return ResponseEntity.ok(diplomaById);
    }



    @GetMapping("/getRejectedDiplom/{id}")
    public ResponseEntity<?> getRejectedDiplomById(@PathVariable Integer id) {

        Result diplomaById = adminService.getDiplomaByIdRejected(id);
        return ResponseEntity.ok(diplomaById);
    }

    @GetMapping("/exportDiplomaToAdmin")
    public ResponseEntity<?> exportDiploma(@RequestParam(value = "status") String status) {
        List<GetDiplomasToExcel> diplomasToExcel = adminService.exportDiplomasToAdmin(status);
        return ResponseEntity.ok(diplomasToExcel);
    }

    @GetMapping("/exportForeignDiplomaToAdmin")
    public ResponseEntity<?> exportForeignDiploma(@RequestParam(value = "status") String status) {
        List<GetDiplomasToExcel> diplomasToExcel = adminService.exportForeignDiplomasToAdmin(status);
        return ResponseEntity.ok(diplomasToExcel);
    }

    @GetMapping("/exportAppToAdmin")
    public ResponseEntity<?> exportApp(@RequestParam(value = "diplomaStatus") String diplomaStatus,
                                       @RequestParam(value = "appStatus") String appStatus) {
        List<GetAppToExcel> diplomasToExcel = adminService.exportAllAppToAdmin(appStatus, diplomaStatus);
        return ResponseEntity.ok(diplomasToExcel);
    }

    @GetMapping("/getStatisToAdmin")
    public ResponseEntity<?> getStatToAdmin() {
        List<StatisDirectionResponseByFutureInst> statisticToAdmin = statService.getStatisticToAdmin();
        return ResponseEntity.ok(statisticToAdmin);
    }
    @PutMapping("/updateAppStatus/{appId}")
    public ResponseEntity<?> updateAppStatus(@PathVariable Integer appId,
                                             @RequestBody UpdateAppStatus updateAppStatus,
                                             Principal principal) {

        Result result = adminService.updateStatusAppToAdmin(principal, updateAppStatus, appId);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }
    @GetMapping("/searchAppByDiplomaStatus")
    public ResponseEntity<?> searchAppByDiplomaStatus(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
            @RequestParam(value = "appStatus") String appStatus,
            @RequestParam(value = "diplomaStatus") String diplomaStatus,
            @RequestParam(value = "text") String text,
            Principal principal) {
        Page<GetAppToExcel> appResponses = adminService.searchAllAppByStatus(principal, diplomaStatus, appStatus, text, page, size);
        return ResponseEntity.ok(appResponses);
    }
    @GetMapping("/searchApp")
    public ResponseEntity<?> searchAppByAdmin(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
            @RequestParam(value = "status") String status,
            @RequestParam(value = "text") String text,
            Principal principal) {
        Page<GetAppToExcel> appResponses = adminService.searchAllAppByAdmin(principal, status, text, page, size);
        return ResponseEntity.ok(appResponses);
    }
    @PutMapping("/updateDiploma/{diplomaId}")
    public ResponseEntity<?> updateDiplomaByAdmin(@PathVariable int diplomaId,
                                           @RequestParam(value = "countryName", required = false) String countryName,
                                           @RequestParam(value = "institutionId", required = false) Integer institutionId,
                                           @RequestParam(value = "id", required = false) Integer id,
                                           @RequestParam(value = "eduFormName", required = false) String eduFormName,
                                           @RequestParam(value = "eduFinishingDate", required = false) String eduFinishingDate,
                                           @RequestParam(value = "speciality", required = false) String speciality,
                                           @RequestParam(value = "diplomaNumberAndSerial", required = false) String diplomaNumberAndSerial,
                                           @RequestParam(value = "diplomaCopyId", required = false) Integer diplomaCopyId,
                                           @RequestParam(value = "diploma", required = false) MultipartFile diploma,
                                           @RequestParam(value = "diplomaIlovaId", required = false) Integer diplomaIlovaId,
                                           @RequestParam(value = "diplomaIlova", required = false) MultipartFile diplomaIlova) {
        Result result = enrolleeService.updateDiplomaByAdmin(diplomaId, countryName, institutionId, id, eduFormName, eduFinishingDate,
                speciality, diplomaNumberAndSerial, diplomaCopyId, diploma, diplomaIlovaId, diplomaIlova);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }
    @GetMapping("/searchDiploma")
    public ResponseEntity<?> searchDiplomaByUAdmin(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
            @RequestParam(value = "status") String status,
            @RequestParam(value = "text") String text) {
        Page<GetDiplomasToExcel> diplomResponseAdmins = adminService.searchDiplomasToAdmin( status, text, page, size);
        return ResponseEntity.ok(diplomResponseAdmins);
    }

}
