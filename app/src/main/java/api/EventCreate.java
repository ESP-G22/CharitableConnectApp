package api;

import android.graphics.Bitmap;
import android.media.Image;

import org.json.HTTP;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import layout.EventCreateProperties;
import layout.OutputPair;

public class EventCreate implements EventCreateProperties {
    public OutputPair createEvent(String eventType, String title, String description, Date datetime, String address1, String address2, String postcode, Bitmap image,
                                  String organiserAuthHeaderValue) {
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
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.post(Util.ENDPOINT_EVENT_CREATE, input, organiserAuthHeaderValue);
        conn.disconnect();

        if (!status.isSuccess()) {
            return status;
        }

        //Util.uploadImage(image, organiserAuthHeaderValue);
        // If successful, output the success message
        return new OutputPair(true, "Event has been created.");
    }
}
