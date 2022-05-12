package Testing;

import QueryBinder.Annotation.QueryBindingUrl;
import QueryBinder.Annotation.QueryBindingParam;
import QueryBinder.QueryResponsible;

@QueryBindingUrl
public class TestResponseVO implements QueryResponsible {
    @QueryBindingParam(value = "aaa")
    private String name;


    @Override
    public TestResponseVO newInstance() {
        return null;
    }
}
