package querylibrary.querybinder.Funtional;

import querylibrary.querybinder.QueryMap;

@FunctionalInterface
public interface GetQueryRequest {
    String getQueryRequest(QueryMap map);
}
