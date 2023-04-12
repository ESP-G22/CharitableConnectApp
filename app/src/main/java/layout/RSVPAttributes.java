package layout;

public interface RSVPAttributes {
    /**
     * The unique value for the RSVP.
     *
     * @return RSVP ID.
     */
    int getID();

    /**
     * Event that the RSVP is associated with.
     *
     * @return event ID.
     */
    int getEventID();

    /**
     * User that the RSVP is associated with.
     *
     * @return user ID.
     */
    int getUserID();
}
