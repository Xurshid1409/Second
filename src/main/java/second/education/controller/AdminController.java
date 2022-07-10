package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import second.education.domain.classificator.Direction;
import second.education.domain.classificator.FutureInstitution;
import second.education.model.request.*;
import second.education.model.response.*;
import second.education.service.DirectionService;
import second.education.service.EduFormAndLanguageService;
import second.education.service.FutureInstitutionService;
import second.education.service.EduFormService;
import java.util.List;

@RestController
@RequestMapping("admin/")
@RequiredArgsConstructor
@SecurityRequirement(name = "second")
public class AdminController {

    private final FutureInstitutionService futureInstitutionService;
    private final DirectionService directionService;
    private final EduFormAndLanguageService eduFormAndLanguageService;
    private final EduFormService eduFormService;

    @PostMapping("futureInstitution")
    public ResponseEntity<?> createFutureInstitution(@RequestBody FutureInstitutionRequest request) {
        FutureInstitution futureInstitution = futureInstitutionService.createFutureInstitution(request);
        return ResponseEntity.ok(futureInstitution);
    }

    @PutMapping("futureInstitution/{futureInstId}")
    public ResponseEntity<?> updateFutureInstitution(@PathVariable int futureInstId, @RequestBody FutureInstitutionRequest request) {
        FutureInstitution futureInstitution = futureInstitutionService.updateFutureInstitution(futureInstId, request);
        return ResponseEntity.ok(futureInstitution);
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
        Direction direction = directionService.createDirection(request);
        return ResponseEntity.ok(direction);
    }

    @PutMapping("direction/{directionId}")
    public ResponseEntity<?> updateDirection(@PathVariable int directionId, @RequestBody DirectionRequest request) {
        Direction direction = directionService.updateDirection(directionId, request);
        return ResponseEntity.ok(direction);
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

    //Ta'lim tili

    @GetMapping("language/{languageId}")
    public ResponseEntity<?> getLanguageById(@PathVariable int languageId) {
        LanguageResponse languageById = eduFormAndLanguageService.getLanguageById(languageId);
        return ResponseEntity.ok(languageById);
    }

    @DeleteMapping("language/{languageId}")
    public ResponseEntity<?> deleteLanguage(@PathVariable int languageId) {
        Result result = eduFormAndLanguageService.deleteLanguage(languageId);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @PostMapping("eduForm")
    public ResponseEntity<?> createEduForm(@RequestBody EduFormRequest eduFormRequest) {
        Result result = eduFormService.createEduForm(eduFormRequest);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

    @PutMapping("eduForm")
    public ResponseEntity<?> updateEduForm(@RequestBody EduFormRequest request) {
        Result result = eduFormService.updateEduForm(request);
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
}
