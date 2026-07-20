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
public class TransferResponseDto extends BaseModel {
    private int senderAccountId;
    private int receiverAccountId;
    private double amount;
    private String message;
}
