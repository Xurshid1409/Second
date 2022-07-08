package second.education.api_model.ins_api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataItemType {

    private Integer id;
    private String code;
    private String name_uz;
    private String name_oz;
    private String name_ru;
    private String name_en;
    private Integer edu_type_id;
    private String edu_type_name;
    private Integer status_id;
    private String status_name;
    private String created_at;
}
