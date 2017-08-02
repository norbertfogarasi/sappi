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
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.cars.CarFeedFragment;
import com.lynxsolutions.intern.sappi.events.EventFeedFragment;
import com.lynxsolutions.intern.sappi.login.LoginActivity;
import com.lynxsolutions.intern.sappi.news.NewsFeedFragment;
import com.lynxsolutions.intern.sappi.profile.CircleTransform;
import com.lynxsolutions.intern.sappi.profile.FavoritesFragment;
import com.lynxsolutions.intern.sappi.profile.ProfileFragment;
import com.lynxsolutions.intern.sappi.profile.UserInfo;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private View navHeader;
    private ImageView bgImage, profileImage;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private TextView emailTextView;

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
        bgImage = (ImageView) navHeader.findViewById(R.id.nav_bg_image);
        profileImage = (ImageView) navHeader.findViewById(R.id.profile_photo);
        emailTextView = (TextView) navHeader.findViewById(R.id.email_text_at_naviagtion_drawer);

        addBackGroundPhotoToNavigationDrawer();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        switchToFragment(new NewsFeedFragment());
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
            LoginManager.getInstance().logOut();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void addBackGroundPhotoToNavigationDrawer() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference reToUser = FirebaseDatabase.getInstance().getReference("users");
        reToUser.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo info = dataSnapshot.getValue(UserInfo.class);
                emailTextView.setText(info.getEmail());
                try {
                    Glide.with(MainActivity.this).load(info.getPhoto()).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                            .into(bgImage);
                    Glide.with(MainActivity.this).load(info.getPhoto()).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                            .bitmapTransform(new CircleTransform(MainActivity.this))
                            .into(profileImage);
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

