package Testing;

import QueryBinder.Annotation.BindingMapper;
import QueryBinder.Annotation.BindingMapperParam;
import QueryBinder.QueryBinder;
import QueryBinder.QueryRequestable;
import QueryBinder.Request.HttpRequestMethods;

import java.util.HashMap;

@BindingMapper(url = "http://localhost:8080/myboard/view.ajax", method = HttpRequestMethods.GET)
public class TestRequestVO implements QueryRequestable<TestResponseVO> {
    /// FIELDs
    private int value;

    /// CONSTRUCOTRs

    /// METHODs

    // GETTER
    // SETTER

    //
    @BindingMapperParam(name = "value", defaultValue = "0", required = false)
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
