package querylibrary.querybinder.Request;

/**
 * 바이너리 지원
 */
public class HttpContentBinary extends HttpContent {

    //region Fields ----------------------------------------------------------------------------------------------------
    private byte[] bytes;
    //endregion Fields -------------------------------------------------------------------------------------------------


    //region Constructors ----------------------------------------------------------------------------------------------
    public HttpContentBinary() {
        super();
    }
    public HttpContentBinary(byte[] bytes) {
        super();
        this.bytes = bytes;
    }
    //endregion Constructors -------------------------------------------------------------------------------------------


    //region Methods ---------------------------------------------------------------------------------------------------
    @Override
    public String getString() { throw new UnsupportedOperationException(); }
    //endregion Methods ------------------------------------------------------------------------------------------------


    //region Getters & Setters -----------------------------------------------------------------------------------------
    @Override
    public byte[] getBytes() { return bytes; }
    public void setBytes(byte[] bytes) { this.bytes = bytes; }
    //endregion Getters & Setters --------------------------------------------------------------------------------------

}
