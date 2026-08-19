package models.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.BaseModel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserResponseDto extends BaseModel {

    private long id;
    private String username;
    private String name;
    private String role;
    private List<CreateAccountResponseDto> accounts;

}
