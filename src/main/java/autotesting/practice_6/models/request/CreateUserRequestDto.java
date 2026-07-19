package autotesting.practice_6.models.request;

import autotesting.practice_6.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequestDto extends BaseModel {
    private String username;
    private String password;
    private String role;
}
