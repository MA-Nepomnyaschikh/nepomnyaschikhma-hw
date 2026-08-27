package models.api.request;

import models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepositRequestDto extends BaseModel {
    private long accountId;
    private BigDecimal amount;
}
