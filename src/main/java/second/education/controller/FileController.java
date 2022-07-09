package second.education.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.service.DocumentService;
import second.education.service.FileService;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("api/file/")
@RequiredArgsConstructor
@SecurityRequirement(name = "second")
public class FileController {

    private final FileService fileService;
    private final DocumentService documentService;

    @PostMapping("upload/{diplomaId}")
    public ResponseEntity<?> uploadDiploma(@PathVariable int diplomaId,
                                           @RequestParam(value = "document") MultipartFile document,
                                           @RequestParam(value = "documentIlova") MultipartFile documentIlova) {
        Result result = documentService.documentSave(diplomaId, document, documentIlova);
        return ResponseEntity.status(result.isSuccess() ? 201 : 400).body(result);
    }

//    @PutMapping("update/{diplomaId}")
//    public ResponseEntity<?> updateDiploma(@RequestParam(value = "docDiplomId", required = false) int docDiplomId,
//                                           @RequestParam(value = "docIlovaId", required = false) int documentIlovaId,
//                                           @RequestParam(value = "docDiplom", required = false) MultipartFile docDiplom,
//                                           @RequestParam(value = "docilova", required = false) MultipartFile docilova) {
//        Result result = documentService.documentUpdate(docDiplomId, documentIlovaId, docDiplom, docilova);
//        return ResponseEntity.status(result.isSuccess() ? 200 : 400).body(result);
//    }

    @GetMapping("{documentId}")
    public ResponseEntity<?> getDocumentById(@PathVariable int documentId) {
        return ResponseEntity.ok(documentService.getDocumentById(documentId));

    }

    @GetMapping("download/{fileName}")
    public ResponseEntity<?> downloadDiploma(@PathVariable String fileName) {
        try {
            Resource resource = fileService.downloadFile(fileName);
            HttpHeaders headers = new HttpHeaders();
            MediaType contType;
            headers.add("content-disposition", "inline; filename=" + resource.getFilename());
            if (Objects.requireNonNull(resource.getFilename()).endsWith("pdf")) {
                headers.setContentType(MediaType.parseMediaType("application/pdf"));
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                contType = MediaType.APPLICATION_PDF;
            } else {
                contType = MediaType.IMAGE_JPEG;
            }
            return ResponseEntity.ok().contentType(contType).
                    headers(headers)
                    .body(resource);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            return ResponseEntity.ok(new Result(ResponseMessage.ERROR.getMessage(), false));
        }
    }

    @DeleteMapping("delete/{documentId}")
    public ResponseEntity<?> deleteFile(@PathVariable int documentId) {
        Result deleted = documentService.deleteDocument(documentId);
        return ResponseEntity.status(deleted.isSuccess() ? 200 : 404).body(deleted);
    }
}
