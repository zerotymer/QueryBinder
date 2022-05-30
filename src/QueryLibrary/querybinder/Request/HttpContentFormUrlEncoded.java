package querylibrary.querybinder.Request;

import java.util.Map;

public class HttpContentFormUrlEncoded
        extends HttpContentFormData {

    //region Constructors ----------------------------------------------------------------------------------------------
    public HttpContentFormUrlEncoded() { super(); }
    public HttpContentFormUrlEncoded(Map<String, String> map) { super(map); }
    //endregion Constructors -------------------------------------------------------------------------------------------

    //region Methods ---------------------------------------------------------------------------------------------------
    @Override
    public String getString() {
        // TODO: make this method.
        // URLEncoding
        return null;
    }
    //endregion Methods ------------------------------------------------------------------------------------------------
}
