package Testing;

import QueryBinder.Annotation.BindingMapperUrl;
import QueryBinder.Annotation.BindingMapperParam;
import QueryBinder.QueryResponsible;

@BindingMapperUrl
public class TestResponseVO implements QueryResponsible {
    @BindingMapperParam(value = "aaa")
    private String name;


    @Override
    public TestResponseVO newInstance() {
        return null;
    }
}
