package com.appsbygreatness.alc4phase2;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.appsbygreatness.alc4phase2.ListActivity.RC_SIGN_IN;

public class FirebaseUtils {

    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    private static FirebaseUtils firebaseUtils;
    public static FirebaseStorage firebaseStorage;
    public static StorageReference storageReference;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseAuth.AuthStateListener authStateListener;
    public static Activity caller;
    public static boolean isAdmin;

    public static ArrayList<TravelDeal> deals;

    private FirebaseUtils(){
    }


    public static void openDatabaseReference(String location, final Activity callerActivity){
        if(firebaseUtils == null){
            firebaseUtils = new FirebaseUtils();
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;

            authStateListener= new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser() == null){
                        FirebaseUtils.signIn();

                    }else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);

                    }

                }
            };
            connectStorage();

        }
        deals = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference().child(location);
    }

    public static void attachListener(){
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public static void dettachListener(){
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private static void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    public static void connectStorage(){
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("deals_pictures");
    }


    private static void checkAdmin(String uid) {
        FirebaseUtils.isAdmin = false;

        DatabaseReference ref = firebaseDatabase.getReference().child("administrators")
                .child(uid);

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FirebaseUtils.isAdmin = true;

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(listener);
    }



}
