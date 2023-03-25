package dev.n0ne1eft.charitableconnect;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
                goToFeed("subscribed", false);
            }
        });
        Button dateButton = view.findViewById(R.id.DateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("date", false);
            }
        });
        Button button3 = view.findViewById(R.id.TrendingButton);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("trending", false);
            }
        });
        ImageView button4 = (ImageView) view.findViewById(R.id.climateImage);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("climate", false);
            }
        });
        ImageView button5 = (ImageView) view.findViewById(R.id.localBusinessesImage);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("localBusiness", false);
            }
        });
        ImageView button6 = (ImageView) view.findViewById(R.id.communityImage);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("community", false);
            }
        });
        ImageView button7 = (ImageView) view.findViewById(R.id.sportsImage);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeed("sports", false);
            }
        });

        EditText search = (EditText) view.findViewById(R.id.SearchButton);
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    goToFeed(search.getText().toString(), true);
                    // Perform action on key press
                    return true;
                }
                return false;
            }
        });

        return view;
    }
    public String handletext(View v){
        EditText t = v.findViewById(R.id.SearchButton);
        String input = t.getText().toString();
        return input;
    }

    public void goToFeed(String category, boolean byTitle) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_main_activity);

        Bundle bundle = new Bundle();
        bundle.putString("TITLE", category);
        bundle.putBoolean("SEARCH_BY_TITLE", byTitle);
        navController.navigate(R.id.feedFragment, bundle);
    }
}