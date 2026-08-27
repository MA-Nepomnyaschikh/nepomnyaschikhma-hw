package models.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.BaseModel;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountResponseDto extends BaseModel {
    private long id;
    private String accountNumber;
    private BigDecimal balance;

    public void setBalance(BigDecimal balance) {
        this.balance = balance == null
                ? null
                : balance.setScale(2);
    }
}
