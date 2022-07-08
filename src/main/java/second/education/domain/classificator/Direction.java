package second.education.domain.classificator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.AbstractData;
import second.education.domain.classificator.FutureInstitution;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Direction extends AbstractData<Integer> {

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private FutureInstitution futureInstitution;
}
