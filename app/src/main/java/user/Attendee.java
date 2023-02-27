package user;

import java.util.List;
import event.Event;

public class Attendee extends User {
    private List<Event> eventsSubbedTo;
    private List<Integer> eventPreference;
    private List<Organiser> organiserSubbedTo;

    public Attendee(
            int id, int count, String email, String name,
            List<Event> eventsSubbedTo, List<Integer> eventPreference,
            List<Organiser> organiserSubbedTo)
    {
        super(id, count, email, name);
        setEventsSubbedTo(eventsSubbedTo);
        setOrganiserSubbedTo(organiserSubbedTo);
        setEventPreference(eventPreference);

    }

    public List<Event> getEventsSubbedTo() { return eventsSubbedTo; }

    private void setEventsSubbedTo(List<Event> eventsSubbedTo) {
        this.eventsSubbedTo = eventsSubbedTo;
    }

    public List<Integer> getEventPreference() { return eventPreference; }

    private void setEventPreference(List<Integer> eventPreference) {
        this.eventPreference = eventPreference;
    }

    public List<Organiser> getOrganiserSubbedTo() { return organiserSubbedTo; }

    private void setOrganiserSubbedTo(List<Organiser> organiserSubbedTo) {
        this.organiserSubbedTo = organiserSubbedTo;
    }

    public boolean addEvent(Event event) {
        return false;
    }

    private void postEvent(Event event) {
        return;
    }

    public boolean deleteEvent(Event event) {
        return false;
    }

    public boolean subscribe(Organiser organiser) {
        return false;
    }

    public boolean unsubscribe(Organiser organiser) {
        return false;
    }
}
