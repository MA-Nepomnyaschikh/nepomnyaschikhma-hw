package autotesting.practice_3.contract.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAccountResponseDto {
    private CreateUserResponseDto customer;
    private String message;
}
