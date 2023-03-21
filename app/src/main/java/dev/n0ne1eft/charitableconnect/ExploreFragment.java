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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        Button subscribedButton = view.findViewById(R.id.SubscribedButton);
        subscribedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("Subscribed");
            }
        });
        Button dateButton = view.findViewById(R.id.DateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("Date");
            }
        });
        Button button3 = view.findViewById(R.id.TrendingButton);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("Trending");
            }
        });
        ImageView button4 = (ImageView) view.findViewById(R.id.foodImage);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("FoodTasting");
            }
        });
        ImageView button5 = (ImageView) view.findViewById(R.id.moviesImage);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("Movies");
            }
        });
        ImageView button6 = (ImageView) view.findViewById(R.id.clubImage);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("Club");
            }
        });
        ImageView button7 = (ImageView) view.findViewById(R.id.sportsImage);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("Sports");
            }
        });
        return view;
    }
    public String handletext(View v){
        EditText t = v.findViewById(R.id.SearchButton);
        String input = t.getText().toString();
        return input;
    }

    public void goToFeed(String category) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_main_activity);

        Bundle bundle = new Bundle();
        bundle.putString("TITLE", category);
        navController.navigate(R.id.feedFragment, bundle);
    }
}