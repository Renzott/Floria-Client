package com.example.floria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.floria.adapters.ContactoAdapter;

import com.example.floria.modelo.Card_Post;

import com.example.floria.modelo.Comments;
import com.example.floria.modelo.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;


import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ListView lvPostFriends;
    ArrayList<Card_Post> lstCardPost = new ArrayList<Card_Post>();

    ContactoAdapter adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String nombre;
    String apellido;

    String correo;


    TextView navNombre;
    TextView navMail;

    public static final String SHARED_PREFS = "Preferencias";
    public static final String NAME = "keyAppUser";

    static final int REQUEST_TAKE_PHOTO = 1;

    private static final int MY_REQUEST_CODE = 0xe110;

    String mCurrentPhotoPath;

    String keyApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigator_drawer);

        // Instanciar elementos en la vista
        toolbar = findViewById(R.id.tbMenu);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        lvPostFriends = findViewById(R.id.lvPostFriends);

        // Intanciar elementos dentro del navigationView
        navNombre = navigationView.getHeaderView(0).findViewById(R.id.navNombre);
        navMail = navigationView.getHeaderView(0).findViewById(R.id.navMail);

        // Inicializar Firebase
        initFirebase();
        listarDatos();

        /*Sin internet*/

        /*Card_Post reg = new Card_Post();

        reg.setUid("asdasd");
        reg.setViews(0);
        reg.setPath("asdad");
        reg.setDescripcion("Oh no");
        reg.setImage("xczc");
        reg.setUser("Pepito");
        reg.setLike(0);
        reg.setComments(0);

        lstCardPost.add(reg);

        adapter = new ContactoAdapter(MainActivity.this,R.layout.item_card_post,lstCardPost);
        lvPostFriends.setAdapter(adapter);

        /**/

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);

        drawerLayout.setDrawerListener(toggle);

        toggle.syncState();

        loadUserData();

        lvPostFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Card_Post item = (Card_Post) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, DetalleCardActivity.class);

                intent.putExtra("Post", item);

                startActivity(intent);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        //Comments item = new Comments();

        //item.setUid("d62c17d3-0234-4664-954e-aa8deec6aa16");
        //item.setNick("Ricitos de Oro");

        //databaseReference.child("AllPostUsers").child("1f0431e4-15c7-4d2d-a8d1-6a4b1e29ee35").child("comments").child(item.getUid()).setValue(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println(mCurrentPhotoPath);
        System.out.println("Request : " + requestCode );

        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {

                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(this.getContentResolver(), Uri.fromFile(file));

                        Uri uri  = getImageUri(getApplicationContext(),bitmap);

                        Intent intent = new Intent(this,UserPhotoActivity.class);
                        intent.putExtra("imageUri", uri.toString());
                        startActivity(intent);

                    }
                    break;
                }
                case MY_REQUEST_CODE:{

                    loadUserData();
                }
            }

        } catch (Exception error) {
            error.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_app,menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void initFirebase() {

        FirebaseApp.initializeApp(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    private void listarDatos() {
        databaseReference.child("AllPostUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstCardPost.clear();

                for(DataSnapshot item : dataSnapshot.getChildren()){

                    ArrayList<Comments> lstComments = new ArrayList<>();
                    Iterator<DataSnapshot> comments =  item.child("comments").getChildren().iterator();

                    int limit = 0;

                    while (comments.hasNext() & limit < 3){

                        Comments reg = comments.next().getValue(Comments.class);
                        lstComments.add(reg);

                        limit++;

                    }

                    Card_Post reg = item.getValue(Card_Post.class);

                    int countComments = (int) item.child("comments").getChildrenCount();

                    reg.setListaComments(lstComments);
                    reg.setCountComments(countComments);
                    lstCardPost.add(reg);

                }

                adapter = new ContactoAdapter(MainActivity.this,R.layout.item_card_post,lstCardPost);
                lvPostFriends.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadUserData(){

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        keyApp = preferences.getString(NAME,"default");

        if(keyApp == "default"){
            Intent intent = new Intent(this,NewUserActivity.class);
            startActivityForResult(intent,MY_REQUEST_CODE);

        }else{
            databaseReference.child("Persona").child(keyApp).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    User reg = dataSnapshot.getValue(User.class);

                    nombre = reg.getNombre();
                    apellido = reg.getApellido();
                    correo = reg.getCorreo();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            navNombre.setText(nombre + " " + apellido );
                            navMail.setText(correo);
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

                System.out.println("Error: " + ex.getMessage());
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.floria.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

}
