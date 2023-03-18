package api;

import android.graphics.Bitmap;
import android.media.Image;
import org.json.JSONObject;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import layout.EventCreateProperties;
import layout.OutputPair;

public class EventCreate implements EventCreateProperties {
    public OutputPair createEvent(String eventType, String title, String description, Date datetime, String address1, String address2, String postcode, List<Bitmap> images,
                                  String organiserAuthHeaderValue) {

        HTTPConnection conn = new HTTPConnection();
        List<String> uuids = new LinkedList<>();

        for (int i = 0; i < images.size(); i++) {
            Bitmap image = images.get(i);
            OutputPair status_images = conn.postImage(Util.ENDPOINT_IMAGE_UPLOAD, image, organiserAuthHeaderValue);

            if (!status_images.isSuccess()) {
                return status_images;
            }
            uuids.add(status_images.getMessage());
        }

        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("eventType", eventType);
        params.put("title", title);
        params.put("description", description);
        params.put("date", Util.dateToString(datetime));
        params.put("address1", address1);
        params.put("address2", address2);
        params.put("postcode", postcode);
        params.put("images", uuids);
        JSONObject input = new JSONObject(params);

        // Establish connection and post JSON parameters
        OutputPair status = conn.post(Util.ENDPOINT_EVENT_CREATE, input, organiserAuthHeaderValue);
        conn.disconnect();

        if (!status.isSuccess()) {
            return status;
        }

        // If successful, output the success message
        return new OutputPair(true, "Event has been created.");
    }
}
