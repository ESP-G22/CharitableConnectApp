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

import api.UserGet;
import api.UserProfile;
import layout.OutputPair;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        view = inflater.inflate(R.layout.fragment_login, container, false);

        //Sign in onclick
        Button signInButton = view.findViewById(R.id.signInButton);
        EditText usernameBox = (EditText) view.findViewById(R.id.usernameInput);
        EditText passwordBox = (EditText) view.findViewById(R.id.passwordInput);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameBox.getText().toString();
                String password = passwordBox.getText().toString();

                OutputPair output_login = validateSignIn(username, password);
                boolean validated = output_login.isSuccess();

                if (!validated) {
                    Toast.makeText(getActivity(), output_login.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                // clear previous input
                usernameBox.setText("");
                passwordBox.setText("");

                String token = output_login.getMessage();

                OutputPair output_getid = getUserID(username, token);

                if (!output_getid.isSuccess()) {
                    Toast.makeText(getActivity(), output_getid.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                int userID = Integer.parseInt(output_getid.getMessage());

                //Launch main activity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("TOKEN", token);
                intent.putExtra("USERID", userID);
                requireActivity().startActivity(intent);
            }
        });

        //Launch sign up text onclick
        TextView launchSignUp = view.findViewById(R.id.launchSignUpText);
        launchSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch sign up screen
                NavController navController = Navigation.findNavController(requireActivity(), R.id.loginNavHost);
                navController.navigate(R.id.registerFragment);
            }
        });
        return view;
    }

    /**
     * Evaluates sign in request and runs the task in a new thread.
     *
     * @param username Login username input.
     * @param password Login password input.
     * @return Output pair where if true, the message is the token, otherwise it
     *  contains an error message.
     */
    private OutputPair validateSignIn(String username, String password) {
        LoginTask login = new LoginTask(username, password);
        login.execute();
        try {
            OutputPair output_login = login.get();  // get return value from thread.
            return output_login;
        } catch (ExecutionException err) {
            return new OutputPair(false, "ExecutionError");
        } catch (InterruptedException err) {
            return new OutputPair(false, "InterruptedError");
        }
    }

    private OutputPair getUserID(String username, String token) {
        GetUserIDTask getid = new GetUserIDTask(username, token);
        getid.execute();
        try {
            OutputPair output = getid.get();  // get return value from thread.
            return output;
        } catch (ExecutionException err) {
            return new OutputPair(false, "ExecutionError");
        } catch (InterruptedException err) {
            return new OutputPair(false, "InterruptedError");
        }
    }
}

class LoginTask extends AsyncTask<String, String, OutputPair> {
    private String username;
    private String password;

    public LoginTask(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }
    protected OutputPair doInBackground(String... params) {
        UserGet userGet = new UserGet();
        OutputPair output_login = userGet.login(username, password);

        return output_login;
    }
}

class GetUserIDTask extends AsyncTask<String, String, OutputPair> {
    private String username;
    private String token;

    public GetUserIDTask(String username, String token) {
        super();
        this.username = username;
        this.token = token;
    }
    protected OutputPair doInBackground(String... params) {
        UserGet userGet = new UserGet();
        OutputPair output = userGet.getUserID(username, token);

        return output;
    }
}