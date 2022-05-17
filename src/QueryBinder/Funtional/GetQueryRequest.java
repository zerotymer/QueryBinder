package QueryBinder.Funtional;

import QueryBinder.QueryMap;

@FunctionalInterface
public interface GetQueryRequest {
    String getQueryRequest(QueryMap map);
}
