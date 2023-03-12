package api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import layout.OutputPair;
import layout.UserGet;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.IOException;

public class UserGetAPI implements UserGet {
    public OutputPair login(String username, String password) {
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("username", username);
        params.put("password", password);
        JSONObject input = new JSONObject(params);

        // Establish connection and post JSON parameters
        HttpURLConnection conn;
        try {
            URL url = new URL(Util.ENDPOINT_LOGIN);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            Util.passParams(conn, input);
        } catch (IOException err) {
            return new OutputPair(false, "Problem with sending request");
        }

        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            return status;
        }

        // If successful, output the token
        String token;
        try {
            InputStream inputStream = conn.getInputStream();
            JSONArray out = Util.getJSONResponse(inputStream);
            JSONObject json = out.getJSONObject(0);
            token = json.getString("token");

            return Util.disconnect(conn, new OutputPair(true, token));
        } catch (IOException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with getting token"));
        } catch (JSONException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with parsing JSON"));
        }
    }

    public OutputPair register(String email, String username, String password) {
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("email", email);
        params.put("username", username);
        params.put("password", password);
        JSONObject input = new JSONObject(params);

        // Establish connection and post JSON parameters
        HttpURLConnection conn;
        try {
            URL url = new URL(Util.ENDPOINT_LOGIN);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            Util.passParams(conn, input);
        } catch (IOException err) {
            return new OutputPair(false, "Problem with sending request");
        }

        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            return status;
        }

        // If successful, output the success message
        return new OutputPair(true, "User has been added. You can now login.");
    }

    public OutputPair getUserID(String username, String token) {
        String authHeaderValue = Util.createToken(token);

        // Establish connection and post JSON parameters
        HttpURLConnection conn;
        try {
            URL url = new URL(Util.ENDPOINT_USER_LIST);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            conn.setDoOutput(true);
        } catch (IOException err) {
            return new OutputPair(false, "Problem with sending request");
        }

        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            return status;
        }

        // If successful, output the users
        try {
            InputStream inputStream = conn.getInputStream();
            JSONArray out = Util.getJSONResponse(inputStream);
            for (int i = 0; i < out.length(); i++) {
                JSONObject curr = out.getJSONObject(i);

                if (username.equals(curr.getString("username"))) {
                    int pk = curr.getInt("pk");
                    return Util.disconnect(conn, new OutputPair(true, Integer.toString(pk)));
                }
            }
            return Util.disconnect(conn, new OutputPair(false, "Username not found."));
        } catch (IOException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with getting token"));
        } catch (JSONException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with parsing JSON"));
        }
    }
}
