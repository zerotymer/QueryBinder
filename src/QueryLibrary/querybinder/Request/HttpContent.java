package querylibrary.querybinder.Request;

public abstract class HttpContent {
    // TYPE
    // NONE
    // form-data
    // x-www-form-urlencoded
    // row
    // binary
    // GraphQL
    public byte[] getBytes() { return null;}
    public String getString() { return null; }
}
