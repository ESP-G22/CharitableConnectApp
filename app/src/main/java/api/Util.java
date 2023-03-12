package api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import layout.OutputPair;

public class Util {
    public static final String ENDPOINT = "https://api.cc.n0ne1eft.dev/";
    public static final String ENDPOINT_USER = ENDPOINT + "user/";
    public static final String ENDPOINT_USER_LIST = ENDPOINT_USER + "list";
    public static final String ENDPOINT_LOGIN = ENDPOINT_USER + "login";
    public static final String ENDPOINT_CHANGE_PASSWORD = ENDPOINT_USER + "passwordchange";
    public static final String ENDPOINT_REGISTER = ENDPOINT_USER + "register";

    public static final String ENDPOINT_EVENT = ENDPOINT + "events/";
    public static final String ENDPOINT_EVENT_LIST = ENDPOINT_EVENT + "list";
    public static final String ENDPOINT_EVENT_CREATE = ENDPOINT_EVENT + "create";
    public static final String ENDPOINT_RSVP = ENDPOINT + "rsvp/";

    public static String getUserEndpoint(int id) {
        return ENDPOINT_USER + "profile/" + Integer.valueOf(id).toString();
    }

    public static String getEventEndpoint(int id) {
        return ENDPOINT_EVENT + Integer.valueOf(id).toString();
    }

    public static String createToken(String token) {
        return "Token " + token;
    }

    public static void passParams(HttpURLConnection conn, JSONObject params) throws IOException {
        OutputStream wr = conn.getOutputStream();
        wr.write(params.toString().getBytes("UTF-8"));
        wr.flush();
        wr.close();
    }

    public static JSONArray getJSONResponse(InputStream stream) throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String lines;
        while ((lines = reader.readLine()) != null) {
            builder.append(lines);
            builder.append(System.getProperty("line.separator"));
        }

        String retStr = builder.toString().trim();
        reader.close();

        if ((retStr.charAt(0) != '[') || (retStr.charAt(retStr.length() - 1) != ']')) {
            return new JSONArray("[" + retStr + "]");
        }
        return new JSONArray(retStr);
    }

    public static OutputPair disconnect(HttpURLConnection conn, OutputPair out) {
        conn.disconnect();
        return out;
    }

    public static OutputPair checkResponseCode(HttpURLConnection conn) {
        try {
            int responseCode = conn.getResponseCode();
            if (responseCode == 404) {
                return Util.disconnect(conn, new OutputPair(false, "Could not contact server"));
            }
            if (responseCode == 403) {
                return Util.disconnect(conn, new OutputPair(false, "You do not have the permission to do this. Are you logged in as the correct user?"));
            }
            if (responseCode != 200) {
                return Util.disconnect(conn, new OutputPair(false, Util.getJSONResponse(conn.getErrorStream()).toString()));
            }
        } catch (IOException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with getting response"));
        } catch (JSONException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with parsing JSON"));
        }
        return new OutputPair(true, "No issues with response code");
    }

    public static String dateToString(Date datetime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date();
        return formatter.format(date);
    }

    public static Date stringToDate(String datetime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return formatter.parse(datetime);
    }
}
