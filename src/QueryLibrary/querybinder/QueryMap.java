package querylibrary.querybinder;

import querylibrary.querybinder.Annotation.QueryBindingGetParam;
import querylibrary.querybinder.Annotation.QueryBindingGetPostData;
import querylibrary.querybinder.Annotation.QueryBindingParam;
import querylibrary.querybinder.Annotation.QueryBindingUrl;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 쿼리 요청을 위한 데이터 객체
 * @Author 신현진
 */
public class QueryMap extends java.util.HashMap<String, Object> {
    /// FIELDs
    private String url = null;
    private Map<String, String> header = null;
    private byte[] data = null;

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

    /**
     * 클래스내부에 정의된 필드를 바인딩하는 메소드
     * @param obj 쿼리 정보를 담은 객체
     * @param cls 객체 타입
     * @param <T> 객체 타입
     * @throws IllegalAccessException
     * @throws UnsupportedEncodingException
     */
    private <T extends QueryRequestable> void mappingFields(T obj, Class<?> cls)
            throws IllegalAccessException, UnsupportedEncodingException {

        for (Field field : cls.getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                boolean access = field.isAccessible();
                field.setAccessible(true);                             // Method 접근 권한 설정
                // URL 가져오기
                if(annotation instanceof QueryBindingUrl) {
                    this.url = (String) field.get(obj);
                    field.setAccessible(access);
                    continue;
                }
                if(annotation instanceof QueryBindingGetPostData) {
                    byte[] data = (byte[]) field.get(obj);
                    if (data != null) this.data = data;
                    field.setAccessible(access);
                    continue;
                }

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

                String value = check(field.get(obj), defaultValue, isRequired, isEncode);
                if (value != null) this.put(paramName, value);
                field.setAccessible(access);
            }
        }

        // 재귀호출
        if (cls.getSuperclass() != null)
            this.mappingFields(obj, cls.getSuperclass());
    }

    /**
     * 클래스내부에 정의된 메소드를 바인딩하는 메소드
     * @param obj 쿼리 정보를 담은 객체
     * @param cls 객체 타입
     * @param <T> 객체 타입
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws UnsupportedEncodingException
     */
    private <T extends QueryRequestable> void mappingMethods(T obj, Class<?> cls)
            throws InvocationTargetException, IllegalAccessException, UnsupportedEncodingException {
        for (Method method : cls.getDeclaredMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                boolean access = method.isAccessible();
                method.setAccessible(true);                             // Method 접근 권한 설정
                // URL 가져오기
                if(annotation instanceof QueryBindingUrl) {
                    this.url = (String) method.invoke(obj);
                    method.setAccessible(access);
                    continue;
                }
                if(annotation instanceof QueryBindingGetPostData) {
                    byte[] data = (byte[]) method.invoke(obj);
                    if (data != null) this.data = data;
                    method.setAccessible(access);
                    continue;
                }

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
                    isEncode = param.isEncode();
                }

                if (paramName == null || paramName.isEmpty()) continue;                     // 이름 미지정

                // TODO: 형변환 처리
                
                String value = check(method.invoke(obj), defaultValue, isRequired, isEncode);
                if (value != null) this.put(paramName, value);                              // value 등록.

                method.setAccessible(access);
            }
        }

        // 재귀호출
        if (cls.getSuperclass() != null)
            this.mappingMethods(obj, cls.getSuperclass());
    }

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

    /**
     * 쿼리 정보를 URL로 바꿔주는 메소드
     * @return
     */
    public String toQueryString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.url);
        sb.append("?");
        for (String key : this.keySet())
            sb.append(key + "=" + this.get(key) + "&");

        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public void headerPut(String key, String value) {
        if (this.header == null) this.header = new HashMap<>();
        this.header.put(key, value);
    }

    private String check(Object value, String defaultValue, boolean isRequired, boolean isEncode)
            throws UnsupportedEncodingException {
        // 기본값과 비교
        String result = null;
        if (value != null) {
            result = String.valueOf(value);
            if (result.isEmpty()) result = null;
            else if (result.equals(defaultValue)) result = null;
        }

        if (result != null)     return isEncode ? URLEncoder.encode(result, "UTF-8"): result;
        else if (isRequired)    return isEncode ? URLEncoder.encode(defaultValue, "UTF-8"): defaultValue;
        else                    return null;
    }

    // GETTERs
    public String getUrl() { return url; }
    public Map<?, ?> getHeader() { return header; }
    public byte[] getBytes() { return data; }

    // SETTERs
    public void setUrl(String url) { this.url = url; }

    /**
     * Post에 사용될 데이터를 설정한다.
     * @param data
     */
    public void setBytes(byte[] data) { this.data = data; }
}
