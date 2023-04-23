package dev.n0ne1eft.charitableconnect;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import api.Event;
import api.UserProfile;
import layout.Pair;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {
    private String pageTitle;
    private String titleKeyword;
    private boolean byTitle;

    private UserProfile user;

    private View view;

    public FeedFragment(String pageTitle) {
        if (pageTitle != null){
            this.pageTitle = pageTitle;
        }
        else {
            this.pageTitle = "";
        }
    }

    public FeedFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            titleKeyword = getArguments().getString("TITLE"); // from bundle in explore buttons.
            byTitle = getArguments().getBoolean("SEARCH_BY_TITLE");
        } else {
            titleKeyword = "Feed";
            byTitle = false;
        }
        pageTitle = getTitle();
        MainActivity activity = (MainActivity) getActivity();
        user = activity.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed, container, false);

        //eventshownfromExplore(v);

        view = inflater.inflate(R.layout.fragment_feed, container, false);

        TextView title = (TextView) view.findViewById(R.id.feedTitleText);
        title.setText(pageTitle);

        //Launch new event
        FloatingActionButton newEventButton = view.findViewById(R.id.launchNewEventButton);

        // activity doesn't run if you hide it.
        /*
        // Only organiser can create events.
        if (user.isAttendee()) {
            newEventButton.hide();
        }
         */
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isAttendee()) {
                    Toast.makeText(getActivity(), R.string.USER_CREATING_EVENT_ERROR, Toast.LENGTH_LONG).show();
                    return;
                }
                //FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                //transaction.replace(R.id.newEventFragment, new NewEventFragment());
                //transaction.addToBackStack(null);
                //transaction.commit();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_main_activity);
                navController.navigate(R.id.newEventFragment);
            }
        });

        LinearLayout linlayout = view.findViewById(R.id.linlayout);

        List<Event> list;
        Pair<String,List<Event>> a = getEventsByTitle(titleKeyword);
        list = a.arg2;

        if (list.isEmpty()) {
            String msg = getEmptyMessage();
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }

        //View view2;
        for (int i = 0; i < list.size(); i++) {
            final Event event = list.get(i); // allows each event to go into each onclick

            View view2 = getLayoutInflater().inflate(R.layout.event_card, null);
            TextView textView = view2.findViewById(R.id.demoevent);
            textView.setText(event.getTitle());
            TextView textView2 = view2.findViewById(R.id.demoorg);
            textView2.setText(event.getShortInfo());
            linlayout.addView(view2);
            view2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEvent(event);
                }
            });
        }

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .commit();

        return view;
    }

    public void showEvent(Event event) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_main_activity);

        Bundle bundle = new Bundle();
        bundle.putParcelable("EVENT", event);
        navController.navigate(R.id.viewEventFragment, bundle);
    }

    public void eventshownfromExplore(View v){
        pageTitle = "Trending";
        Pair<String, List<Event>> output = getEventsByTitle(pageTitle);
        if (!(output.arg1.equals("success"))) {
            Toast.makeText(getActivity(), output.arg1, Toast.LENGTH_LONG).show();
            return;
        }
        List<Event> events = output.arg2;

        if ("Subscribed".equals(pageTitle)) {
            //Only events we are subscribed to their organizer are shown
        } else if ("Date".equals(pageTitle)){
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("Date");
        } else if ("Trending".equals(pageTitle)) {
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("Trending");
        } else if ("FoodTasting".equals(pageTitle)) {
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("FoodTasting");
        } else if ("Movies".equals(pageTitle)) {
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("Movies");
        } else if ("Club".equals(pageTitle)) {
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("Club");
        } else if ("Sports".equals(pageTitle)) {
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("Sports");
        } else {
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("Feed");
        }

        // display events
        System.out.println(events);
    }

    public Pair<String, List<Event>> getEventsByTitle(String pageTitle) {
        GetEventsTask task = new GetEventsTask(pageTitle, byTitle, user);
        task.execute();
        try {
            Pair output = task.get();  // get return value from thread.
            return output;
        } catch (ExecutionException err) {
            return new Pair("ExecutionError", null);
        } catch (InterruptedException err) {
            return new Pair("InterruptedError", null);
        }
    }
    public void sorting(String type){
        /*
        const currentEvent;
        int j;
        if (type == "Date"){
            for (int i = 0; i < events.size() - 1; i++){
                currentEvent = events.get(i);
                j = i + 1;
                if (events.get(i).date > events.get(j).date){
                    events.get(i) = events.get(j);
                    events.get(j) = currentEvent;
                }
            }
        }
        else if (type == "Trending"){
            for (int i = 0; i < events.size() - 1; i++){
                currentEvent = events.get(i);
                j = i + 1;
                if (events.get(i).trending > events.get(j).trending){
                    events.get(i) = events.get(j);
                    events.get(j) = currentEvent;
                }
            }
        }
        */
    }
    public String getTitle() {
        if (byTitle) {
            return "Results for \"" + titleKeyword + "\"";
        }

        switch(titleKeyword) {
            case "sports":
                return getString(R.string.category_sports);
            case "community":
                return getString(R.string.category_community);
            case "localBusiness":
                return getString(R.string.category_local_business);
            case "climate":
                return getString(R.string.category_climate);
            case "date":
                return "Today";
            case "trending":
                return "Trending";
            case "subscribed":
                return "Subscribed";
            default:
                return "Feed";
        }
    }

    public String getEmptyMessage() {
        if (byTitle) {
            return "No results containing \"" + titleKeyword + "\"";
        }

        switch(titleKeyword) {
            case "sports":
                return "No events found for " + pageTitle;
            case "community":
                return "No events found for " + pageTitle;
            case "localBusiness":
                return "No events found for " + pageTitle;
            case "climate":
                return "No events found for " + pageTitle;
            case "date":
                return "No events found today";
            case "trending":
                return "No trending events found";
            case "subscribed":
                return "No subscribed events, get subscribing!";
            default:
                return "No events in feed";
        }
    }
}


class GetEventsTask extends AsyncTask<String, String, Pair<String, List<Event>>> {
    private String keyword;
    private boolean byTitle;
    private UserProfile userRequester;

    public GetEventsTask(String keyword, boolean byTitle, UserProfile userRequester) {
        super();
        this.keyword = keyword;
        this.byTitle = byTitle;
        this.userRequester = userRequester;
    }

    protected Pair<String, List<Event>> doInBackground(String... params) {
        List<Event> events;
        try {
            if (byTitle) {
                events = Event.search(keyword, userRequester);
            } else {
                if ("subscribed".equals(keyword)) {
                    List<Integer> ids = userRequester.getSubscribedEvents();
                    events = Event.idsToEvents(ids, userRequester);
                    //Only events we are subscribed to their organizer are shown
                } else if ("date".equals(keyword)) {
                    events = Event.getByDate(new Date(), userRequester);
                } else if ("trending".equals(keyword)) {
                    events = Event.getTrendingEvents(userRequester);
                } else if ("localBusiness".equals(keyword)) {
                    //Only food tasting events are shown
                    events = Event.getByEventType("localBusiness", userRequester);
                } else if ("climate".equals(keyword)) {
                    //Only movie events are shown
                    events = Event.getByEventType("climate", userRequester);
                } else if ("community".equals(keyword)) {
                    //Only club events are shown
                    events = Event.getByEventType("community", userRequester);
                } else if ("sports".equals(keyword)) {
                    //Only sports events are shown
                    events = Event.getByEventType("sports", userRequester);
                } else {
                    //All events are shown as before
                    events = Event.getEventsList(userRequester);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
            return new Pair(err.getMessage(), new LinkedList<Event>());
        }

        return new Pair("success", events);
    }
}