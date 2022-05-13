package QueryBinder;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * QuaryMap을 이용하여 요청가능
 */
public interface QueryRequestable {
    default QueryMap getQueryMap() throws InvocationTargetException, IllegalAccessException {
        return new QueryMap(this);
    }
}
