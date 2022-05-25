package Testing;

import querylibrary.querybinder.QueryAdapter;
import querylibrary.querybinder.QueryMap;

public class TestRun {
    public static void main(String[] args) throws Exception {

        TestRequestVO testRequestVO = new TestRequestVO();
        QueryMap queryMap = new QueryMap(testRequestVO);
        QueryAdapter adapter = new QueryAdapter();
        System.out.println(adapter.request(queryMap));
//        JSONObject object = new JSONObject(adapter.request(queryMap, HttpRequestMethod.GET));     // One

//        System.out.println(object);
    }
}
