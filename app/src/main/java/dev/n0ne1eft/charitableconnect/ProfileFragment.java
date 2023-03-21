package dev.n0ne1eft.charitableconnect;

import android.os.AsyncTask;
import android.os.Bundle;
import android.media.Image;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import api.Event;
import api.UserProfile;
import layout.Pair;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String ARG_USER = "USER";
    private UserProfile user;

    private View view;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(UserProfile user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        user = activity.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView subC = view.findViewById(R.id.subC);
            subC.setText(Integer.toString(user.getFollowerCount()));

        TextView eventC = view.findViewById(R.id.eventC);
        eventC.setText(Integer.toString(user.getEventCount()));

        TextView profDesc = view.findViewById(R.id.profDesc);
            profDesc.setText(user.getBio());

        TextView profName = view.findViewById(R.id.ProfileName);
            profName.setText(user.getUsername());

        ImageView profilePic = (ImageView) view.findViewById(R.id.DisplayPic);
        if (user.getProfilePic() != null) {
            profilePic.setImageBitmap(user.getProfilePic());
        }

        Button editButton = view.findViewById(R.id.editButton);
            editButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) { changeToEditProfile(v); }
        });

        Button adSButton = view.findViewById(R.id.adSet);
            editButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) { changeToAdvSet(v); }
        });
        //ScrollView layout = view.findViewById(R.id.subsScrollView);

        Map<String, List<Event>> events = getEvents();

        LinearLayout upCL = (LinearLayout)view.findViewById(R.id.upcLayout);
        setEventPanel(events.get("UPCOMING"), upCL);

        LinearLayout Subs = (LinearLayout)view.findViewById(R.id.subsLayout);
        for (int i=0; i<100; i++ ) {
            ImageButton t;
            t = new ImageButton(getActivity());
            t.setId(i);
            t.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            t.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedFragment feedPageFragment = new FeedFragment();
                    // Get the FragmentManager
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    // Start a FragmentTransaction
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // Replace the current fragment with the FeedPage fragment
                    fragmentTransaction.replace(R.id.feedTitleText, new FeedFragment());
                    fragmentTransaction.addToBackStack(null);
                    // Commit the transaction
                    fragmentTransaction.commit();
                }
            });
            Subs.addView(t);
        }
        LinearLayout posts = (LinearLayout)view.findViewById(R.id.postLayout);
        setEventPanel(events.get("POSTS"), posts);

        return view;
    }

    public Map<String, List<Event>> getEvents() {
        // TODO: Improve error handling
        Map<String, List<Event>> output = new HashMap<>();
        GetMyEventsTask task1 = new GetMyEventsTask(user);
        GetUpcomingEventsTask task2 = new GetUpcomingEventsTask(user);

        Pair<String, List<Event>> task1_status;
        task1.execute();
        try {
            task1_status = task1.get();  // get return value from thread.
            output.put("POSTS", task1_status.arg2);
        } catch (ExecutionException err) {
        } catch (InterruptedException err) {
        }
        Pair<String, List<Event>> task2_status;
        task2.execute();
        try {
            task2_status = task2.get();  // get return value from thread.
            output.put("UPCOMING", task2_status.arg2);
        } catch (ExecutionException err) {
        } catch (InterruptedException err) {
        }

        return output;
    }

    public void setEventPanel(List<Event> events, LinearLayout layout) {
        for (int i = 0; i < events.size(); i++) {
            final Event event = events.get(i); // allows each event to go into each onclick

            View eventWidget = getLayoutInflater().inflate(R.layout.event_card, null);

            TextView titleText = eventWidget.findViewById(R.id.demoevent);
            titleText.setText(event.getTitle());

            TextView infoText = eventWidget.findViewById(R.id.demoorg);
            infoText.setText(event.getShortInfo());

            layout.addView(eventWidget);
            eventWidget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEvent(event);
                }
            });
        }
    }
    public void showEvent(Event event) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_main_activity);

        Bundle bundle = new Bundle();
        bundle.putParcelable("EVENT", event);
        navController.navigate(R.id.viewEventFragment, bundle);
    }

    public String changeToAdvSet(View v) {
        // Check if the button clicked is the one that triggers the page change
        if (v.getId() == R.id.adSet) {
            // Create a new instance of the FeedPage fragment
            FeedFragment feedPageFragment = new FeedFragment();
            // Get the FragmentManager
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            // Start a FragmentTransaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the current fragment with the FeedPage fragment
            fragmentTransaction.replace(R.id.feedTitleText, new FeedFragment());
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();
            // Return an empty string
            return "Advanced Settings";
        }
        // Return null if the button clicked is not the one that triggers the page change
        return null;
    }
    public String changeToEditProfile(View v) {
        // Check if the button clicked is the one that triggers the page change
        if (v.getId() == R.id.editButton) {
            // Create a new instance of the FeedPage fragment
            FeedFragment feedPageFragment = new FeedFragment();
            // Get the FragmentManager
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            // Start a FragmentTransaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the current fragment with the FeedPage fragment
            fragmentTransaction.replace(R.id.feedTitleText, new FeedFragment());
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();
            // Return an empty string
            return "Edit";
        }
        // Return null if the button clicked is not the one that triggers the page change
        return null;
    }
}

class GetUpcomingEventsTask extends AsyncTask<String, String, Pair<String, List<Event>>> {
    private UserProfile userRequester;

    public GetUpcomingEventsTask(UserProfile userRequester) {
        super();
        this.userRequester = userRequester;
    }

    protected Pair<String, List<Event>> doInBackground(String... params) {
        List<Event> events;
        try {
            List<Integer> ids = userRequester.getSubscribedEvents();
            events = Event.idsToEvents(ids, userRequester);
        } catch (Exception err) {
            err.printStackTrace();
            return new Pair(err.getMessage(), new LinkedList<Event>());
        }

        return new Pair("success", events);
    }
}

class GetMyEventsTask extends AsyncTask<String, String, Pair<String, List<Event>>> {
    private UserProfile userRequester;

    public GetMyEventsTask(UserProfile userRequester) {
        super();
        this.userRequester = userRequester;
    }

    protected Pair<String, List<Event>> doInBackground(String... params) {
        List<Event> events;
        try {
            events = Event.getEventsFromOrganiser(userRequester.getID(), userRequester);
        } catch (Exception err) {
            err.printStackTrace();
            return new Pair(err.getMessage(), new LinkedList<Event>());
        }

        return new Pair("success", events);
    }
}