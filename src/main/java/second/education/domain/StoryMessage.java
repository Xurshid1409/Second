package second.education.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class StoryMessage extends AbstractData<Integer> {

    private String status;
    private String message;
    private String pinfl;
    private String firstname;
    private String lastname;

    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;
}
