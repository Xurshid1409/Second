package second.education.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import second.education.domain.Document;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.DocumentRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileService {

    private DocumentRepository documentRepository;
    private String fileStorageLocation;
    private Path fileStoragePath;

    public FileService(DocumentRepository documentRepository,
                       @Value("${file.storage.location}") String fileStorageLocation) {
        this.documentRepository = documentRepository;
        this.fileStorageLocation = fileStorageLocation;
        this.fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        try {
            if (!Files.isDirectory(fileStoragePath))
                Files.createDirectory(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }
    }

    @Transactional
    public String upload(MultipartFile file, String prefix) {
        if (!file.isEmpty()) {
            String random = RandomStringUtils.random(5, true, true);
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fullFileName = prefix + "-" + random + fileName;
            Path filePath = Paths.get(fileStoragePath + "/" + fullFileName);
            try {
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Issue in storing the file", e);
            }
            return fullFileName;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Resource downloadFile(String fileName) throws IOException {

        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading file", e);
        }
        if (resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("The file doesn't exists or not readable");
        }
    }

    @Transactional
    public Result deleteDocument(Document document) {
        try {
            File file = new File(fileStoragePath + "/" + document.getFileName());
            file.delete();
            return new Result(ResponseMessage.SUCCESSFULLY_DELETED.getMessage(), true);
        } catch (RuntimeException e) {
            return new Result(ResponseMessage.ERROR_DELETED.getMessage(), false);
        }
    }
}
