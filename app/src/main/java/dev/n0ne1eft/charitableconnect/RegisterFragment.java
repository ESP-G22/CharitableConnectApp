package dev.n0ne1eft.charitableconnect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        view = inflater.inflate(R.layout.fragment_register, container, false);
        // Inflate the layout for this fragment

        //Sign up onclick
        Button confirmSignUp = view.findViewById(R.id.confirmSignUpButton);
        confirmSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validated = ValidateSignUp(v);
                if(validated){
                    //Go to home page

                }
            }
        });

        return view;
    }

    private boolean ValidateSignUp(View v) {
        //Get first password as string
        EditText passwordView = view.findViewById(R.id.regPasswordInput);
        String password = passwordView.getText().toString();

        //Get confirmation password as string
        EditText confPasswordView = view.findViewById(R.id.confirmPasswordInput);
        String confPassword = confPasswordView.getText().toString();

        //Check if they are not equal
        if(!password.equals(confPassword)){
            //Raise toast
            Toast.makeText(getActivity(), "Passwords must match", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}