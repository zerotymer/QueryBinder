package QueryBinder;

import QueryBinder.Annotation.BindingMapperParam;
import QueryBinder.Annotation.BindingMapperUrl;
import QueryBinder.Request.HttpRequestMethods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 * QueryMap.java
 * This class is used to store the query and its corresponding parameters.
 *
 */
public class QueryMap extends java.util.HashMap<String, Object> {
    /// FIELDs
    private String url = "";
    private HttpRequestMethods method = HttpRequestMethods.GET;

    /// CONSTRUCTORs
    public QueryMap() {
        super();
    }
    public QueryMap(String url) {
        super();
        this.url = url;
    }
    public QueryMap(BindingMapperUrl map) throws InvocationTargetException, IllegalAccessException {
        super();
        this.url = map.value();

        for (Method method : map.getClass().getDeclaredMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation instanceof BindingMapperParam) {
                    BindingMapperParam param = (BindingMapperParam) annotation;
                    this.put(param.value(), method.invoke(map));                         // Annotation 등록.
                }
            }
        }

        for (Field field : map.getClass().getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof BindingMapperParam) {
                    BindingMapperParam param = (BindingMapperParam) annotation;
                    this.put(param.value(), field.get(map));                         // Annotation 등록.
                }
            }
        }
    }
    public QueryMap(QueryRequestable<?> map) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        super();

        for (Annotation anno : map.getClass().getDeclaredAnnotations()) {
            if (anno instanceof BindingMapperUrl) this.url = ((BindingMapperUrl) anno).value();
            else if (anno instanceof BindingMapperParam) {
                BindingMapperParam param = (BindingMapperParam) anno;
                this.put(param.value(), map.getClass().getDeclaredMethod(param.value()).invoke(map));
            }
        }
    }

    /// METHODs
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.url);
        sb.append("?");
        for (String key : this.keySet())
            sb.append(key + "=" + this.get(key) + "&");

        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    // GETTERs
    public String getUrl() { return url; }
    public HttpRequestMethods getMethod() {return method; }

    // SETTERs
    public void setUrl(String url) { this.url = url; }
    public void setMethod(HttpRequestMethods method) { this.method = method; }

}
