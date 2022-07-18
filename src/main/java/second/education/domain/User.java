package second.education.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.classificator.FutureInstitution;
import second.education.domain.classificator.Role;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", indexes = @Index(columnList = "phoneNumber"))
public class User extends AbstractData<Integer> {

    private String phoneNumber;
    private String password;
    private String pinfl;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private Role role;

//    @OneToOne(fetch = FetchType.LAZY)
//    private FutureInstitution futureInstitution;

}
