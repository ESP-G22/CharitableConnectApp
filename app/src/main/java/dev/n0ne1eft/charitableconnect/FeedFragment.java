package dev.n0ne1eft.charitableconnect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.TextView;
/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {
    private String pageTitle;

    public FeedFragment(String pageTitle) {
        if (pageTitle != null){
            this.pageTitle = pageTitle;
        }
        else {
            this.pageTitle = "";
        }
    }
    private View view;

    public FeedFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageTitle = getArguments().getString("TITLE"); // from bundle in explore buttons.
        } else {
            pageTitle = "Feed";
        }
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
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_main_activity);
                navController.navigate(R.id.newEventFragment);
            }
        });

        return view;
    }
    public void eventshownfromExplore(View v){
        if (pageTitle == "Subscribed") {
            //Only events we are subscribed to their organizer are shown
        } else if (pageTitle == "Date"){
            sorting("Date");
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("Date");
        } else if (pageTitle == "Trending") {
            sorting("Trending");
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("Trending");
        } else if (pageTitle == "FoodTasting") {
            //Only food tasting events are shown
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("FoodTasting");
        } else if (pageTitle == "Movies") {
            //Only movie events are shown
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("Movies");
        } else if (pageTitle == "Club") {
            //Only club events are shown
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("Club");
        } else if (pageTitle == "Sports") {
            //Only sports events are shown
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("Sports");
        } else {
            //All events are shown as before
            TextView textView2 = v.findViewById(R.id.feedTitleText);
            textView2.setText("Feed");
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