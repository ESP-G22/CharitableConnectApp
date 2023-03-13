package api;

public class Util {
    public static final String ENDPOINT = "https://api.cc.n0ne1eft.dev/";
    public static final String ENDPOINT_USER = ENDPOINT + "user/";
    public static final String ENDPOINT_USER_LIST = ENDPOINT_USER + "list";
    public static final String ENDPOINT_LOGIN = ENDPOINT_USER + "login";
    public static final String ENDPOINT_CHANGE_PASSWORD = ENDPOINT_USER + "passwordchange";
    public static final String ENDPOINT_REGISTER = ENDPOINT_USER + "register";

    public static final String ENDPOINT_EVENT = ENDPOINT + "events/";
    public static final String ENDPOINT_EVENT_LIST = ENDPOINT_EVENT + "list";
    public static final String ENDPOINT_EVENT_CREATE = ENDPOINT_EVENT + "create";

    public static final String getUserEndpoint(int id) {
        return ENDPOINT_USER + Integer.valueOf(id).toString();
    }

    public static final String getEventEndpoint(int id) {
        return ENDPOINT_EVENT + Integer.valueOf(id).toString();
    }
}
