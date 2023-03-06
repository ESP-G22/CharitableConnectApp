package dev.n0ne1eft.charitableconnect;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import dev.n0ne1eft.charitableconnect.databinding.ActivityMainBinding;

public class RegisterActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_init_welcome);

        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        /*Navigation Bar Code
        bottomNav = findViewById(R.id.bottom_nav);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.feedFragment, R.id.exploreFragment, R.id.profileFragment).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_main_activity);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);*/
    }
}
