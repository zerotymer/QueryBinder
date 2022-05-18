package querylibrary.querybinder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

/**
 * QuaryMap을 이용하여 요청가능
 * 
 * Functional Programming 지원
 */

public interface QueryRequestable {
    default QueryMap getQueryMap()
            throws InvocationTargetException, IllegalAccessException, UnsupportedEncodingException {
        return new QueryMap(this);
    }
}
