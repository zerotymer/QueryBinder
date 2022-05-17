package Testing;

import QueryBinder.QueryBinder;
import QueryBinder.QueryMap;

import java.util.List;

public class TestRun {
    public static void main(String[] args) throws Exception {

        TestRequestVO testRequestVO = new TestRequestVO();
        QueryMap queryMap = new QueryMap(testRequestVO);
        List<?> list = QueryBinder.jsonToArray(QueryBinder.getRequestList(queryMap));     // One

        System.out.println(list);

//        System.out.println(map);
//        MyBoardVO vo = QueryBinder.<MyBoardVO>mapping(map, MyBoardVO.class);
//        System.out.println(vo.toString());



    }
}
