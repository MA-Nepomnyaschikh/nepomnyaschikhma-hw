package api.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import api.models.BaseModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserProfileResponseDto extends BaseModel {

    private long id;
    private String username;
    private String name;
    private String role;

}
