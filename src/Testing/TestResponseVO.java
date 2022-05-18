package Testing;

import querylibrary.querybinder.Annotation.QueryBindingUrl;
import querylibrary.querybinder.Annotation.QueryBindingParam;
import querylibrary.querybinder.QueryResponsible;

@QueryBindingUrl
public class TestResponseVO implements QueryResponsible {
    @QueryBindingParam(value = "aaa")
    private String name;


    @Override
    public TestResponseVO newInstance() {
        return null;
    }
}
