package com.appsbygreatness.alc4phase2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.firebase.ui.auth.AuthUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;


public class ListActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assert getSupportActionBar() !=null;
        getSupportActionBar().setTitle("TravelMantics");

        FirebaseUtils.openDatabaseReference("traveldeals", this);
        setupRecyclerView();

    }


    private void setupRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        DealAdapter dealAdapter = new DealAdapter();
        recyclerView.setAdapter(dealAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.list_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addNewDeal){
            Intent intent = new Intent(this, DealActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.logout){

            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseUtils.attachListener();
                        }
                    });
            FirebaseUtils.dettachListener();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtils.dettachListener();
    }

    @Override
    protected void onResume() {
        setupRecyclerView();
        FirebaseUtils.attachListener();
        super.onResume();
    }
}
