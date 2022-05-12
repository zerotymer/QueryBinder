package QueryBinder;

import QueryBinder.Annotation.QueryBindingParam;
import QueryBinder.Exception.NoUrlExcption;
import QueryBinder.Request.HttpRequestMethods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * USE SINGLETON PATTERN
 * @author 신현진
 */
public class QueryBinder {
    /// FIELDs

    /// CONSTRUCTORs

    /// METHODs

    @Deprecated
    public static void getQuery(QueryMap query) {
        try {
            URL url = new URL(query.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");                // TODO: check
            for (String key : query.keySet()) {
                conn.setRequestProperty(key, query.get(key).toString());
            }

            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(
                                        responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.err.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static Map<?, ?> requestQuery(QueryMap query) {
        try {
            URL url = new URL(query.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");                // TODO: check
            for (String key : query.keySet()) {
                conn.setRequestProperty(key, query.get(key).toString());
            }

            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return new JSONObject(response.toString()).toMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Deprecated
    public static List<Map<?, ?>> requestQueryList(QueryMap query) {
        try {
            URL url = new URL(query.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");                // TODO: check
            for (String key : query.keySet()) {
                conn.setRequestProperty(key, query.get(key).toString());
            }

            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            ArrayList<Map<?, ?>> list = new ArrayList();
            JSONArray jsonArray = new JSONArray(response.toString());
            jsonArray.forEach(json -> {
                list.add(new JSONObject(json.toString()).toMap());
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * GET 방식의 쿼리를 요청하여 결과를 반환한다.
     * @param query
     * @return
     * @throws MalformedURLException
     */
    public static Map<?, ?> getRequest(String query) throws MalformedURLException {
        // URL check
        if (query.isEmpty()) throw new MalformedURLException("URL is empty");
        URL url = new URL(query);

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Response
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return new JSONObject(response.toString()).toMap();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Query failed");
        }

        return null;
    }

    /**
     * GET 방식의 쿼리를 요청하여 결과를 반환한다.
     * @param map
     * @return
     * @throws MalformedURLException
     */
    public static Map<?, ?> getRequest(QueryMap map) throws MalformedURLException {
        // URL check
        if (map.getUrl().isEmpty()) throw new MalformedURLException("URL is empty");
        URL url = new URL(map.toQueryString());

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Set Property - for matadata
//            map.keySet().forEach(key -> conn.setRequestProperty(key, map.get(key).toString()));

            // Response
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return new JSONObject(response.toString()).toMap();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Query failed");
        }

        return null;
    }

    /**
     * GET 방식의 쿼리를 요청하여 결과를 반환한다. (리스트)
     * @param map
     * @return
     * @throws MalformedURLException
     */
    public static List<?> getRequestList(QueryMap map) throws MalformedURLException {
        // URL check
        if (map.getUrl().isEmpty()) throw new MalformedURLException("URL is empty");
        URL url = new URL(map.toQueryString());

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Set Property - for metadata
//            map.keySet().forEach(key -> conn.setRequestProperty(key, map.get(key).toString()));

            // Response
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            ArrayList<Map<?, ?>> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(response.toString());
            jsonArray.forEach(json -> list.add(new JSONObject(json.toString()).toMap()));
            return list;

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Query failed");
        }

        return null;
    }







    @Deprecated
    public static Map<?, ?> postRequest(QueryMap map) {
        return null;
    }

    @Deprecated
    public static Map<?, ?> postRequest(String url, QueryMap map) {
        return null;
    }










    @Deprecated
    public static QueryResponsible selectOne(QueryResponsible target, Map<?, ?> map)
            throws IllegalAccessException, InvocationTargetException {

        // Field annotations
        for (Field field : target.getClass().getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof QueryBindingParam) {
                    QueryBindingParam param = (QueryBindingParam) annotation;
                    if (map.containsKey(param.value()))
                        field.set(target, map.get(param.value()));
                }
            }
        }

        // Method annotations
        for (Method method : target.getClass().getDeclaredMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation instanceof QueryBindingParam) {
                    QueryBindingParam param = (QueryBindingParam) annotation;
                    if (map.containsKey(param.value()))
                        method.invoke(target, map.get(param.value()));
                }
            }
        }

        return target;                  // For Channing
    }

    @Deprecated
    public static <T extends QueryResponsible> T mapping(Map<?, ?> map, Class cls)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object target = cls.newInstance();

        // Field annotations
        for (Field field : target.getClass().getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof QueryBindingParam) {
                    QueryBindingParam param = (QueryBindingParam) annotation;
                    if (map.containsKey(param.value()))
                        field.set(target, map.get(param.value()));
                }
            }
        }

        // Method annotations
        for (Method method : target.getClass().getDeclaredMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation instanceof QueryBindingParam) {
                    QueryBindingParam param = (QueryBindingParam) annotation;
                    if (map.containsKey(param.value()))
                        method.invoke(target, map.get(param.value()).toString());
                    else
                        method.invoke(target, param.defaultValue());
                }
            }
        }

        return (T) target;
//        return target;                  // For Channing
    }

    @Deprecated
    public static <T extends QueryResponsible> List<T> mapping(List<Map<?, ?>> list, Class cls)
            throws InvocationTargetException, IllegalAccessException, InstantiationException {
        List<T> targets = new ArrayList<>();
        for (Map<?, ?> map : list) {
            QueryBinder.mapping(map, cls);
        }
        return targets;
    }
}
