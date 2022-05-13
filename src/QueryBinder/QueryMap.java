package QueryBinder;

import QueryBinder.Annotation.QueryBindingGetParam;
import QueryBinder.Annotation.QueryBindingParam;
import QueryBinder.Annotation.QueryBindingUrl;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

/**
 * 쿼리 요청을 위한 데이터 객체
 * @Author 신현진
 */
public class QueryMap extends java.util.HashMap<String, Object> {
    /// FIELDs
    private String url = "";

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
    public QueryMap(QueryRequestable obj) throws InvocationTargetException, IllegalAccessException, UnsupportedEncodingException {
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
        this.mappingMethods(obj);
        this.mappingFields(obj);
        
    }

    /**
     * Object에서 정의한 Annotation을 찾아서 바인딩하는 생성자.
     * @param obj
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public QueryMap(QueryBindingUrl obj) throws InvocationTargetException, IllegalAccessException, UnsupportedEncodingException {
        super();

        // URL 가져오기
        this.url = obj.value();

        this.mappingMethods(obj);
        try {
            this.mappingFields(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
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
    public String toQueryString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.url);
        sb.append("?");
        for (String key : this.keySet())
            sb.append(key + "=" + this.get(key) + "&");

        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private void mappingFields(Object obj) throws IllegalAccessException, UnsupportedEncodingException {
        for (Field field : obj.getClass().getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                // URL 가져오기
                if(annotation instanceof QueryBindingUrl) {
                    field.setAccessible(true);
                    this.url = (String) field.get(obj);
                    continue;
                }

                field.setAccessible(true);                                             // Field 접근 권한 설정
                String paramName = null;
                String defaultValue = null;
                boolean isRequired = false;
                boolean isEncode = false;
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
    }

    private void mappingMethods(Object obj) throws InvocationTargetException, IllegalAccessException, UnsupportedEncodingException {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                // URL 가져오기
                if(annotation instanceof QueryBindingUrl) {
                    method.setAccessible(true);
                    this.url = (String) method.invoke(obj);
                    continue;
                }

                method.setAccessible(true);                                             // Method 접근 권한 설정
                String paramName = null;
                String defaultValue = null;
                boolean isRequired = false;
                boolean isEncode = false;
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
    }

    // GETTERs
    public String getUrl() { return url; }

    // SETTERs
    public void setUrl(String url) { this.url = url; }
}
