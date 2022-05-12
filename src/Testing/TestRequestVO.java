package Testing;

import QueryBinder.Annotation.QueryBindingGetParam;
import QueryBinder.Annotation.QueryBindingUrl;
import QueryBinder.Annotation.QueryBindingParam;
import QueryBinder.QueryRequestable;
import QueryBinder.Request.HttpRequestMethods;

import java.util.HashMap;

@QueryBindingUrl(value = "http://localhost:8080/myboard/list.ajax")
public class TestRequestVO implements QueryRequestable{
    /// FIELDs
    private int value;

    /// CONSTRUCOTRs

    /// METHODs

    // GETTER
    // SETTER

    //
    @QueryBindingGetParam(value = "value", defaultValue = "1", isRequired = true)
    public String getQueryValue() {
        return String.valueOf(value);
    }
}
