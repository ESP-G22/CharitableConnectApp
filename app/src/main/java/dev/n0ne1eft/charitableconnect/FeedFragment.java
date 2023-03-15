package dev.n0ne1eft.charitableconnect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.TextView;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        //Call the eventShownfromExplore() function to handle the events shown
        eventshownfromExplore(v);
        
        view = inflater.inflate(R.layout.fragment_feed, container, false);

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
            TextView textView2 = v.findViewById(R.id.textView2);
            textView2.setText("Date");
        } else if (pageTitle == "Trending") {
            sorting("Trending");
            TextView textView2 = v.findViewById(R.id.textView2);
            textView2.setText("Trending");
        } else if (pageTitle == "FoodTasting") {
            //Only food tasting events are shown
            TextView textView2 = v.findViewById(R.id.textView2);
            textView2.setText("FoodTasting");
        } else if (pageTitle == "Movies") {
            //Only movie events are shown
            TextView textView2 = v.findViewById(R.id.textView2);
            textView2.setText("Movies");
        } else if (pageTitle == "Club") {
            //Only club events are shown
            TextView textView2 = v.findViewById(R.id.textView2);
            textView2.setText("Club");
        } else if (pageTitle == "Sports") {
            //Only sports events are shown
            TextView textView2 = v.findViewById(R.id.textView2);
            textView2.setText("Sports");
        } else {
            //All events are shown as before
            TextView textView2 = v.findViewById(R.id.textView2);
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