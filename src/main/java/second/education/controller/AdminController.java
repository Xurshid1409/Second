package second.education.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
public class AdminController {

    private final FutureInstitutionService futureInstitutionService;
    private final DirectionService directionService;
    private final EduFormAndLanguageService eduFormAndLanguageService;
    private final EduFormService kvotaService;

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
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

    @GetMapping("direction/futureInstitution/{futureInstitutionId}")
    public ResponseEntity<?> getAllDirection(@PathVariable int futureInstitutionId) {
        List<DirectionResponse> allDirection = directionService.getAllDirection(futureInstitutionId);
        return ResponseEntity.ok(allDirection);
    }

    @GetMapping("direction/{directionId}")
    public ResponseEntity<?> getDirectionById(@PathVariable int directionId) {
        DirectionResponse directionById = directionService.getDirectionById(directionId);
        return ResponseEntity.ok(directionById);
    }

    //Ta'lim shakli

    @PostMapping("eduForm")
    public ResponseEntity<?> createEduForm(@RequestBody EduFormRequest request) {
        Result result = eduFormAndLanguageService.createEduForm(request);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

    @PutMapping("eduForm/{eduFormId}")
    public ResponseEntity<?> updateEduForm(@PathVariable int eduFormId , @RequestBody EduFormRequest request) {
        Result result = eduFormAndLanguageService.updateEduForm(eduFormId, request);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @GetMapping("eduForm/direction/{directionId}")
    public ResponseEntity<?> getEduFormByDirection(@PathVariable int directionId) {
        List<EduFormResponse> allEduForm = eduFormAndLanguageService.getAllEduForm(directionId);
        return ResponseEntity.ok(allEduForm);
    }

    @GetMapping("eduForm/{eduFormId}")
    public ResponseEntity<?> getEduFormById(@PathVariable int eduFormId) {
        EduFormResponse eduFormById = eduFormAndLanguageService.getEduFormById(eduFormId);
        return ResponseEntity.ok(eduFormById);
    }

    @DeleteMapping("eduForm/{eduFormId}")
    public ResponseEntity<?> deleteEduForm(@PathVariable int eduFormId) {
        Result result = eduFormAndLanguageService.deleteEduForm(eduFormId);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    //Ta'lim tili

    @PostMapping("language")
    public ResponseEntity<?> createLanguage(@RequestBody LanguageRequest request) {
        Result result = eduFormAndLanguageService.createLanguage(request);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

    @PutMapping("language/{languageId}")
    public ResponseEntity<?> updateLanguage(@PathVariable int languageId, @RequestBody LanguageRequest request) {
        Result result = eduFormAndLanguageService.updateLanguage(languageId, request);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @GetMapping("language/direction/{directionId}")
    public ResponseEntity<?> getLanguageByDirection(@PathVariable int directionId) {
        List<LanguageResponse> allLanguages = eduFormAndLanguageService.getAllLanguages(directionId);
        return ResponseEntity.ok(allLanguages);
    }

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

    @PostMapping("kvota")
    public ResponseEntity<?> createKvota(@RequestBody List<KvotaRequest> requests) {
        Result result = kvotaService.createKvota(requests);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

    @PutMapping("kvota")
    public ResponseEntity<?> updateKvota(@RequestBody List<KvotaRequest> requests) {
        Result result = kvotaService.updateKvota(requests);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @GetMapping("kvota")
    public ResponseEntity<?> getAllKvota() {
        List<KvotaResponse> kvotas = kvotaService.getAllKvota();
        return ResponseEntity.ok(kvotas);
    }

    @GetMapping("kvota/{kvotaId}")
    public ResponseEntity<?> getKvotaById(@PathVariable Integer kvotaId) {
        KvotaResponse kvotaBYId = kvotaService.getKvotaBYId(kvotaId);
        return ResponseEntity.status(kvotaBYId != null ? 200 : 404).body(kvotaBYId);
    }
}
