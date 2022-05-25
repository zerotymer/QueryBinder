package querylibrary.querybinder;

import querylibrary.querybinder.Annotation.QueryBindingGetContent;
import querylibrary.querybinder.Annotation.QueryBindingGetParam;
import querylibrary.querybinder.Annotation.QueryBindingParam;
import querylibrary.querybinder.Annotation.QueryBindingUrl;
import querylibrary.querybinder.Request.HttpContent;
import querylibrary.querybinder.Request.HttpRequestMethod;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 쿼리 요청을 위한 데이터 객체
 * @Author 신현진
 * @Date 2022-05-24
 * @Version 1.0
 * compostion 방식으로 필드 정의
 */
public class QueryMap implements Map<String, String> {
    public enum MapType {
        PARAMS, HEADER
    }

    /// FIELDs ---------------------------------------------------------------------------------------------------------
    private String url = null;                                          // URL
    private HttpRequestMethod method;                                  // METHOD
    private final Map<String, String> params;                           // PARAMS
    // TODO: Authorization                                              // AUTHORIZATION
    private final Map<String, String> header;                           // HEADER
    private HttpContent content;                                        // CONTENT
    // TODO: Cookie                                                     // COOKIE

    /// Initializer Block ----------------------------------------------------------------------------------------------
    {
        this.method = HttpRequestMethod.GET;
        this.params = new HashMap<>();
        this.header = new HashMap<>();
        this.content = null;
    }

    /// CONSTRUCTORs ---------------------------------------------------------------------------------------------------
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
     * @implSpec Implementation Requirements:
     * <br> 1. 클래스에 정의한 {@link QueryBindingUrl}을 찾아서 URL 값을 설정한다.
     * <br> 2. {@link QueryRequestable.getMethod()}를 찾아서 METHOD 값을 설정한다.
     * <br> 3. 클래스에 정의한 {@link QueryBindingGetParam}({@link QueryBindingParam})을 찾아서 Method, Field를 찾아서 값을 설정한다.
     *
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
        
        // Method 설정
        this.method = obj.getMethod();

        // Parameter 가져오기: 클래스내부에 정의된 것을 가져옴
        this.mappingMethods(obj, obj.getClass());
        this.mappingFields(obj, obj.getClass());
    }

    /// METHODs --------------------------------------------------------------------------------------------------------

    /**
     * 클래스내부에 정의된 필드를 바인딩하는 메소드
     * @param obj 쿼리 정보를 담은 객체
     * @param cls 객체 타입
     * @param <T> 객체 타입
     * @throws IllegalAccessException
     * @throws UnsupportedEncodingException
     * @implSpec Implementation Requirements:
     * <br> 객체를 순회하며 필드에 정의된 {@link Annotation}을 찾아서 값을 설정한다.
     * <br> 1. {@link QueryBindingUrl} 이 정의되어 있으면 URL 값을 설정한다.
     * <br> 2. {@link QueryBindingGetContent} 이 정의되어 있으면 CONTENT 값을 설정한다.
     * <br> 3. {@link QueryBindingGetParam}({@link QueryBindingParam}) 이 정의되어 있으면 PARAMS 값을 설정한다.
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
                
                // BODY 가져오기
                if(annotation instanceof QueryBindingGetContent) {
                    Object content = field.get(obj);
                    if (!(content instanceof HttpContent))
                        throw new IllegalArgumentException("QueryBindingGetContent must be HttpContent");

                    this.content = (HttpContent) content;
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
     * @implSpec Implementation Requirements:
     * <br> 객체를 순회하며 메소드에 정의된 {@link Annotation}을 찾아서 값을 설정한다.
     * <br> 1. {@link QueryBindingUrl} 이 정의되어 있으면 URL 값을 설정한다.
     * <br> 2. {@link QueryBindingGetContent} 이 정의되어 있으면 CONTENT 값을 설정한다.
     * <br> 3. {@link QueryBindingGetParam}({@link QueryBindingParam}) 이 정의되어 있으면 PARAMS 값을 설정한다.
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
                
                // BODY 가져오기
                if(annotation instanceof QueryBindingGetContent) {
                    Object content = method.invoke(obj);
                    if (!(content instanceof HttpContent))
                        throw new IllegalArgumentException("QueryBindingGetContent must be HttpContent");

                    this.content = (HttpContent) content;
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

    /**
     * 값을 기본 값 등과 비교하여 가져옴.
     * @param value
     * @param defaultValue
     * @param isRequired
     * @param isEncode
     * @return 가져올 수 없거나 가져오지 않아도 되는 값은 null
     * @throws UnsupportedEncodingException
     * @implSpec Implementation Requirements:
     * <br> 1. 기본 값과 비교
     * <br> 2. 값이 없거나 가져오지 않아도 되는 값은 null
     */
    private String check(Object value, String defaultValue, boolean isRequired, boolean isEncode)
            throws UnsupportedEncodingException {
        // 기본값과 비교. TODO: 형변환 처리
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


    // Getter & Setter -------------------------------------------------------------------------------------------------

    /**
     * URL 정보를 가져옵니다.
     * @return
     */
    public String getUrl() { return url; }

    /**
     * URL 정보를 설정합니다.
     * @param url
     */
    public void setUrl(String url) { this.url = url; }

    /**
     * Http 요청 방식을 가져옵니다.
     * @return
     */
    public HttpRequestMethod getMethod() { return method; }

    /**
     * Http 요청 방식을 설정합니다.
     * @param method
     */
    public void setMethod(HttpRequestMethod method) { this.method = method; }

    /**
     * Content 정보를 가져옵니다. (POST에서 사용)
     * @return
     */
    public HttpContent getContent() { return content; }

    /**
     * Content 정보를 설정합니다. (POST에서 사용)
     * @param content
     */
    public void setContent(HttpContent content) { this.content = content; }

    /// Forwarding - params
    /**
     * Removes all of the mappings from this map (optional opertation).
     */
    public void clear() { this.params.clear(); }

    /**
     * Removes all of the mappings from this map (optional opertation).
     * @param type
     */
    public void clear(MapType type) {
        switch (type) {
            case HEADER: this.header.clear(); break;
            case PARAMS: this.params.clear(); break;
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Attempts to compute a mapping for the specified key and its current mapped value (or null if there is no current mapping).
     * @param key
     * @param remappingFunction
     * @return
     */
    public String compute(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        return this.params.compute(key, remappingFunction);
    }

    /**
     * Attempts to compute a mapping for the specified key and its current mapped value (or null if there is no current mapping).
     * @param key
     * @param remappingFunction
     * @param type
     * @return
     */
    public String compute(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction, MapType type) {
        switch (type) {
            case HEADER: return this.header.compute(key, remappingFunction);
            case PARAMS: return this.params.compute(key, remappingFunction);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * If the specified key is not already associated with a value (or is mapped to null), attempts to compute its value using the given mapping function and enters it into this map unless null.
     * @param key
     * @param mappingFunction
     * @return
     */
    public String computeIfAbsent(String key, Function<? super String, ? extends String> mappingFunction) {
        return this.params.computeIfAbsent(key, mappingFunction);
    }

    /**
     * If the specified key is not already associated with a value (or is mapped to null), attempts to compute its value using the given mapping function and enters it into this map unless null.
     * @param key
     * @param mappingFunction
     * @param type
     * @return
     */
    public String computeIfAbsent(String key, Function<? super String, ? extends String> mappingFunction, MapType type) {
        switch (type) {
            case HEADER: return this.header.computeIfAbsent(key, mappingFunction);
            case PARAMS: return this.params.computeIfAbsent(key, mappingFunction);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * If the value for the specified key is present and non-null, attempts to compute a new mapping given the key and its current mapped value.
     * @param key
     * @param remappingFunction
     * @return
     */
    public String computeIfPresent(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        return this.params.computeIfPresent(key, remappingFunction);
    }

    /**
     * If the value for the specified key is present and non-null, attempts to compute a new mapping given the key and its current mapped value.
     * @param key
     * @param remappingFunction
     * @param type
     * @return
     */
    public String computeIfPresent(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction, MapType type) {
        switch (type) {
            case HEADER: return this.header.computeIfPresent(key, remappingFunction);
            case PARAMS: return this.params.computeIfPresent(key, remappingFunction);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     * @param key
     * @return
     */
    public boolean containsKey(Object key) { return this.params.containsKey(key); }

    /**
     * Returns true if this map contains a mapping for the specified key.
     * @param key
     * @param type
     * @return
     */
    public boolean containsKey(Object key, MapType type) {
        switch (type) {
            case HEADER: return this.header.containsKey(key);
            case PARAMS: return this.params.containsKey(key);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Returns true if this map maps one or more keys to the specified value.
     * @param value
     * @return
     */
    public boolean containsValue(Object value) { return this.params.containsValue(value); }

    /**
     * Returns true if this map maps one or more keys to the specified value.
     * @param value
     * @param type
     * @return
     */
    public boolean containsValue(Object value, MapType type) {
        switch (type) {
            case HEADER: return this.header.containsValue(value);
            case PARAMS: return this.params.containsValue(value);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Returns a Set view of the mappings contained in this map.
     * @return
     */
    public Set<Map.Entry<String, String>> entrySet() { return this.params.entrySet(); }

    /**
     * Returns a Set view of the mappings contained in this map.
     * @param type
     * @return
     */
    public Set<Map.Entry<String, String>> entrySet(MapType type) {
        switch (type) {
            case HEADER: return this.header.entrySet();
            case PARAMS: return this.params.entrySet();
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Compares the specified object with a map for equality.
     * @param o
     * @param type
     * @return
     */
    public boolean equals(Object o, MapType type) {
        switch (type) {
            case HEADER: return this.header.equals(o);
            case PARAMS: return this.params.equals(o);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Performs the given action for each entry in this map until all entries have been processed or the action throws an exception.
     * @param action
     */
    public void forEach(BiConsumer<? super String, ? super String> action) { this.params.forEach(action); }

    /**
     * Performs the given action for each entry in this map until all entries have been processed or the action throws an exception.
     * @param action
     * @param type
     */
    public void forEach(BiConsumer<? super String, ? super String> action, MapType type) {
        switch (type) {
            case HEADER: this.header.forEach(action); break;
            case PARAMS: this.params.forEach(action); break;
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     * @param key
     * @return
     */
    public String get(Object key) { return this.params.get(key); }

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     * @param key
     * @param type
     * @return
     */
    public String get(Object key, MapType type) {
        switch (type) {
            case HEADER: return this.header.get(key);
            case PARAMS: return this.params.get(key);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key.
     * @param key
     * @param defaultValue
     * @return
     */
    public String getOrDefault(Object key, String defaultValue) { return this.params.getOrDefault(key, defaultValue); }

    /**
     * Returns the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key.
     * @param key
     * @param defaultValue
     * @param type
     * @return
     */
    public String getOrDefault(Object key, String defaultValue, MapType type) {
        switch (type) {
            case HEADER: return this.header.getOrDefault(key, defaultValue);
            case PARAMS: return this.params.getOrDefault(key, defaultValue);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Returns the hash code value for a map.
     * @param type
     * @return
     */
    public int hashCode(MapType type) {
        switch (type) {
            case HEADER: return this.header.hashCode();
            case PARAMS: return this.params.hashCode();
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Returns true if this map contains no key-value mappings.
     * @return
     */
    public boolean isEmpty() { return this.params.isEmpty(); }

    /**
     * Returns true if this map contains no key-value mappings.
     * @param type
     * @return
     */
    public boolean isEmpty(MapType type) {
        switch (type) {
            case HEADER: return this.header.isEmpty();
            case PARAMS: return this.params.isEmpty();
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Returns a Set view of the keys contained in this map.
     * @return
     */
    public Set<String> keySet() { return this.params.keySet(); }

    /**
     * Returns a Set view of the keys contained in this map.
     * @param type
     * @return
     */
    public Set<String> keySet(MapType type) {
        switch (type) {
            case HEADER: return this.header.keySet();
            case PARAMS: return this.params.keySet();
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * If the specified key is not already associated with a value or is associated with null, associates it with the given non-null value.
     * @param key key with which the resulting value is to be associated
     * @param value the non-null value to be merged with the existing value
     *        associated with the key or, if no existing value or a null value
     *        is associated with the key, to be associated with the key
     * @param remappingFunction the function to recompute a value if present
     * @return
     */
    public String merge(String key, String value, BiFunction<? super String,? super String,? extends String> remappingFunction) {
        return this.params.merge(key, value, remappingFunction);
    }

    /**
     * If the specified key is not already associated with a value or is associated with null, associates it with the given non-null value.
     * @param key
     * @param value
     * @param remappingFunction
     * @param type
     * @return
     */
    public String merge(String key, String value, BiFunction<? super String,? super String,? extends String> remappingFunction, MapType type) {
        switch (type) {
            case HEADER: return this.header.merge(key, value, remappingFunction);
            case PARAMS: return this.params.merge(key, value, remappingFunction);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Associates the specified value with the specified key in this map (optional operation).
     * @param key
     * @param value
     * @return
     */
    public String put(String key, String value) { return this.params.put(key, value); }

    /**
     * Associates the specified value with the specified key in this map (optional operation).
     * @param key
     * @param value
     * @param type
     * @return
     */
    public String put(String key, String value, MapType type) {
        switch (type) {
            case HEADER: return this.header.put(key, value);
            case PARAMS: return this.params.put(key, value);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Copies all of the mappings from the specified map to params map (optional operation).
     * @param m
     */
    public void putAll(Map<? extends String,? extends String> m) { this.params.putAll(m); }

    /**
     * Copies all of the mappings from the specified map to a map (optional operation).
     * @param m
     * @param type
     */
    public void putAll(Map<? extends String,? extends String> m, MapType type) {
        switch (type) {
            case HEADER: this.header.putAll(m); break;
            case PARAMS: this.params.putAll(m); break;
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * If the specified key is not already associated with a value (or is mapped to null) associates it with the given value and returns null, else returns the current value.
     * @param key
     * @param value
     * @return
     */
    public String putIfAbsent(String key, String value) { return this.params.putIfAbsent(key, value); }

    /**
     * If the specified key is not already associated with a value (or is mapped to null) associates it with the given value and returns null, else returns the current value.
     * @param key
     * @param value
     * @param type
     * @return
     */
    public String putIfAbsent(String key, String value, MapType type) {
        switch (type) {
            case HEADER: return this.header.putIfAbsent(key, value);
            case PARAMS: return this.params.putIfAbsent(key, value);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Removes the mapping for a key from this map if it is present (optional operation).
     * @param key
     * @return
     */
    public String remove(Object key) { return this.params.remove(key); }

    /**
     * Removes the mapping for a key from this map if it is present (optional operation).
     * @param key
     * @param type
     * @return
     */
    public String remove(Object key, MapType type) {
        switch (type) {
            case HEADER: return this.header.remove(key);
            case PARAMS: return this.params.remove(key);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to the specified value.
     * @param key
     * @param value
     * @return
     */
    public boolean remove(Object key, Object value) { return this.params.remove(key, value); }

    /**
     * Removes the entry for the specified key only if it is currently mapped to the specified value.
     * @param key
     * @param value
     * @param type
     * @return
     */
    public boolean remove(Object key, Object value, MapType type) {
        switch (type) {
            case HEADER: return this.header.remove(key, value);
            case PARAMS: return this.params.remove(key, value);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Replaces the entry for the specified key only if it is currently mapped to some value.
     * @param key
     * @param value
     * @return
     */
    public String replace(String key, String value) { return this.params.replace(key, value); }

    /**
     * Replaces the entry for the specified key only if it is currently mapped to some value.
     * @param key
     * @param value
     * @param type
     * @return
     */
    public String replace(String key, String value, MapType type) {
        switch (type) {
            case HEADER: return this.header.replace(key, value);
            case PARAMS: return this.params.replace(key, value);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Replaces the entry for the specified key only if currently mapped to the specified value.
     * @param key key with which the specified value is associated
     * @param oldValue value expected to be associated with the specified key
     * @param newValue value to be associated with the specified key
     * @return
     */
    @Override
    public boolean replace(String key, String oldValue, String newValue) { return this.params.replace(key, oldValue, newValue); }

    /**
     * Replaces the entry for the specified key only if currently mapped to the specified value.
     * @param key
     * @param oldValue
     * @param newValue
     * @param type
     * @return
     */
    public boolean replace(String key, String oldValue, String newValue, MapType type) {
        switch (type) {
            case HEADER: return this.header.replace(key, oldValue, newValue);
            case PARAMS: return this.params.replace(key, oldValue, newValue);
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Replaces each entry's value with the result of invoking the given function on that entry until all entries have been processed or the function throws an exception.
     * @param function
     */
    public void replaceAll(BiFunction<? super String, ? super String, ? extends String> function) { this.params.replaceAll(function); }

    /**
     * Replaces each entry's value with the result of invoking the given function on that entry until all entries have been processed or the function throws an exception.
     * @param function
     * @param type
     */
    public void replaceAll(BiFunction<? super String, ? super String, ? extends String> function, MapType type) {
        switch (type) {
            case HEADER: this.header.replaceAll(function); break;
            case PARAMS: this.params.replaceAll(function); break;
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     * @return
     */
    public int size() { return this.params.size(); }

    /**
     * Returns the number of key-value mappings in this map.
     * @param type
     * @return
     */
    public int size(MapType type) {
        switch (type) {
            case HEADER: return this.header.size();
            case PARAMS: return this.params.size();
            default: throw new AssertionError(MapType.class);
        }
    }

    /**
     * Returns a Collection view of the values contained in this map.
     * @return
     */
    public Collection<String> values() { return this.params.values(); }

    /**
     * Returns a Collection view of the values contained in this map.
     * @param type
     * @return
     */
    public Collection<String> values(MapType type) {
        switch (type) {
            case HEADER: return this.header.values();
            case PARAMS: return this.params.values();
            default: throw new AssertionError(MapType.class);
        }
    }

}
