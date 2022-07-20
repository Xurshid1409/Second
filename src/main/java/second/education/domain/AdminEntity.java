package second.education.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.classificator.FutureInstitution;
import second.education.domain.classificator.University;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity extends AbstractData<Integer> {

    private String fistName;
    private String lastname;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FutureInstitution futureInstitution;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<University> universities = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
}
