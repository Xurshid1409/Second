package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import second.education.model.response.DiplomaResponse;
import second.education.model.response.EnrolleeResponse;
import second.education.model.response.Result;
import second.education.service.EnrolleeService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/enrolleeInfo/")
@RequiredArgsConstructor
@SecurityRequirement(name = "second")
public class EnrolleeInfoController {

    private final EnrolleeService enrolleeService;

    @GetMapping
    public ResponseEntity<?> getEnrolleInfo(Principal principal) {
        EnrolleeResponse enrolleeResponse = enrolleeService.getEnrolleeResponse(principal);
        return ResponseEntity.ok(enrolleeResponse);
    }

    @PostMapping
    public ResponseEntity<?> createDiploma(Principal principal,
                                           @RequestParam(value = "countryName", required = false) String countryName,
                                           @RequestParam(value = "institutionId", required = false) Integer institutionId,
                                           @RequestParam(value = "id", required = false) Integer id,
                                           @RequestParam(value = "eduFormName", required = false) String eduFormName,
                                           @RequestParam(value = "eduFinishingDate", required = false) String eduFinishingDate,
                                           @RequestParam(value = "speciality", required = false) String speciality,
                                           @RequestParam(value = "diplomaNumberAndSerial", required = false) String diplomaNumberAndSerial,
                                           @RequestParam(value = "diploma", required = false) MultipartFile diploma,
                                           @RequestParam(value = "diplomaIlova", required = false) MultipartFile diplomaIlova) {
        DiplomaResponse response = enrolleeService.createDiploma(principal, countryName,
                institutionId, id, eduFormName, eduFinishingDate, speciality,
                diplomaNumberAndSerial, diploma, diplomaIlova);
        return ResponseEntity.ok(response);
    }

    @PostMapping("foreignDiploma")
    public ResponseEntity<?> createForeignDiploma(Principal principal,
                                           @RequestParam(value = "countryName", required = false) String countryName,
                                           @RequestParam(value = "institutionName", required = false) String institutionName,
                                           @RequestParam(value = "eduFormName", required = false) String eduFormName,
                                           @RequestParam(value = "eduFinishingDate", required = false) String eduFinishingDate,
                                           @RequestParam(value = "speciality", required = false) String speciality,
                                           @RequestParam(value = "diplomaNumberAndSerial", required = false) String diplomaNumberAndSerial,
                                           @RequestParam(value = "diploma", required = false) MultipartFile diploma,
                                           @RequestParam(value = "diplomaIlova", required = false) MultipartFile diplomaIlova) {
        DiplomaResponse response = enrolleeService.createForeignDiploma(principal, countryName,
                institutionName, eduFormName, eduFinishingDate, speciality,
                diplomaNumberAndSerial, diploma, diplomaIlova);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{diplomaId}")
    public ResponseEntity<?> updateDiploma(@PathVariable int diplomaId,
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
        DiplomaResponse diplomaResponse = enrolleeService.updateDiploma(diplomaId, countryName, institutionId, id, eduFormName, eduFinishingDate,
                speciality, diplomaNumberAndSerial, diplomaCopyId, diploma, diplomaIlovaId, diplomaIlova);
        return ResponseEntity.ok(diplomaResponse);
    }

    @PatchMapping("check/diplomas/{diplomaId}")
    public ResponseEntity<?> checkDiploma(@PathVariable int diplomaId) {
        Result result = enrolleeService.checkDiploma(diplomaId);
        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
    }

    @GetMapping("diplomas")
    public ResponseEntity<?> getDiplomas(Principal principal) {
        List<DiplomaResponse> diplomasByEnrolleeInfo = enrolleeService.getDiplomasByEnrolleeInfo(principal);
        return ResponseEntity.ok(diplomasByEnrolleeInfo);
    }

    @GetMapping("diplomas/{diplomaId}")
    public ResponseEntity<?> getDiplomaById(Principal principal, @PathVariable int diplomaId) {
        DiplomaResponse diplomaByIdAndEnrolleInfo = enrolleeService.getDiplomaByIdAndEnrolleInfo(principal, diplomaId);
        return ResponseEntity.ok(diplomaByIdAndEnrolleInfo);
    }

    @GetMapping("diplomas/profile")
    public ResponseEntity<?> getDiplomaProfile(Principal principal) {
        DiplomaResponse diplomaByIdAndEnrolleInfo = enrolleeService.getDiplomaProfile(principal);
        return ResponseEntity.ok(diplomaByIdAndEnrolleInfo);
    }
}
