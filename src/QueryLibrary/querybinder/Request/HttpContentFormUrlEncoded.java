package querylibrary.querybinder.Request;

import java.util.Map;

public class HttpContentFormUrlEncoded
        extends HttpContentFormData {

    /// CONSTRUCTORs
    public HttpContentFormUrlEncoded() {
        super();
    }
    public HttpContentFormUrlEncoded(Map<String, String> map) {
        super(map);
    }

    /// METHOD
    @Override
    public String getString() {
        // TODO: make this method.
        // URLEncoding
        return null;
    }


}
