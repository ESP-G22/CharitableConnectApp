package dev.n0ne1eft.charitableconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.ExecutionException;

import api.UserProfile;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;

    private UserProfile user;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            user = savedInstanceState.getParcelable("USER");
        } else {
            // Getting the user who logged in
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                // throw error if user cannot be found from parameter pass?
            }
            String token = extras.getString("TOKEN");
            int userID = extras.getInt("USERID");

            user = findUser(token, userID);

            if (user == null) {
                Toast.makeText(this, "Could not get user.", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        setContentView(R.layout.activity_main);

        //Navigation Bar Code
        bottomNav = findViewById(R.id.bottom_nav);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.feedFragment, R.id.exploreFragment, R.id.profileFragment).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_main_activity);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);
        bottomNav.setSelectedItemId(R.id.feedFragment);

        //bottomNav.setBackgroundColor(getResources().getColor(R.color.navBarColor, getTheme()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public UserProfile findUser(String token, int userID) {
        GetUserTask output = new GetUserTask(token, userID);
        output.execute();
        try {
            UserProfile user = output.get();  // get return value from thread.
            return user;
        } catch (ExecutionException err) {
            err.printStackTrace();
            return null;
        } catch (InterruptedException err) {
            err.printStackTrace();
            return null;
        }
    }

    public UserProfile getUser() {
        return user;
    }
}