package user;

import java.util.List;

import event.Event;

public class Organiser extends User {
    private List<Event> eventsHosted;
    private String bio;

    public Organiser(
            int id, int count, String email, String name,
            List<Event> eventsHosted, String bio)
    {
        super(id, count, email, name);
        setEventsHosted(eventsHosted);
        setBio(bio);

    }

    public List<Event> getEventsHosted() { return eventsHosted; }

    private void setEventsHosted(List<Event> eventsHosted) {
        this.eventsHosted = eventsHosted;
    }

    public String getBio() { return bio; }

    private void setBio(String bio) {
        this.bio = bio;
    }

    public boolean createEvent(Event event) {
        return false;
    }

    public boolean deleteEvent(Event event) {
        return false;
    }
}
