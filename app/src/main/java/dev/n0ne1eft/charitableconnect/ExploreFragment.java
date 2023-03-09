package dev.n0ne1eft.charitableconnect;

import android.os.Bundle;
import android.view.View.OnClickListener;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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
            FeedFragment feedPageFragment = new FeedFragment();
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
            return "Subscribed";
        }
        // Return null if the button clicked is not the one that triggers the page change
        return null;
    }
    public String changeToFeedPageDate(View v) {
        // Check if the button clicked is the one that triggers the page change
        if (v.getId() == R.id.DateButton) {
            // Create a new instance of the FeedPage fragment
            FeedFragment feedPageFragment = new FeedFragment();
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
            FeedFragment feedPageFragment = new FeedFragment();
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
            FeedFragment feedPageFragment = new FeedFragment();
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
            FeedFragment feedPageFragment = new FeedFragment();
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
            FeedFragment feedPageFragment = new FeedFragment();
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
            FeedFragment feedPageFragment = new FeedFragment();
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