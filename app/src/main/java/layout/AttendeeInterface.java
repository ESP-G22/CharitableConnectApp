package layout;

import java.util.List;

public interface AttendeeInterface {
    List<Event> getEventsSubbedTo();

    List<Integer> getEventPreference();

    List<Integer> getOrganiserSubbedTo();

    void setEventsSubbedTo(List<Event> eventsSubbedTo);

    void setEventPreference(List<Integer> eventPreference);

    void setOrganiserSubbedTo(List<Integer> organiserSubbedTo);

    boolean addEvent(Event event);

    boolean removeEvent(Event event);

    boolean subscribe(Integer organiser);

    boolean unsubscribe(Integer organiser);
}
