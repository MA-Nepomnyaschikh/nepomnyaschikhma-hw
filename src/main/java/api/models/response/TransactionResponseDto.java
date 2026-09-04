package api.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import api.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import api.models.enams.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({
        "amountAsDouble",
        "timestampAsString"
})
public class TransactionResponseDto extends BaseModel {
    private long id;
    private BigDecimal amount;
    private String type;
    private String timestamp;
    private CreateAccountResponseDto relatedAccount;
    private TransactionStatus status;
    private boolean fraudCheckRequired;
    private long relatedAccountId;

    public void setAmount(BigDecimal amount) {
        this.amount = amount == null
                ? null
                : amount.setScale(2);
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp == null
                ? null
                : String.valueOf(timestamp.truncatedTo(ChronoUnit.MILLIS));
    }
}
