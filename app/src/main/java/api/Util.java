package api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
     * @param uuid ID of image to get.
     * @return URL of image.
     */
    public static String getImageEndpoint(String uuid) {
        return ENDPOINT_IMAGE + uuid;
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

    public static String dateToPrettyString(Date datetime) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY @ HH:mm");

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

    public static Date valuesToDate(int year, int month, int day, int hour, int minute) throws ParseException {
        String y = Integer.toString(year);
        String mo = String.format("%02d", month);
        String d = String.format("%02d", day);

        String h = String.format("%02d", hour);
        String mi = String.format("%02d", minute);

        return Util.stringToDate(y + "-" + mo + "-" + d + "T" + h + ":" + mi + ":00Z");
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

    public static List<Bitmap> getImages(JSONArray uuids, String authHeaderValue) throws JSONException, IOException {
        List<Bitmap> images = new LinkedList<Bitmap>();

        for (int i = 0; i < uuids.length(); i++) {
            images.add(Util.getImage(uuids.getString(i), authHeaderValue));
        }

        return images;
    }

    public static Bitmap getImage(String uuid, String authHeaderValue) throws IOException {
        return null;
        //HTTPConnection conn = new HTTPConnection();
        //Bitmap image = conn.getImage(Util.getImageEndpoint(uuid), authHeaderValue);

        //return image;
    }
}
