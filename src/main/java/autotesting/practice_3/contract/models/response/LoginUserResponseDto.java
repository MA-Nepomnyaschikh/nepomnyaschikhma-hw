package autotesting.practice_3.contract.models.response;

import autotesting.practice_3.contract.enams.UserRole;
import autotesting.practice_3.contract.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserResponseDto extends BaseModel {
    private String role;
    private String username;
}
