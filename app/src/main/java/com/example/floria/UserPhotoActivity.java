package com.example.floria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.floria.modelo.Card_Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class UserPhotoActivity extends AppCompatActivity {

    ImageView ivPhoto;

    Toolbar toolbar;
    Uri myUri;

    Button btnPhoto;

    ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    EditText etDescripcion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photo);

        toolbar = findViewById(R.id.tbPhoto);
        ivPhoto = findViewById(R.id.ivPhoto);
        btnPhoto = findViewById(R.id.btnPhoto);
        etDescripcion = findViewById(R.id.etDescripcion);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initStorageFireBase();

        progressDialog = new ProgressDialog(this);

        myUri = Uri.parse(getIntent().getExtras().getString("imageUri"));

        System.out.println(myUri.getLastPathSegment());

        Picasso.with(this).load(myUri).fit().centerCrop().into(ivPhoto);

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = etDescripcion.getText().toString();

                if(text.isEmpty() || text == ""){
                    Toast.makeText(UserPhotoActivity.this,"Escribe una Descripcion",Toast.LENGTH_LONG).show();
                }else {
                    uploadPhoto();
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void uploadPhoto() {

        progressDialog.setMessage("Upload image...");
        progressDialog.show();

        StorageReference filepath = storageReference.child("Photo").child(myUri.getLastPathSegment());

        filepath.putFile(myUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                progressDialog.dismiss();

                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Card_Post item = new Card_Post();

                        String descripcion = etDescripcion.getText().toString();

                        item.setUid(UUID.randomUUID().toString());
                        item.setUser("Admin");
                        item.setImage(uri.toString());
                        item.setDescripcion(descripcion);
                        item.setLike(0);
                        item.setViews(0);
                        item.setPath(uri.getLastPathSegment());
                        createCardPost(item);

                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        System.out.println("Error : " + e.getMessage());
                    }
                });



                //System.out.println("Path : " + uri.toString());
                //Picasso.with(MainActivity.this).load(uri).fit().centerCrop().into(null);
                //Toast.makeText(MainActivity.this,"Upload Image",Toast.LENGTH_LONG).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                System.out.println("Error : " + e.getMessage());
            }
        });

    }

    private void initStorageFireBase() {

        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void createCardPost(Card_Post reg) {

        databaseReference.child("AllPostUsers").child(reg.getUid()).setValue(reg);

    }

}
