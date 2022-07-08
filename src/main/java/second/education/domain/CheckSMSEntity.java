package second.education.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(columnList = "phoneNumber"))
public class CheckSMSEntity extends AbstractData<Integer> {

    private String phoneNumber;
    private String code;
    private String pinfl;
    private String givenDate;
}
