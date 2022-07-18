package second.education.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.classificator.FutureInstitution;
import second.education.domain.classificator.University;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity extends AbstractData<Integer> {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FutureInstitution futureInstitution;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<University> university;

    private String pinfl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

}
