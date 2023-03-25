package dev.n0ne1eft.charitableconnect;

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

public class RegisterFragment extends Fragment {
    private View view;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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