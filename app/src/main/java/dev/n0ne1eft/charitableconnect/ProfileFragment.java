package dev.n0ne1eft.charitableconnect;

import android.media.Image;
import android.os.Bundle;

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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LinearLayout Lay = this.findViewById(R.layout.)
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        //setContentView(R.layout.fragment_profile);
        /*for (int i=0; i<6; i++){
            Button btn;
            btn = new Button();
            btn.setId(i);
            btn.

        }*/
        //LinearLayout posts = findViewById(R.layout.fragment_profile);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView subC = view.findViewById(R.id.subC);
        subC.setText("10");

        TextView profDesc = view.findViewById(R.id.profDesc);
        profDesc.setText("This is a normal description of an organization and what it aims to achieve as well.");

        TextView profName = view.findViewById(R.id.ProfileName);
        profName.setText("Charitable Connect");

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
}