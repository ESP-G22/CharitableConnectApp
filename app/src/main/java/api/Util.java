package api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public static final String ENDPOINT_EVENT_SEARCH = ENDPOINT_EVENT + "search";
    public static final String ENDPOINT_EVENT_CREATE = ENDPOINT_EVENT + "create";
    public static final String ENDPOINT_RSVP = ENDPOINT + "rsvp/";
    public static final String ENDPOINT_RSVP_CREATE = ENDPOINT_RSVP + "create";

    public static final String ENDPOINT_IMAGE = ENDPOINT + "images/";

    public static final String ENDPOINT_IMAGE_UPLOAD = ENDPOINT_IMAGE + "upload";

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String PROBLEM_WITH_SENDING_REQUEST = "Problem with sending the request.\n" +
            "Are you connected to the Internet?";

    /**
     * Get specific user by their id.
     *
     * @param id ID of user to get.
     * @return URL of user profile with given id.
     */
    public static String getUserEndpoint(int id) {
        return ENDPOINT_USER + "profile/" + Integer.valueOf(id).toString();
    }

    /**
     * Get specific event by their id.
     *
     * @param id ID of event to get.
     * @return URL of event.
     */
    public static String getEventEndpoint(int id) {
        return ENDPOINT_EVENT + Integer.valueOf(id).toString();
    }

    /**
     * Get specific rsvp by their id.
     *
     * @param id ID of rsvp to get.
     * @return URL of rsvp.
     */
    public static String getRSVPEndpoint(int id) {
        return ENDPOINT_RSVP + Integer.valueOf(id).toString();
    }

    /**
     * Get specific image by their id.
     *
     * @param id ID of image to get.
     * @return URL of image.
     */
    public static String getImageEndpoint(int id) {
        return ENDPOINT_IMAGE + Integer.valueOf(id).toString();
    }

    public static String getEventRSVPEndpoint(int id) {
        return Util.getEventEndpoint(id) + "/rsvp";
    }

    /**
     * Format token for authorisation header.
     *
     * @param token Token of user.
     * @return Formatted as 'Token {token}'.
     */
    public static String createToken(String token) {
        return "Token " + token;
    }

    /**
     * Pass JSON into output stream. Used in HTTP requests.
     *
     * @param conn HTTP connection.
     * @param params JSON body.
     * @throws IOException If the output stream cannot be obtained.
     */
    public static void passParams(HttpURLConnection conn, JSONObject params) throws IOException {
        OutputStream wr = conn.getOutputStream();
        wr.write(params.toString().getBytes("UTF-8"));
        wr.flush();
        wr.close();
    }

    /**
     * Convert response from HTTP request to JSON.
     *
     * @param stream Response from HTTP request. Correct or error stream.
     * @return Response stream in JSON.
     * @throws IOException If the stream cannot be read, or no lines can be read.
     * @throws JSONException If the response cannot be converted to JSON.
     */
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

        // Converting response to format that looks like an array so it can be converted to a
        // JSON array.
        if ((retStr.charAt(0) != '[') || (retStr.charAt(retStr.length() - 1) != ']')) {
            return new JSONArray("[" + retStr + "]");
        }
        return new JSONArray(retStr);
    }

    /**
     * Return status message from HTTP request whilst safely closing the connection.
     * @param conn HTTP connection.
     * @param out Output from calling function to return after closing the connection.
     * @return out
     */
    public static OutputPair disconnect(HttpURLConnection conn, OutputPair out) {
        conn.disconnect();
        return out;
    }

    /**
     * After sending a request, this determines what response to output to the screen or
     * how to proceed to get proper output.
     *
     * @param conn HTTP connection.
     * @return OutputPair containing several error messages if it did not work.
     */
    public static OutputPair checkResponseCode(HttpURLConnection conn) {
        try {
            int responseCode = conn.getResponseCode();
            if (responseCode == 404) {
                return Util.disconnect(conn, new OutputPair(false, "Could not contact server."));
            }
            if (responseCode == 403) {
                return Util.disconnect(conn, new OutputPair(false, "You do not have the permission to do this. Are you logged in as the correct user?"));
            }
            if ((responseCode > 299) || (responseCode < 200)) {
                return Util.disconnect(conn, new OutputPair(false, Util.getJSONResponse(conn.getErrorStream()).toString()));
            }
        } catch (IOException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with getting response"));
        } catch (JSONException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with parsing JSON"));
        }
        return new OutputPair(true, "No issues with response code");
    }

    /**
     * Converts date object to string using correct date format.
     *
     * @param datetime Date to convert to string.
     * @return String representation of datetime.
     */
    public static String dateToString(Date datetime) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date date = new Date();
        return formatter.format(date);
    }

    /**
     * Converts string object to date object using correct date format.
     *
     * @param datetime String to convert to Date.
     * @return Date representation of datetime.
     * @throws ParseException If the date cannot be formatted using DATE_FORMAT.
     */
    public static Date stringToDate(String datetime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.parse(datetime);
    }

    /**
     * Set an optional attribute value after successfully receiving the response.
     *
     * @param attrs All attributes.
     * @param fieldName Name of attribute.
     * @param defaultFieldValue If the value at fieldName is null, this value is used.
     * @return Value of attribute.
     * @throws JSONException If the attribute does not exist in attrs.
     */
    public static String setOptionalStringField(JSONObject attrs, String fieldName, String defaultFieldValue) throws JSONException {
        if (attrs.isNull(fieldName)) {
            return defaultFieldValue;
        }
        return attrs.getString(fieldName);
    }

    public static OutputPair getRequest(String urlStr, String authHeaderValue) {
        // Establish connection and post JSON parameters
        HttpURLConnection conn;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            conn.setDoOutput(true);
        } catch (IOException err) {
            return new OutputPair(false, Util.PROBLEM_WITH_SENDING_REQUEST);
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
            return Util.disconnect(conn, new OutputPair(true, out.toString()));
        } catch (IOException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with getting token"));
        } catch (JSONException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with parsing JSON"));
        }
    }

    public static OutputPair uploadImage(Bitmap image, String authHeaderValue) {
        // convert image to byte array for upload.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageContents = stream.toByteArray();
        image.recycle();

        HttpURLConnection conn;
        try {
            URL url = new URL(ENDPOINT_IMAGE_UPLOAD);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "image/jpeg");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            DataOutputStream imageStream = new DataOutputStream(out);

            imageStream.write(imageContents, 0, imageContents.length);
        } catch (IOException err) {
            return new OutputPair(false, Util.PROBLEM_WITH_SENDING_REQUEST);
        }

        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            return status;
        }

        return new OutputPair(true, "Image added");
    }

    public Bitmap getImage(int imageID, String authHeaderValue) throws IOException, Exception {
        HttpURLConnection conn;

        URL url = new URL(Util.getImageEndpoint(imageID));
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "image/jpeg");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", authHeaderValue);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        //Bitmap bitmap = BitmapFactory.decodeStream((InputStream) conn.getEntity().getContent());

        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            throw new Exception(status.getMessage());
        }

        //BitmapFactory.decodeByteArray(null, 0, 1);
        return null;
    }
}
