package second.education.domain.classificator;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.AbstractData;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Language extends AbstractData<Integer> {

    private String language;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Direction direction;
}
