package api.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import api.models.BaseModel;
import api.models.enams.TransferStatus;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferWithFraudCheckResponseDto extends BaseModel {
    private long senderAccountId;
    private long receiverAccountId;
    private BigDecimal amount;
    private String message;
    private long transactionId;
    private boolean requiresManualReview;
    private boolean requiresVerification;
    private BigDecimal fraudRiskScore;
    private String fraudReason;
    private TransferStatus status;
}
