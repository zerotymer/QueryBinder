package querylibrary.querybinder;

import querylibrary.querybinder.Request.HttpRequestMethod;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

/**
 * QuaryMap을 이용하여 요청가능
 * 
 * Functional Programming 지원
 */

public interface QueryRequestable {

    //region Inner Classes ---------------------------------------------------------------------------------------------
    //endregion Inner Classes  -----------------------------------------------------------------------------------------


    //region Fields ----------------------------------------------------------------------------------------------------
    //endregion Fields -------------------------------------------------------------------------------------------------


    //region Constructors ----------------------------------------------------------------------------------------------
    //endregion Constructors -------------------------------------------------------------------------------------------


    //region Methods ---------------------------------------------------------------------------------------------------
    default QueryMap getQueryMap()
            throws InvocationTargetException, IllegalAccessException, UnsupportedEncodingException {
        return new QueryMap(this);
    }

    default HttpRequestMethod getMethod() {
        return HttpRequestMethod.GET;
    }

    default String request()
            throws UnsupportedEncodingException, InvocationTargetException, IllegalAccessException, MalformedURLException {
        QueryMap map = new QueryMap(this);
        map.setMethod(HttpRequestMethod.GET);
        return QueryAdapter.staticRequest(map);
    }
    //endregion Methods ------------------------------------------------------------------------------------------------
}
