package autotesting.practice_10.models.request;

import autotesting.practice_10.models.BaseModel;
import autotesting.practice_10.testdata.randommodelgenerator.annotations.GeneratingRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequestDto extends BaseModel {
    @GeneratingRule(regex = "^[A-Za-z]+ [A-Za-z]+$")
    private String name;
}
