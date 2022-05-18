package querylibrary.querybinder.Request;

/**
 * Http 요청 방식
 * @author 신현진
 */
public enum HttpRequestMethods {
    GET(1), POST(2), PUT(3), DELETE(4), HEAD(5), PATCH(6), OPTIONS(7), TRACE(8), CONNECT(9);

    /// FIELDs
    private final int value;

    /// CONSTRUCTORs
    HttpRequestMethods(int value) { this.value = value; }

    /// METHODs
    public int getValue() { return value; }
    // toString, valueOf 지원
}
