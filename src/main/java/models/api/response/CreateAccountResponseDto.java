package models.api.response;

import models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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
