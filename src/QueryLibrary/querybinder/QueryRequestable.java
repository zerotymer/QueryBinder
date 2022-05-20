package querylibrary.querybinder;

import querylibrary.querybinder.Request.HttpRequestMethods;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

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

    default String request(HttpRequestMethods method)
            throws UnsupportedEncodingException, InvocationTargetException, IllegalAccessException, MalformedURLException {
        QueryMap map = new QueryMap(this);
        return QueryAdapter.staticRequest(map, method);
    }
}
