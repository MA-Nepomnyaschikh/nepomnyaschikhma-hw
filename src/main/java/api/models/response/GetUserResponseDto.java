package api.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import api.models.BaseModel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserResponseDto extends BaseModel {

    private long id;
    private String username;
    private String password;
    private String name;
    private String role;
    private List<CreateAccountResponseDto> accounts;

}
