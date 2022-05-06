package Testing;

import QueryBinder.Annotation.BindingMapper;
import QueryBinder.QueryBinder;
import QueryBinder.QueryMap;

import java.lang.reflect.InvocationTargetException;

public class TestRun {
    public static void main(String[] args) throws Exception {
        TestRequestVO testRequestVO = new TestRequestVO();
//        QueryMap queryMap = new QueryMap((BindingMapper) testRequestVO);
        QueryMap queryMap = new QueryMap(testRequestVO);
        QueryBinder binder = new QueryBinder();
        System.out.println(binder);
        binder.getQuery(queryMap);
    }
}
