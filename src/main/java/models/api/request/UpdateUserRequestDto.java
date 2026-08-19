package models.api.request;

import models.BaseModel;
import testdata.randommodelgenerator.annotations.GeneratingRule;
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
