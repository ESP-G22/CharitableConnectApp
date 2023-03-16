package dev.n0ne1eft.charitableconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.ExecutionException;

import api.UserProfile;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;

    private ExploreFragment exploreFragment;
    private ProfileFragment profileFragment;
    private FeedFragment feedFragment;

    private UserProfile user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            user = (UserProfile) savedInstanceState.getParcelable("USER");
            //exploreFragment = (ExploreFragment) getSupportFragmentManager().getFragment(savedInstanceState, "explore");
            //profileFragment = (ProfileFragment) getSupportFragmentManager().getFragment(savedInstanceState, "profile");
            //feedFragment = (FeedFragment) getSupportFragmentManager().getFragment(savedInstanceState, "feed");
        } else {
            // Getting the user who logged in
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                // throw error if user cannot be found from parameter pass?
            }
            String token = extras.getString("TOKEN");
            int userID = extras.getInt("USERID");

            user = findUser(token, userID); // null check
        }

        setContentView(R.layout.activity_main);
        /*
        exploreFragment = ExploreFragment.newInstance(user);
        feedFragment = new FeedFragment();
        profileFragment = ProfileFragment.newInstance(user);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.exploreFragment, exploreFragment)
                .add(R.id.profileFragment, profileFragment)
                .add(R.id.feedFragment, feedFragment)
                .commit();

         */

        //Navigation Bar Code
        bottomNav = findViewById(R.id.bottom_nav);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.feedFragment, R.id.exploreFragment, R.id.profileFragment).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_main_activity);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);

        //bottomNav.setBackgroundColor(getResources().getColor(R.color.navBarColor, getTheme()));
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        //getSupportFragmentManager().putFragment(outState, "explore", exploreFragment);
        //getSupportFragmentManager().putFragment(outState, "profile", profileFragment);
        //getSupportFragmentManager().putFragment(outState, "feed", feedFragment);
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