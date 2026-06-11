package autotesting.practice_3.contract.models.request;

import autotesting.practice_3.contract.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserRequestDto extends BaseModel {
    private String username;
    private String password;
}
