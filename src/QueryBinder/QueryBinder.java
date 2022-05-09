package QueryBinder;

import QueryBinder.Annotation.BindingMapperParam;
import QueryBinder.Request.HttpRequestMethods;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Testing.MyBoardVO;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class QueryBinder {
    /// FIELDs

    /// CONSTRUCTORs

    /// METHODs

    @Deprecated
    public static void getQuery(String query, HttpRequestMethods method) throws NotImplementedException {
        // TODO: not Implemented
        throw new NotImplementedException();
    }

    public static void getQuery(QueryMap query) {
        try {
            URL url = new URL(query.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(query.getMethod().toString());                // TODO: check
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

    public static Map<?, ?> requestQuery(QueryMap query) {
        try {
            URL url = new URL(query.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(query.getMethod().toString());                // TODO: check
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

    public static List<Map<?, ?>> requestQueryList(QueryMap query) {
        try {
            URL url = new URL(query.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(query.getMethod().toString());                // TODO: check
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

    @Deprecated
    public static QueryResponsible selectOne(QueryResponsible target, Map<?, ?> map)
            throws IllegalAccessException, InvocationTargetException {

        // Field annotations
        for (Field field : target.getClass().getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof BindingMapperParam) {
                    BindingMapperParam param = (BindingMapperParam) annotation;
                    if (map.containsKey(param.value()))
                        field.set(target, map.get(param.value()));
                }
            }
        }

        // Method annotations
        for (Method method : target.getClass().getDeclaredMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation instanceof BindingMapperParam) {
                    BindingMapperParam param = (BindingMapperParam) annotation;
                    if (map.containsKey(param.value()))
                        method.invoke(target, map.get(param.value()));
                }
            }
        }

        return target;                  // For Channing
    }

    /*
     * @param target
     *
     */
    public static <T extends QueryResponsible> T mapping(Map<?, ?> map, Class cls)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object target = cls.newInstance();

        // Field annotations
        for (Field field : target.getClass().getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof BindingMapperParam) {
                    BindingMapperParam param = (BindingMapperParam) annotation;
                    if (map.containsKey(param.value()))
                        field.set(target, map.get(param.value()));
                }
            }
        }

        // Method annotations
        for (Method method : target.getClass().getDeclaredMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation instanceof BindingMapperParam) {
                    BindingMapperParam param = (BindingMapperParam) annotation;
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

    public static <T extends QueryResponsible> List<T> mapping(List<Map<?, ?>> list, Class cls)
            throws InvocationTargetException, IllegalAccessException, InstantiationException {
        List<T> targets = new ArrayList<>();
        for (Map<?, ?> map : list) {
            QueryBinder.mapping(map, cls);
        }
        return targets;
    }
}
