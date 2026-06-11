package autotesting.practice_3.contract.models.response;

import autotesting.practice_3.contract.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponseDto extends BaseModel {
    private int senderAccountId;
    private int receiverAccountId;
    private double amount;
    private String message;
}
