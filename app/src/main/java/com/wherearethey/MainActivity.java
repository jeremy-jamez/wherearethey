package com.wherearethey;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, FeedFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener{


    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }
    private static final String TAG = "AnonymousAuth";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction;

            final Fragment fragment1 = new HomeFragment();
            final Fragment fragment2 = new FeedFragment();
            final Fragment fragment3 = new MapFragment();
            final Fragment fragment4 = new NotificationFragment();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction = fm.beginTransaction();
                    transaction.replace(R.id.content, fragment1).commit();
                    return true;
                case R.id.navigation_dashboard:
                    transaction = fm.beginTransaction();
                    transaction.replace(R.id.content, fragment2).commit();
                    return true;
                case R.id.navigation_map:
                    transaction = fm.beginTransaction();
                    transaction.replace(R.id.content, fragment3).commit();
                    return true;
                case R.id.navigation_notifications:
                    transaction = fm.beginTransaction();
                    transaction.replace(R.id.content, fragment4).commit();
                    return true;
                default:
                    transaction = fm.beginTransaction();
                    transaction.replace(R.id.content, fragment1).commit();
                    break;
            }

            return false;
        }

    };

    private FloatingActionButton.OnClickListener mOnFABClicked
            = new FloatingActionButton.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, AddPersonActivity.class);
            startActivity(i);
        }
    };

    FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        signInAnonymously();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(mOnFABClicked);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signInAnonymously() {

        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END signin_anonymously]
    }

}
