package api;

import android.media.Image;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import layout.EventCreate;
import layout.OutputPair;
import layout.UserProfile;
import layout.Event;

public class EventCreateAPI implements EventCreate {
    public OutputPair createEvent(int eventType, String title, String description, Date datetime, String address1, String address2, String postcode, Image image,
                                  String organiserToken) {
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("eventType", eventType);
        params.put("title", title);
        params.put("description", description);
        params.put("date", Util.dateToString(datetime));
        params.put("address1", address1);
        params.put("address2", address2);
        params.put("postcode", postcode);
        JSONObject input = new JSONObject(params);

        // Establish connection and post JSON parameters
        HttpURLConnection conn;
        try {
            URL url = new URL(Util.ENDPOINT_EVENT_CREATE);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", organiserToken);
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
        return new OutputPair(true, "Event has been created.");
    }
}
