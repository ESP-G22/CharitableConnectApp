package api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import layout.OutputPair;
import layout.UserGetProperties;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserGet implements UserGetProperties {
    public OutputPair login(String username, String password) {
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("username", username);
        params.put("password", password);
        JSONObject input = new JSONObject(params);

        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.post(Util.ENDPOINT_LOGIN, input);

        if (!status.isSuccess()) {
            try {
                JSONArray output = new JSONArray(status.getMessage());
                JSONObject error = output.getJSONObject(0);
                if (error.has("non_field_errors")) {
                    status.setMessage("Incorrect credentials");
                } else if (error.has("username")) {
                    status.setMessage("Username is invalid");
                } else if (error.has("password")) {
                    status.setMessage("Password is invalid");
                } else {
                    status.setMessage("Unknown error in getting response");
                }
            } catch (JSONException err) {
                status.setMessage("Unknown error in getting response");
            }

            conn.disconnect();
            return status;
        }

        // If successful, output the token
        String token;
        OutputPair status_token;
        try {
            JSONArray out = new JSONArray(status.getMessage());
            JSONObject json = out.getJSONObject(0);
            token = json.getString("token");

            status_token = new OutputPair(true, token);
        } catch (JSONException err) {
            status_token = new OutputPair(false, "Problem with parsing JSON");
        }

        conn.disconnect();
        return status_token;
    }

    public OutputPair register(String email, String username, String password) {
        // Check for empty input
        if ("".equals(username)) {
            return new OutputPair(false, "Username cannot be empty.");
        }
        if ("".equals(password)) {
            return new OutputPair(false, "Password cannot be empty.");
        }
        if ("".equals(email)) {
            return new OutputPair(false, "Email cannot be empty.");
        }
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("email", email);
        params.put("username", username);
        params.put("password", password);
        JSONObject input = new JSONObject(params);

        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.post(Util.ENDPOINT_REGISTER, input);
        conn.disconnect();

        if (!status.isSuccess()) {
            try {
                JSONArray output = new JSONArray(status.getMessage());
                JSONObject error = output.getJSONObject(0);
                if (error.has("email")) {
                    status.setMessage(error.getString("email"));
                } else {
                    status.setMessage("Unknown error in getting response");
                }
            } catch (JSONException err) {
                status.setMessage("Unknown error in getting response");
            }

            return status;
        }
        // If successful, output the success message
        return new OutputPair(true, "User has been added. You can now login.");
    }

    public OutputPair getUserID(String username, String token) {
        String authHeaderValue = Util.createToken(token);

        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.get(Util.ENDPOINT_USER_LIST, authHeaderValue);

        if (!status.isSuccess()) {
            return status;
        }

        // If successful, output the users
        try {
            JSONArray out = new JSONArray(status.getMessage());
            for (int i = 0; i < out.length(); i++) {
                JSONObject curr = out.getJSONObject(i);

                if (username.equals(curr.getString("username"))) {
                    int pk = curr.getInt("pk");
                    return new OutputPair(true, Integer.toString(pk));
                }
            }
            return new OutputPair(false, "Username not found.");
        } catch (JSONException err) {
            return new OutputPair(false, "Problem with parsing JSON");
        }
    }
}
