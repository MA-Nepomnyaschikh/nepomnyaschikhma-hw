package autotesting.practice_9.models.request;

import autotesting.practice_9.models.BaseModel;
import autotesting.practice_9.testdata.randommodelgenerator.annotations.GeneratingRule;
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
