package dev.n0ne1eft.charitableconnect;

import android.os.Bundle;
import android.media.Image;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import api.UserProfile;

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
    View view = inflater.inflate(R.layout.fragment_profile, container, false);

    TextView subC = view.findViewById(R.id.subC);
        subC.setText("10");

    TextView profDesc = view.findViewById(R.id.profDesc);
        profDesc.setText("This is a normal description of an organization and what it aims to achieve as well.");

    TextView profName = view.findViewById(R.id.ProfileName);
        profName.setText(user.getUsername());

    Button editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener(){
        public void onClick(View v) { changeToEditProfile(v); }
    });

    Button adSButton = view.findViewById(R.id.adSet);
        editButton.setOnClickListener(new View.OnClickListener(){
        public void onClick(View v) { changeToAdvSet(v); }
    });
    //ScrollView layout = view.findViewById(R.id.subsScrollView);

    LinearLayout upCL = (LinearLayout)view.findViewById(R.id.upcLayout);
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
                fragmentTransaction.replace(R.id.textView, new FeedFragment());
                fragmentTransaction.addToBackStack(null);
                // Commit the transaction
                fragmentTransaction.commit();
            }
        });
        upCL.addView(t);
    }
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
                fragmentTransaction.replace(R.id.textView, new FeedFragment());
                fragmentTransaction.addToBackStack(null);
                // Commit the transaction
                fragmentTransaction.commit();
            }
        });
        Subs.addView(t);
    }
    LinearLayout posts = (LinearLayout)view.findViewById(R.id.postLayout);
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
                fragmentTransaction.replace(R.id.textView, new FeedFragment());
                fragmentTransaction.addToBackStack(null);
                // Commit the transaction
                fragmentTransaction.commit();
            }
        });
        posts.addView(t);
        }

        return view;
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
            fragmentTransaction.replace(R.id.textView, new FeedFragment());
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
            fragmentTransaction.replace(R.id.textView, new FeedFragment());
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