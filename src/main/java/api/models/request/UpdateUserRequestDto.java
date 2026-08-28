package api.models.request;

import api.models.BaseModel;
import common.testdata.generator.annotations.GeneratingRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequestDto extends BaseModel {
    @GeneratingRule(regex = "^[A-Za-z]{1,8} [A-Za-z]{1,8}$")
    private String name;
}
