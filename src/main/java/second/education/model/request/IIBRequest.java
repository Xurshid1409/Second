package second.education.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IIBRequest {

    private String phoneNumber;
    private String pinfl;
    private String given_date; //yyyy-MM-dd formatda
}
