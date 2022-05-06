package Testing;

import QueryBinder.Annotation.BindingMapper;
import QueryBinder.Annotation.BindingMapperParam;
import QueryBinder.QueryResponsible;

import java.util.HashMap;

@BindingMapper
public class TestResponseVO implements QueryResponsible {
    @BindingMapperParam(name = "aaa")
    private String name;

    @Override
    public HashMap getMap() {
        return null;
    }
}
