package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import second.education.domain.Diploma;
import second.education.domain.Document;
import second.education.model.response.DocumentResponse;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.DiplomaRepository;
import second.education.repository.DocumentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final FileService fileService;
    private final DocumentRepository documentRepository;
    private final DiplomaRepository diplomaRepository;

    @Transactional
    public Result documentSave(int diplomaId, MultipartFile diplomaCopy, MultipartFile diplomaIlova){

        try {
            Diploma diploma = diplomaRepository.findById(diplomaId).get();
            List<Document> documents = new ArrayList<>();
            if (diplomaCopy != null) {
                Document diplom = new Document();
                String diplomName = fileService.upload(diplomaCopy, "Diplom");
                String currentUrl = getCurrentUrl(diplomName);
                diplom.setUrl(currentUrl);
                diplom.setFileName(diplomName);
                diplom.setDiploma(diploma);
                documents.add(diplom);
            }
            if (diplomaIlova != null) {
                Document diplomIlova = new Document();
                String ilovaName = fileService.upload(diplomaIlova, "Ilova");
                String currentUrl = getCurrentUrl(ilovaName);
                diplomIlova.setUrl(currentUrl);
                diplomIlova.setFileName(ilovaName);
                diplomIlova.setDiploma(diploma);
                documents.add(diplomIlova);
            }
            documentRepository.saveAll(documents);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result documentUpdate( int docDiplomId, int docIlovaId, MultipartFile docDiplom, MultipartFile docIlova){
        try {
            List<Document> documents = new ArrayList<>();
            if (docDiplom != null) {
                Document document = documentRepository.findById(docDiplomId).get();
                fileService.deleteDocument(document);
                String diplomName = fileService.upload(docDiplom, "Diplom");
                String currentUrl = getCurrentUrl(diplomName);
                document.setUrl(currentUrl);
                document.setFileName(diplomName);
                document.setModifiedDate(LocalDateTime.now());
                documents.add(document);
            }
            if (docIlova != null) {
                Document documentIlova = documentRepository.findById(docIlovaId).get();
                fileService.deleteDocument(documentIlova);
                String diplomIlovaName = fileService.upload(docIlova, "Ilova");
                String currentUrl = getCurrentUrl(diplomIlovaName);
                documentIlova.setUrl(currentUrl);
                documentIlova.setFileName(diplomIlovaName);
                documentIlova.setModifiedDate(LocalDateTime.now());
                documents.add(documentIlova);
            }
            documentRepository.saveAll(documents);
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

    @Transactional(readOnly = true)
    public DocumentResponse getDocumentById(int documentId) {
        Document document = documentRepository.findById(documentId).orElse(new Document());
        return new DocumentResponse(document);
    }

    @Transactional
    public Result deleteDocument(int documentId) {
        try {
            Document document = documentRepository.findById(documentId).get();
            Result result = fileService.deleteDocument(document);
            if (result.isSuccess())
            documentRepository.deleteById(documentId);
            return new Result(ResponseMessage.SUCCESSFULLY_DELETED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_DELETED.getMessage(), false);
        }
    }

    private String getCurrentUrl(String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/download/")
                .path(fileName).toUriString();
    }
}
