package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

}
