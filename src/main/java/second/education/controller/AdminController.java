package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import second.education.model.request.DirectionRequest;
import second.education.model.request.EduFormRequest;
import second.education.model.request.FutureInstitutionRequest;
import second.education.model.request.UserRequest;
import second.education.model.response.*;
import second.education.service.AdminService;
import second.education.service.DirectionService;
import second.education.service.EduFormService;
import second.education.service.FutureInstitutionService;

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

}
