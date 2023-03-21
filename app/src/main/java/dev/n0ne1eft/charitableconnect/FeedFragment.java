package dev.n0ne1eft.charitableconnect;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
            pageTitle = getArguments().getString("TITLE"); // from bundle in explore buttons.
            byTitle = getArguments().getBoolean("SEARCH_BY_TITLE");
        } else {
            pageTitle = "Feed";
            byTitle = false;
        }
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
        if (byTitle) {
            title.setText("Results for \"" + pageTitle + "\"");
        } else {
            title.setText(pageTitle);
        }

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
                    Toast.makeText(getActivity(), "Only organisers can create events.", Toast.LENGTH_LONG).show();
                    return;
                }
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_main_activity);
                navController.navigate(R.id.newEventFragment);
            }
        });

        LinearLayout linlayout = view.findViewById(R.id.linlayout);

        List<Event> list;
        Pair<String,List<Event>> a = getEventsByTitle(pageTitle);
        list = a.arg2;

        if (list.isEmpty()) {
            Toast.makeText(getActivity(), "No events found for \"" + pageTitle + "\"", Toast.LENGTH_SHORT).show();
        }

        //View view2;
        for (int i = 0; i < list.size(); i++) {
            final Event event = list.get(i); // allows each event to go into each onclick

            View view2 = getLayoutInflater().inflate(R.layout.event_card, null);
            TextView textView = view2.findViewById(R.id.demoevent);
            textView.setText(event.getTitle());
            TextView textView2 = view2.findViewById(R.id.demoorg);
            textView2.setText(event.getEventType());
            linlayout.addView(view2);
            view2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEvent(event);
                }
            });
        }

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

}


class GetEventsTask extends AsyncTask<String, String, Pair<String, List<Event>>> {
    private String pageTitle;
    private boolean byTitle;
    private UserProfile userRequester;

    public GetEventsTask(String pageTitle, boolean byTitle, UserProfile userRequester) {
        super();
        this.pageTitle = pageTitle;
        this.byTitle = byTitle;
        this.userRequester = userRequester;
    }

    protected Pair<String, List<Event>> doInBackground(String... params) {
        List<Event> events;
        try {
            if (byTitle) {
                events = Event.search(pageTitle, userRequester);
            } else {
                if ("Subscribed".equals(pageTitle)) {
                    List<Integer> ids = userRequester.getSubscribedEvents();
                    events = Event.idsToEvents(ids, userRequester);
                    //Only events we are subscribed to their organizer are shown
                } else if ("Date".equals(pageTitle)) {
                    events = Event.getByDate(new Date(), userRequester);
                } else if ("Trending".equals(pageTitle)) {
                    events = Event.getTrendingEvents(userRequester);
                } else if ("FoodTasting".equals(pageTitle)) {
                    //Only food tasting events are shown
                    events = Event.getByEventType(pageTitle, userRequester);
                } else if ("Movies".equals(pageTitle)) {
                    //Only movie events are shown
                    events = Event.getByEventType(pageTitle, userRequester);
                } else if ("Club".equals(pageTitle)) {
                    //Only club events are shown
                    events = Event.getByEventType(pageTitle, userRequester);
                } else if ("Sports".equals(pageTitle)) {
                    //Only sports events are shown
                    events = Event.getByEventType(pageTitle, userRequester);
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