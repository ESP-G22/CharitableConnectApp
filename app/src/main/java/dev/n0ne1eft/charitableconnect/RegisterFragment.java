package dev.n0ne1eft.charitableconnect;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import api.UserGet;
import layout.OutputPair;

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

        EditText passwordView = view.findViewById(R.id.regPasswordInput);
        EditText confPasswordView = view.findViewById(R.id.confirmPasswordInput);
        EditText usernameView = view.findViewById(R.id.regUsernameInput);
        EditText emailView = view.findViewById(R.id.regEmailInput);

        //Sign up onclick
        Button confirmSignUp = view.findViewById(R.id.confirmSignUpButton);
        confirmSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameView.getText().toString();
                String email = emailView.getText().toString();
                String password = passwordView.getText().toString();
                String confPassword = confPasswordView.getText().toString();

                OutputPair output_register = ValidateSignUp(username, email, password, confPassword);
                boolean validated = output_register.isSuccess();

                if(!validated){
                    Toast.makeText(getActivity(), output_register.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                // clear previous input
                usernameView.setText("");
                passwordView.setText("");
                confPasswordView.setText("");
                emailView.setText("");

                // go back to login screen in order to get token.
                NavController navController = Navigation.findNavController(requireActivity(), R.id.loginNavHost);
                navController.navigate(R.id.loginFragment);

                Toast.makeText(getActivity(), output_register.getMessage(), Toast.LENGTH_LONG).show();

                /*
                //Launch main activity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                requireActivity().startActivity(intent);
                */
            }
        });
        //Sign in text onclick
        TextView launchSignIn = view.findViewById(R.id.launchSignInText);

        launchSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch sign in
                NavController navController = Navigation.findNavController(requireActivity(), R.id.loginNavHost);
                navController.navigate(R.id.loginFragment);
            }
        });

        return view;
    }

    /**
     * Evaluates a new user register request.
     *
     * @param username Inputted username.
     * @param email Inputted email. Must be in the correct email format.
     * @param password Inputted password.
     * @param confPassword Double-entry verification of password.
     * @return Output pair where if true, the message a success message, otherwise it
     *  contains an error message.
     */
    private OutputPair ValidateSignUp(String username, String email, String password, String confPassword) {
        // Check if they are not equal
        if(!password.equals(confPassword)){
            return new OutputPair(false, "Passwords must match.");
        }

        RegisterTask register = new RegisterTask(username, email, password);
        register.execute();
        try {
            OutputPair output_register = register.get(); // get return value from thread.
            return output_register;
        } catch (ExecutionException err) {
            return new OutputPair(false, "ExecutionError");
        } catch (InterruptedException err) {
            return new OutputPair(false, "InterruptedError");
        }
    }
}

class RegisterTask extends AsyncTask<String, String, OutputPair> {
    private String username;
    private String email;
    private String password;

    public RegisterTask(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
    }
    protected OutputPair doInBackground(String... params) {
        UserGet userGet = new UserGet();
        OutputPair output_register = userGet.register(email, username, password);

        return output_register;
    }
}