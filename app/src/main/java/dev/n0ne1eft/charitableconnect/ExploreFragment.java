package dev.n0ne1eft.charitableconnect;

import android.os.Bundle;
import android.view.View.OnClickListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.concurrent.ExecutionException;

import api.UserProfile;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  ExploreFragment extends Fragment {
    private static final String ARG_USER = "USER";

    private UserProfile user;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance(UserProfile user) {
        ExploreFragment fragment = new ExploreFragment();
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        Button button1 = view.findViewById(R.id.SubscribedButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToFeedPageSubscribed(v);
            }
        });
        Button button2 = view.findViewById(R.id.DateButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToFeedPageDate(v);
            }
        });
        Button button3 = view.findViewById(R.id.TrendingButton);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToFeedPageTrending(v);
            }
        });
        Button button4 = view.findViewById(R.id.FoodButton);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToFeedPageFoodTasting(v);
            }
        });
        Button button5 = view.findViewById(R.id.MoviesButton);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToFeedPageMovies(v);
            }
        });
        Button button6 = view.findViewById(R.id.ClubButton);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToFeedPageClubNights(v);
            }
        });
        Button button7 = view.findViewById(R.id.SportsButton);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToFeedPageSports(v);
            }
        });
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }
    public String changeToFeedPageSubscribed(View v) {
        // Check if the button clicked is the one that triggers the page change
        if (v.getId() == R.id.SubscribedButton) {
            // Create a new instance of the FeedPage fragment
            FeedFragment feedPageFragment = new FeedFragment("Subscribed");
            // Get the FragmentManager
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the current fragment with the FeedPage fragment
            fragmentTransaction.replace(R.id.textView, new FeedFragment());
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();
            // Return an empty string
            return "Subscribed";
        }
        // Return null if the button clicked is not the one that triggers the page change
        return null;
    }
    public String changeToFeedPageDate(View v) {
        // Check if the button clicked is the one that triggers the page change
        if (v.getId() == R.id.DateButton) {
            // Create a new instance of the FeedPage fragment
            FeedFragment feedPageFragment = new FeedFragment("Date");
            // Get the FragmentManager
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            // Start a FragmentTransaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the current fragment with the FeedPage fragment
            fragmentTransaction.replace(R.id.textView, new FeedFragment());
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();
            // Return an empty string
            return "Date";
        }
        // Return null if the button clicked is not the one that triggers the page change
        return null;
    }
    public String changeToFeedPageTrending(View v) {
        // Check if the button clicked is the one that triggers the page change
        if (v.getId() == R.id.TrendingButton) {
            // Create a new instance of the FeedPage fragment
            FeedFragment feedPageFragment = new FeedFragment("Trending");
            // Get the FragmentManager
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            // Start a FragmentTransaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the current fragment with the FeedPage fragment
            fragmentTransaction.replace(R.id.textView, new FeedFragment());
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();
            // Return an empty string
            return "Trending";
        }
        // Return null if the button clicked is not the one that triggers the page change
        return null;
    }
    public String changeToFeedPageFoodTasting(View v) {
        // Check if the button clicked is the one that triggers the page change
        if (v.getId() == R.id.FoodButton) {
            // Create a new instance of the FeedPage fragment
            FeedFragment feedPageFragment = new FeedFragment("FoodTasting");
            // Get the FragmentManager
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            // Start a FragmentTransaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the current fragment with the FeedPage fragment
            fragmentTransaction.replace(R.id.textView, new FeedFragment());
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();
            // Return an empty string
            return "FoodTasting";
        }
        // Return null if the button clicked is not the one that triggers the page change
        return null;
    }
    public String changeToFeedPageMovies(View v) {
        // Check if the button clicked is the one that triggers the page change
        if (v.getId() == R.id.MoviesButton) {
            // Create a new instance of the FeedPage fragment
            FeedFragment feedPageFragment = new FeedFragment("Movies");
            // Get the FragmentManager
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            // Start a FragmentTransaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the current fragment with the FeedPage fragment
            fragmentTransaction.replace(R.id.textView, new FeedFragment());
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();
            // Return an empty string
            return "Movies";
        }
        // Return null if the button clicked is not the one that triggers the page change
        return null;
    }
    public String changeToFeedPageClubNights(View v) {
        // Check if the button clicked is the one that triggers the page change
        if (v.getId() == R.id.ClubButton) {
            // Create a new instance of the FeedPage fragment
            FeedFragment feedPageFragment = new FeedFragment("Club");
            // Get the FragmentManager
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            // Start a FragmentTransaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the current fragment with the FeedPage fragment
            fragmentTransaction.replace(R.id.textView, new FeedFragment());
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();
            // Return an empty string
            return "Club";
        }
        // Return null if the button clicked is not the one that triggers the page change
        return null;
    }
    public String changeToFeedPageSports(View v) {
        // Check if the button clicked is the one that triggers the page change
        if (v.getId() == R.id.SportsButton) {
            // Create a new instance of the FeedPage fragment
            FeedFragment feedPageFragment = new FeedFragment("Sports");
            // Get the FragmentManager
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            // Start a FragmentTransaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the current fragment with the FeedPage fragment
            fragmentTransaction.replace(R.id.textView, new FeedFragment());
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();
            // Return an empty string
            return "Sports";
        }
        // Return null if the button clicked is not the one that triggers the page change
        return null;
    }
    public String handletext(View v){
        EditText t = v.findViewById(R.id.SearchButton);
        String input = t.getText().toString();
        return input;
    }
}