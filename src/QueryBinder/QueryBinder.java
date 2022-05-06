package QueryBinder;

import QueryBinder.Request.HttpRequestMethods;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

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

    public HashMap<String, String> requestQuery(QueryMap query) {
        HashMap<String, String> response = new HashMap<>();
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

        return response;
    }
}
