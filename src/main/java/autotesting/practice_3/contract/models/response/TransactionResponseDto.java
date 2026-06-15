package autotesting.practice_3.contract.models.response;

import autotesting.practice_3.contract.models.BaseModel;
import autotesting.practice_3.contract.enams.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponseDto extends BaseModel {
    private int id;
    private double amount;
    private String type;
    private String timestamp;
    private int relatedAccountId;
}
