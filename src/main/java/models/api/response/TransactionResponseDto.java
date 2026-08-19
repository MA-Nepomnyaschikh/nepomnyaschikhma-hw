package models.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
