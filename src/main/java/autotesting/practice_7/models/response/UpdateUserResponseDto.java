package autotesting.practice_7.models.response;

import autotesting.practice_7.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserResponseDto extends BaseModel {
    private CreateUserResponseDto customer;
    private String message;
}
