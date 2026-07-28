package autotesting.practice_9.models.response;

import autotesting.practice_9.models.BaseModel;
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
