package autotesting.practice_10.models.response;

import autotesting.practice_10.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountResponseDto extends BaseModel {
    private int id;
    private String accountNumber;
    private double balance;
    private List<TransactionResponseDto> transactions;
}
