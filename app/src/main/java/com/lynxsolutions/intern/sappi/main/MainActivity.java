package com.lynxsolutions.intern.sappi.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.cars.CarFeedFragment;
import com.lynxsolutions.intern.sappi.events.EventFeedFragment;
import com.lynxsolutions.intern.sappi.login.LoginActivity;
import com.lynxsolutions.intern.sappi.news.NewsFeedFragment;
import com.lynxsolutions.intern.sappi.profile.FavoritesFragment;
import com.lynxsolutions.intern.sappi.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private View navHeader;
    private ImageView bgImage, profileImage;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        bgImage = (ImageView) findViewById(R.id.nav_bg_image);
        profileImage = (ImageView) findViewById(R.id.profile_photo);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        switchToFragment(new NewsFeedFragment());

        /*FirebaseAuth.getInstance().signInWithEmailAndPassword("kalmy07@yahoo.com", "123456")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Szevasz", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("Szevasz", "signInWithEmail:failed", task.getException());
                        }

                        // ...
                    }
                });

*/
    }

    public void switchToFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content, fragment).commit();
    }




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchToFragment(new NewsFeedFragment());
                    return true;
                case R.id.navigation_cars:
                    switchToFragment(new CarFeedFragment());
                    return true;
                case R.id.navigation_events:
                    switchToFragment(new EventFeedFragment());
                    return true;
            }
            return false;
        }

    };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {//for testing purpose
            switchToFragment(new ProfileFragment());
        } else if (id == R.id.nav_favorites) {
            switchToFragment(new FavoritesFragment());
        } else if (id == R.id.nav_signout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

}

