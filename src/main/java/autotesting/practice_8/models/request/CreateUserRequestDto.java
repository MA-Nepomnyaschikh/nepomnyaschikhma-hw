package autotesting.practice_8.models.request;

import autotesting.practice_8.models.BaseModel;
import autotesting.practice_8.testdata.randommodelgenerator.annotations.GeneratingRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequestDto extends BaseModel {
    @GeneratingRule(regex = "^User_[A-Za-z0-9._-]{5,10}$")
    private String username;
    @GeneratingRule(regex = "^[A-Z][a-z][0-9][!@#$%^&][A-Za-z0-9!@#$%^&*]{8}$")
    private String password;
    @GeneratingRule(regex = "^USER$")
    private String role;
}
