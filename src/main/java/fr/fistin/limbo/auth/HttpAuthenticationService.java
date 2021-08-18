package fr.fistin.limbo.auth;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 19:43
 */
public abstract class HttpAuthenticationService {

    protected HttpURLConnection createUrlConnection(URL url) throws IOException {
        if (url != null) {
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);

            return connection;
        }
        return null;
    }

    public String sendPostRequest(URL url, String post, String contentType) throws IOException {
        final HttpURLConnection connection = this.createUrlConnection(url);
        final byte[] postBytes = post.getBytes(StandardCharsets.UTF_8);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
        connection.setRequestProperty("Content-Length", "" + postBytes.length);
        connection.setDoOutput(true);

        final OutputStream outputStream = connection.getOutputStream();

        outputStream.write(postBytes);
        outputStream.flush();
        outputStream.close();

        return this.readResponse(connection);
    }

    public String sendGetRequest(URL url) throws IOException {
        final HttpURLConnection connection = this.createUrlConnection(url);

        connection.setRequestMethod("GET");

        return this.readResponse(connection);
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        final int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder response = new StringBuilder();

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            return response.toString();
        } else {
            System.err.println("Request failed with " + responseCode + " as response code!");
        }
        return null;
    }

    public static String buildQuery(Map<String, Object> query) {
        if (query != null) {
            final StringBuilder builder = new StringBuilder();

            for (Map.Entry<String, Object> entry : query.entrySet()) {
                if (builder.length() > 0) {
                    builder.append('&');
                }

                try {
                    builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (entry.getValue() != null) {
                    builder.append('=');

                    try {
                        builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8.toString()));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            return builder.toString();
        }
        return null;
    }

    public static URL concatenateURL(URL url, String query) {
        try {
            return url.getQuery() != null && url.getQuery().length() > 0 ? new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "&" + query) : new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "?" + query);
        } catch (MalformedURLException var3) {
            throw new IllegalArgumentException("Could not concatenate given URL with GET arguments!", var3);
        }
    }

    public static URL constantURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException var2) {
            throw new Error("Couldn't create constant for " + url, var2);
        }
    }

}
