package QueryBinder;

import QueryBinder.Annotation.BindingMapperParam;
import QueryBinder.Request.HttpRequestMethods;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.json.JSONObject;

public class QueryBinder {
    /// FIELDs

    /// CONSTRUCTORs

    /// METHODs

    public void getQuery(String query, HttpRequestMethods method) {

    }

    public void getQuery(QueryMap query) {
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

    public Map<?, ?> requestQuery(QueryMap query) {
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

    public QueryResponsible test(QueryResponsible target, Map<?, ?> map) throws IllegalAccessException, InvocationTargetException {

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
}
