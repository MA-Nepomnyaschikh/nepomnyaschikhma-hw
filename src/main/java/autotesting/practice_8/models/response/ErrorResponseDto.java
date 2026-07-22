package autotesting.practice_8.models.response;

import autotesting.practice_8.models.BaseModel;
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
