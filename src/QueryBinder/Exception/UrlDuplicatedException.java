package QueryBinder.Exception;

/**
 * URL duplicated exception.
 * URL 값이 2번 이상 정의되었을 경우 발생하는 예외입니다.
 * @author 신현진
 */
public class UrlDuplicatedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /// CONSTRUCTORs
    public UrlDuplicatedException() {
        super();
    }

    public UrlDuplicatedException(String message) {
        super(message);
    }
}
