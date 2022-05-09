package Testing;

import QueryBinder.Annotation.BindingMapperUrl;
import QueryBinder.Annotation.BindingMapperParam;
import QueryBinder.QueryRequestable;
import QueryBinder.Request.HttpRequestMethods;

import java.util.HashMap;

@BindingMapperUrl(value = "http://localhost:8080/myboard/list.ajax", method = HttpRequestMethods.GET)
public class TestRequestVO implements QueryRequestable{
    /// FIELDs
    private int value;

    /// CONSTRUCOTRs

    /// METHODs

    // GETTER
    // SETTER

    //
    @BindingMapperParam(value = "value", defaultValue = "1", required = false)
    public String getQueryValue() {
        return String.valueOf(value);
    }

    @Override
    public TestResponseVO requestQuery() {
        return null;
    }

    @Override
    public HashMap<String, String> requestQueryToMap() {
        return null;
    }
}
