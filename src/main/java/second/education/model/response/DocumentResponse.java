package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {

    private int id;
   // private String fileName;
    private String url;

    public DocumentResponse(Document document) {
        this.id = document.getId();
     //   this.fileName = document.getFileName();
        this.url = document.getUrl();
    }
}
