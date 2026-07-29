package autotesting.practice_10.requests;

import autotesting.practice_10.models.BaseModel;

public interface CrudOperations<T extends BaseModel> {
    Object post(BaseModel model);
    Object post();
    Object get();
    Object getAll();
    Object put(BaseModel model);
    Object delete(long id);

}
