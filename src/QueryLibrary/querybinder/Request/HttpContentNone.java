package querylibrary.querybinder.Request;

public class HttpContentNone extends HttpContent {
    /// FIELDs
    public static final String TYPE = "NONE";
    private static final HttpContentNone instance = new HttpContentNone();

    private HttpContentNone() {
    }

    public static HttpContentNone getInstance() { return instance; }

    @Override
    public byte[] getBytes() { return null; }

}
