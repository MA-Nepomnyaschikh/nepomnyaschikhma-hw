package autotesting.practice_3.models.response;

import autotesting.practice_3.models.BaseModel;
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
