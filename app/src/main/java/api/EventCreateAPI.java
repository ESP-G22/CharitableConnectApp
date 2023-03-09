package api;

import android.media.Image;
import java.util.Date;

import layout.EventCreate;
import layout.UserProfile;
import layout.Event;

public class EventCreateAPI implements EventCreate {
    public Event createEvent(int eventType, String title, String description, Date datetime, String location, Image image,
                           boolean hasRSVP, UserProfile organiser) {
        return null;
    }
}
