package QueryBinder.Exception;

/**
 * URL not found exception
 * URL이 정의되지 않은 경우 발생하는 예외입니다.
 * @author 신현진
 */
public class NoUndefinedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NoUndefinedException(String message) {
        super(message);
    }
}
