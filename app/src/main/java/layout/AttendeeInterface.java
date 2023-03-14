package layout;

import java.util.List;

public interface AttendeeInterface {
    List<EventAttributes> getEventsSubbedTo();

    List<Integer> getEventPreference();

    List<Integer> getOrganiserSubbedTo();

    void setEventsSubbedTo(List<EventAttributes> eventsSubbedTo);

    void setEventPreference(List<Integer> eventPreference);

    void setOrganiserSubbedTo(List<Integer> organiserSubbedTo);

    boolean addEvent(EventAttributes event);

    boolean removeEvent(EventAttributes event);

    boolean subscribe(Integer organiser);

    boolean unsubscribe(Integer organiser);
}
