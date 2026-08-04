package autotesting.practice_9.models.response;

import autotesting.practice_9.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDto extends BaseModel {
    private String timestamp;
    private String status;
    private String error;
    private String path;
}
