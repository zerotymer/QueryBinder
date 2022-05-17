package QueryBinder;

import QueryBinder.Annotation.QueryBindingGetParam;
import QueryBinder.Annotation.QueryBindingParam;
import QueryBinder.Annotation.QueryBindingUrl;
import QueryBinder.Funtional.GetQueryRequest;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 쿼리 요청을 위한 데이터 객체
 * @Author 신현진
 */
public class QueryMap extends java.util.HashMap<String, Object> {
    /// FIELDs
    private String url = null;

    /// CONSTRUCTORs
    /**
     * 기본 생성자
     */
    public QueryMap() {
        super();
    }

    /**
     * URL 값을 가진 생성자
     * @param url
     */
    public QueryMap(String url) {
        super();
        this.url = url;
    }

    /**
     * Object에서 정의한 Annotation을 찾아서 바인딩하는 생성자.
     * @param obj
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public QueryMap(QueryRequestable obj)
            throws InvocationTargetException, IllegalAccessException, UnsupportedEncodingException {
        super();

        // QueryRequestable 객체

        // URL 가져오기: 클래스에 정의된 것만 가져옴
        for (Annotation anno : obj.getClass().getDeclaredAnnotations()) {
            if (anno instanceof QueryBindingUrl) {
                this.url = ((QueryBindingUrl) anno).value();
                break;
            }
        }

        // Parameter 가져오기: 클래스내부에 정의된 것을 가져옴
        this.mappingMethods(obj, obj.getClass());
        this.mappingFields(obj, obj.getClass());
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
    public String toQueryString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.url);
        sb.append("?");
        for (String key : this.keySet())
            sb.append(key + "=" + this.get(key) + "&");

        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private <T extends QueryRequestable> void mappingFields(T obj, Class<?> cls)
            throws IllegalAccessException, UnsupportedEncodingException {

        for (Field field : cls.getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                // URL 가져오기
                if(annotation instanceof QueryBindingUrl) {
                    field.setAccessible(true);
                    this.url = (String) field.get(obj);
                    continue;
                }

                field.setAccessible(true);                                             // Field 접근 권한 설정
                String paramName = null, defaultValue = null;
                boolean isRequired = false, isEncode = false;
                // Parameter 가져오기
                if (annotation instanceof QueryBindingGetParam) {
                    QueryBindingGetParam param = (QueryBindingGetParam) annotation;
                    paramName = param.value();
                    defaultValue = param.defaultValue();
                    isRequired = param.isRequired();
                    isEncode = param.isEncode();
                }
                else if (annotation instanceof QueryBindingParam) {
                    QueryBindingParam param = (QueryBindingParam) annotation;
                    paramName = param.value();
                    defaultValue = param.defaultValue();
                    isRequired = param.isRequired();
                }

                if (paramName == null || paramName.isEmpty()) continue;
                // TODO: 형변환 처리

                String value = (String) field.get(obj);
                if (isEncode) value = URLEncoder.encode(value, "UTF-8");
                if (value != null && !value.isEmpty()) this.put(paramName, value);          // value 등록.
                else if (isRequired) this.put(paramName, defaultValue);                     // Default 등록
            }
        }

        // 재귀호출
        if (cls.getSuperclass() != null)
            this.mappingFields(obj, cls.getSuperclass());
    }

    private <T extends QueryRequestable> void mappingMethods(T obj, Class<?> cls)
            throws InvocationTargetException, IllegalAccessException, UnsupportedEncodingException {
        for (Method method : cls.getDeclaredMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                // URL 가져오기
                if(annotation instanceof QueryBindingUrl) {
                    method.setAccessible(true);
                    this.url = (String) method.invoke(obj);
                    continue;
                }

                method.setAccessible(true);                                             // Method 접근 권한 설정
                String paramName = null, defaultValue = null;
                boolean isRequired = false, isEncode = false;
                // Parameter 가져오기
                if (annotation instanceof QueryBindingGetParam) {
                    QueryBindingGetParam param = (QueryBindingGetParam) annotation;
                    paramName = param.value();
                    defaultValue = param.defaultValue();
                    isRequired = param.isRequired();
                    isEncode = param.isEncode();
                }
                else if (annotation instanceof QueryBindingParam) {
                    QueryBindingParam param = (QueryBindingParam) annotation;
                    paramName = param.value();
                    defaultValue = param.defaultValue();
                    isRequired = param.isRequired();
                }

                if (paramName == null || paramName.isEmpty()) continue;
                // TODO: 형변환 처리

                String value = (String) method.invoke(obj);
                if (isEncode) value = URLEncoder.encode(value, "UTF-8");
                if (value != null && !value.isEmpty()) this.put(paramName, value);          // value 등록.
                else if (isRequired) this.put(paramName, defaultValue);                     // Default 등록
            }
        }

        // 재귀호출
        if (cls.getSuperclass() != null)
            this.mappingMethods(obj, cls.getSuperclass());
    }

    // GETTERs
    public String getUrl() { return url; }

    // SETTERs
    public void setUrl(String url) { this.url = url; }
}
