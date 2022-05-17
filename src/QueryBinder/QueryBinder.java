package QueryBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class QueryBinder {
    /// FIELDs

    /// CONSTRUCTORs
    public QueryBinder() {
    }

    /// METHODs
    // Getters & Setters

    @Deprecated
    public void getQuery(QueryMap query) {
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
    public Map<?, ?> requestQuery(QueryMap query) {
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
    public List<Map<?, ?>> requestQueryList(QueryMap query) {
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
    public static String getRequest(String query) throws MalformedURLException {
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

            return response.toString();

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
    public static String getRequest(QueryMap map) throws MalformedURLException {
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

            return response.toString();

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
    public static String getRequestList(QueryMap map) throws MalformedURLException {
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

            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Query failed");
        }

        return null;
    }

    /**
     * Json을 Map으로 변환한다.
     * @param json
     * @return
     */
    public static Map<?, ?> JsonToMap(String json) {
        return new JSONObject(json).toMap();
    }

    /**
     * Json을 Array로 변환한다.
     * @param json
     * @return
     */
    public static List<?> jsonToArray(String json) {
        ArrayList<Map<?, ?>> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            jsonArray.forEach(jsonObject -> list.add(new JSONObject(jsonObject.toString()).toMap()));
        } catch (JSONException e) {
            e.printStackTrace();
            System.err.println(QueryBinder.class.getName() + " : " + e.getMessage());
            System.err.println("STRING DATA ========================================");
            System.err.println(json);
        }
        return list;
    }




    @Deprecated
    public Map<?, ?> postRequest(QueryMap map) {
        return null;
    }

    @Deprecated
    public Map<?, ?> postRequest(String url, QueryMap map) {
        return null;
    }

}
