package querylibrary.querybinder.Request;

/**
 * 바이너리 지원
 */
public class HttpContentBinary extends HttpContent {
    /// FIELDs
    private byte[] bytes;

    /// CONSTRUCTORs
    public HttpContentBinary() {
        super();
    }
    public HttpContentBinary(byte[] bytes) {
        super();
        this.bytes = bytes;
    }

    /// METHODs
    /// GETTERs & SETTERs

    @Override
    public byte[] getBytes() { return bytes; }
    public void setBytes(byte[] bytes) { this.bytes = bytes; }

    @Override
    public String getString() { throw new UnsupportedOperationException(); }
}
