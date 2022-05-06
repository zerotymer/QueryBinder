package QueryBinder;

public enum HttpRequestMethods {
    GET(1), POST(2), PUT(3), DELETE(4), HEAD(5), OPTIONS(6), TRACE(7), CONNECT(8);

    /// FIELDs
    private final int value;

    /// CONSTRUCTORs
    HttpRequestMethods(int value) { this.value = value; }

    /// METHODs
    public int getValue() { return value; }
    // toString, valueOf 지원
}
