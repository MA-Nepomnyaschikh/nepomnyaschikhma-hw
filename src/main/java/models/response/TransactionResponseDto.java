package models.response;

import models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponseDto extends BaseModel {
    private int id;
    private double amount;
    private String type;
    private String timestamp;
    private int relatedAccountId;
}
