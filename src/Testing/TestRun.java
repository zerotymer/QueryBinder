package Testing;

import QueryBinder.QueryBinder;
import QueryBinder.QueryMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestRun {
    public static void main(String[] args) throws Exception {

        TestRequestVO testRequestVO = new TestRequestVO();
        QueryMap queryMap = new QueryMap(testRequestVO);
        List<?> list = QueryBinder.getRequestList(queryMap);     // One

        System.out.println(list);

//        System.out.println(map);
//        MyBoardVO vo = QueryBinder.<MyBoardVO>mapping(map, MyBoardVO.class);
//        System.out.println(vo.toString());



    }
}
