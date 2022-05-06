package Testing;

import QueryBinder.QueryBinder;
import QueryBinder.QueryMap;

import java.util.Map;

public class TestRun {
    public static void main(String[] args) throws Exception {
        TestRequestVO testRequestVO = new TestRequestVO();
//        QueryMap queryMap = new QueryMap((BindingMapper) testRequestVO);
        QueryMap queryMap = new QueryMap(testRequestVO);
        QueryBinder binder = new QueryBinder();
        Map<?, ?> map = binder.requestQuery(queryMap);

        MyBoardVO vo = new MyBoardVO();
        System.out.println("111: " + map);
        binder.test(vo, queryMap);
    }
}
