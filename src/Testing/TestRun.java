package Testing;

import org.json.JSONObject;
import querylibrary.querybinder.QueryAdapter;
import querylibrary.querybinder.QueryMap;
import querylibrary.querybinder.Request.HttpRequestMethods;

import java.util.List;

public class TestRun {
    public static void main(String[] args) throws Exception {

        TestRequestVO testRequestVO = new TestRequestVO();
        QueryMap queryMap = new QueryMap(testRequestVO);
        QueryAdapter adapter = new QueryAdapter();
        System.out.println(adapter.request(queryMap));
//        JSONObject object = new JSONObject(adapter.request(queryMap, HttpRequestMethods.GET));     // One

//        System.out.println(object);
    }
}
