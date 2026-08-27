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
public class DepositResponseDto extends BaseModel {

    private long id;
    private String accountNumber;
    private BigDecimal balance;
    private BigDecimal depositAmount;
    private long transactionId;
}
