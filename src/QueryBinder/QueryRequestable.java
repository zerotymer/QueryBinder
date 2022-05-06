package QueryBinder;

import java.util.HashMap;

/*
 * Query 요청을 위한 인터페이스
 */
public interface QueryRequestable<T extends QueryResponsible> {
    QueryResponsible requestQuery();
    HashMap<String, String> requestQueryToMap();

}
