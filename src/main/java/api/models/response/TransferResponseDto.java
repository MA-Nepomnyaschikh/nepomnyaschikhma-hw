package api.models.response;

import api.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponseDto extends BaseModel {
    private int senderAccountId;
    private int receiverAccountId;
    private BigDecimal amount;
    private String message;
}
