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
import java.util.Map;

import layout.OutputPair;

public class Util {
    public static final String ENDPOINT = "https://api.cc.n0ne1eft.dev/";
    public static final String ENDPOINT_USER = ENDPOINT + "user/";
    public static final String ENDPOINT_USER_LIST = ENDPOINT_USER + "list?";
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
     * Converts date object to string using correct date format.
     *
     * @param datetime Date to convert to string.
     * @return String representation of datetime.
     */
    public static String dateToString(Date datetime) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

        return formatter.format(datetime);
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
}
