package autotesting.practice_8.models.response;

import autotesting.practice_8.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserResponseDto extends BaseModel {
    private long id;
    private String username;
    private String password;
    private String name;
    private String role;
    private List<CreateAccountResponseDto> accounts;

}
