package Testing;

import querylibrary.querybinder.Annotation.QueryBindingGetParam;
import querylibrary.querybinder.Annotation.QueryBindingUrl;
import querylibrary.querybinder.QueryRequestable;

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
