package autotesting.practice_6.models.request;

import autotesting.practice_6.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepositRequestDto extends BaseModel {
    private int id;
    private double balance;
}
