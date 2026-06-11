package autotesting.practice_3.contract.models.response;

import autotesting.practice_3.contract.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponseDto extends BaseModel {
    private int id;
    private String accountNumber;
    private double balance;
    private List<TransactionResponseDto> transactions;
}
