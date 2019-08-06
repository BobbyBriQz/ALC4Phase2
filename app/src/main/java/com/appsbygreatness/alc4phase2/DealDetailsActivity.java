package com.appsbygreatness.alc4phase2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DealDetailsActivity extends AppCompatActivity {

    public static final int IMPORT_IMAGE = 5555;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TravelDeal deal;
    EditText titleET;
    EditText priceET;
    EditText descriptionET;
    ImageView imageView;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_deal);
        FirebaseUtils.openDatabaseReference("traveldeals",this);
        firebaseDatabase = FirebaseUtils.firebaseDatabase;
        databaseReference = FirebaseUtils.databaseReference;

        titleET = findViewById(R.id.titleDetailET);
        priceET = findViewById(R.id.priceDetailET);
        descriptionET = findViewById(R.id.descriptionDetailET);
        imageView = findViewById(R.id.imageDetail);

        Button importImageButton = findViewById(R.id.importImageButton);
        importImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivityForResult();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Insert Picture"), IMPORT_IMAGE);
            }
        });

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal == null){
            deal = new TravelDeal();
        }
        this.deal = deal;

        titleET.setText(deal.getTitle());
        priceET.setText(deal.getPrice());
        descriptionET.setText(deal.getDescription());
        Picasso.get().load(deal.getImageUrl()).into(imageView);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveDeal){

            saveDeal();
        }else if(item.getItemId() == R.id.deleteDeal){

            deleteDeal();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDeal() {

        deal.setTitle(titleET.getText().toString());
        deal.setPrice(priceET.getText().toString());
        deal.setDescription(descriptionET.getText().toString());
        if (imageUrl != null){
            deal.setImageUrl(imageUrl);
        }

        if(deal.getId() == null){
            //Save as new deal
            databaseReference.push().setValue(deal);
            Toast.makeText(this, "Saved new deal successfully", Toast.LENGTH_LONG).show();
        }else{
            //Replace existing
            databaseReference.child(deal.getId()).setValue(deal);
            Toast.makeText(this, "Saved existing deal successfully", Toast.LENGTH_LONG).show();
        }

        finish();

    }


    private void deleteDeal(){
        if (deal.getId() == null){
            Toast.makeText(this, "Please save the deal before deleting", Toast.LENGTH_LONG).show();
            return;
        }
        databaseReference.child(deal.getId()).removeValue();
        Toast.makeText(this, "Deleted deal successfully", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMPORT_IMAGE && resultCode == RESULT_OK){
            final ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);

            final Uri imageUri = data.getData();

            Log.i("Url", "About to get Uri");


            final StorageReference ref = FirebaseUtils.storageReference.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrl = uri.toString();
                            deal.setImageUrl(uri.toString());
                            Log.i("Url", "download url is: "+uri.toString());
                            progressBar.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageURI(imageUri);
                            Toast.makeText(getApplicationContext(), "Image uploaded sucsessfully", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }
}
