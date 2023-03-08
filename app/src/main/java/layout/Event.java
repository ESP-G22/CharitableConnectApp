package layout;

import android.media.Image;
import java.util.Date;

public interface Event {
    // get event attributes directly through source
    int getID();
    UserProfile getOrganiser();

    int getEventType();

    String getTitle();

    String getDescription();

    Date getDatetime();

    String getLocation();

    Image getImage();

    boolean getHasRSVP();

    // update event attributes through source
    void setOrganiser(UserProfile organiser);

    void setEventType(int eventType);

    void setTitle(String title);

    void setDescription(String Description);

    void setDatetime(Date datetime);

    void setLocation(String location);

    void setImage(Image image);

    void setHasRSVP(boolean hasRSVP);

    // delete event
    boolean deleteEvent();
}
