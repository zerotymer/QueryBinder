package querylibrary.querybinder;

import querylibrary.querybinder.Request.HttpContent;
import querylibrary.querybinder.Request.HttpRequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 실제 쿼리를 요청하기 위해 사용되는 클래스
 * @author 신현진
 */
public class QueryAdapter {
    //region Methods ---------------------------------------------------------------------------------------------------

    /**
     * 쿼리 요청을 실행하기 위한 메소드
     * @param query 쿼리 문자열(url)
     * @param method 쿼리 메소드(GET, POST, PUT, DELETE)
     * @return 쿼리 요청 결과
     * @throws MalformedURLException
     * @implSpec Implementation Requirements:
     * <br> {@link HttpURLConnection} 객체를 통하여 쿼리 요청을 실행한다.
     */
    public String request(String query, HttpRequestMethod method)
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
     * @return 쿼리 요청 결과
     * @throws MalformedURLException 잘못된 URL의 경우 {@link HttpURLConnection} 객체 생성시 오류 발생
     * @implSpec Implementation Requirements:
     * <br> {@link HttpURLConnection} 객체를 통하여 쿼리 요청을 실행한다.
     */
    public String request(QueryMap map)
            throws MalformedURLException {
        // URL check
        if (map.getUrl().isEmpty()) throw new MalformedURLException("URL is empty");
        URL url = new URL(map.toQueryString());

        HttpRequestMethod method = map.getMethod();

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.toString());

            // Header 설정
            if (!map.isEmpty(QueryMap.MapType.HEADER)) {
                map.keySet(QueryMap.MapType.HEADER).forEach(key -> {
                    conn.setRequestProperty(key, map.get(key, QueryMap.MapType.HEADER));
                });
            }

            // Content - Post 전송
            HttpContent content = map.getContent();
            if (content != null) {
                byte[] bytes = map.getContent().getBytes();
                if (method == HttpRequestMethod.POST && bytes != null) {
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    os.write(bytes);
                    os.flush();
                }
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
     * @return 쿼리 요청 결과
     * @throws MalformedURLException
     * @implSpec Implementation Requirements:
     * <br> {@link HttpURLConnection} 객체를 통하여 쿼리 요청을 실행한다.
     */
    public static String staticRequest(String query, HttpRequestMethod method)
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
     * @return 쿼리 요청 결과
     * @throws MalformedURLException 잘못된 URL의 경우 {@link HttpURLConnection} 객체 생성시 오류 발생
     * @implSpec Implementation Requirements:
     * <br> {@link HttpURLConnection} 객체를 통하여 쿼리 요청을 실행한다.
     */
    public static String staticRequest(QueryMap map)
            throws MalformedURLException {
        // URL check
        if (map.getUrl().isEmpty()) throw new MalformedURLException("URL is empty");
        URL url = new URL(map.toQueryString());

        HttpRequestMethod method = map.getMethod();

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.toString());

            // Header 설정
            if (!map.isEmpty(QueryMap.MapType.HEADER)) {
                map.keySet(QueryMap.MapType.HEADER).forEach(key -> {
                    conn.setRequestProperty(key, map.get(key, QueryMap.MapType.HEADER));
                });
            }

            // Content - Post 전송
            HttpContent content = map.getContent();
            if (content != null) {
                byte[] bytes = map.getContent().getBytes();
                if (method == HttpRequestMethod.POST && bytes != null) {
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    os.write(bytes);
                    os.flush();
                }
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

    //endregion Methods ------------------------------------------------------------------------------------------------
}
