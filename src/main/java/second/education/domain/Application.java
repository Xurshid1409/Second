package second.education.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.classificator.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application extends AbstractData<Integer> {

    private String status;

    @OneToOne(fetch = FetchType.LAZY)
    private Kvota kvota;

    @ManyToOne(fetch = FetchType.LAZY)
    private FutureInstitution futureInstitution;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EnrolleeInfo enrolleeInfo;
}
