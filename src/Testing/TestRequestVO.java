package Testing;

import QueryBinder.Annotation.BindingMapper;
import QueryBinder.Annotation.BindingMapperParam;
import QueryBinder.QueryRequestable;
import QueryBinder.Request.HttpRequestMethods;

@BindingMapper(url = "http://localhost:8080/QueryBinder/QueryBinderServlet", method = HttpRequestMethods.GET)
public class TestVO implements QueryRequestable {
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
}
