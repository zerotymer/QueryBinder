package querylibrary.querybinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import querylibrary.querybinder.Request.HttpRequestMethods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * USE PATTERN
 * @author 신현진
 */
public class QueryAdapter {
    /// FIELDs

    /// CONSTRUCTORs

    /// METHODs

    /**
     * 쿼리 요청을 실행하기 위한 메소드
     * @param query 쿼리 문자열(url)
     * @param method 쿼리 메소드(GET, POST, PUT, DELETE)
     * @return
     * @throws MalformedURLException
     */
    public String request(String query, HttpRequestMethods method)
            throws MalformedURLException {
        // URL check
        if (query.isEmpty()) throw new MalformedURLException("URL is empty");
        URL url = new URL(query);

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.toString());

            // Header 설정

            // Response
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream()));

            // TO STRING
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Query failed");
        }

        return null;
    }

    /**
     * 쿼리 요청을 실행하기 위한 메소드
     * @param map
     * @param method
     * @return
     * @throws MalformedURLException
     */
    public String request(QueryMap map, HttpRequestMethods method)
            throws MalformedURLException {
        // URL check
        if (map.getUrl().isEmpty()) throw new MalformedURLException("URL is empty");
        URL url = new URL(map.toQueryString());

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.toString());

            // Header 설정
            Map<?, ?> header = map.getHeader();
            if (header != null) {
                header.keySet().forEach(key -> {
                    conn.setRequestProperty(key.toString(), (String) header.get(key));
                });
            }

            // Body 설정


            // Post 전송
            byte[] bytes = map.getBytes();
            if (method == HttpRequestMethods.POST && bytes != null) {
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(bytes);
                os.flush();
            }

            // Response
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream()));

            // TO STRING
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Query failed");
        }

        return null;
    }


    /// Static
    /**
     * 쿼리 요청을 실행하기 위한 메소드
     * @param query 쿼리 문자열(url)
     * @param method 쿼리 메소드(GET, POST, PUT, DELETE)
     * @return
     * @throws MalformedURLException
     */
    public static String staticRequest(String query, HttpRequestMethods method)
            throws MalformedURLException {
        // URL check
        if (query.isEmpty()) throw new MalformedURLException("URL is empty");
        URL url = new URL(query);

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.toString());

            // Header 설정

            // Response
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream()));

            // TO STRING
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Query failed");
        }

        return null;
    }

    /**
     * 쿼리 요청을 실행하기 위한 메소드
     * @param map
     * @param method
     * @return
     * @throws MalformedURLException
     */
    public static String staticRequest(QueryMap map, HttpRequestMethods method)
            throws MalformedURLException {
        // URL check
        if (map.getUrl().isEmpty()) throw new MalformedURLException("URL is empty");
        URL url = new URL(map.toQueryString());

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.toString());

            // Header 설정
            Map<?, ?> header = map.getHeader();
            if (header != null) {
                header.keySet().forEach(key -> {
                    conn.setRequestProperty(key.toString(), (String) header.get(key));
                });
            }

            // Body 설정


            // Post 전송
            if (method == HttpRequestMethods.POST) {
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(map.getBytes());
                os.flush();
            }

            // Response
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream()));

            // TO STRING
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Query failed");
        }

        return null;
    }
}
