package com.example.floria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.floria.graphclient.MyApolloClient;
import com.example.floria.modelo.Card_Post;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class DetalleCardActivity extends AppCompatActivity {

    ImageView ivGraph;

    TextView tvNombre;
    TextView tvReino;
    TextView tvDivision;
    TextView tvClase;
    TextView tvSubclase;
    TextView tvOrden;
    TextView tvFamilia;
    TextView tvSubfamilia;
    TextView tvTribu;
    TextView tvGenero;
    TextView tvEspecie;
    TextView tvDescripcion;

    String nombre;
    String reino;
    String division;
    String clase;
    String subclase;
    String orden;
    String familia;
    String subfamilia;
    String tribu;
    String genero;
    String especie;
    String descripcion;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_card);

        ivGraph = findViewById(R.id.ivGraph);
        tvNombre = findViewById(R.id.tvNombre);
        tvReino = findViewById(R.id.tvReino);
        tvDivision = findViewById(R.id.tvDivision);
        tvClase = findViewById(R.id.tvClase);
        tvSubclase = findViewById(R.id.tvSubClase);
        tvOrden = findViewById(R.id.tvOrden);
        tvFamilia = findViewById(R.id.tvFamilia);
        tvSubfamilia = findViewById(R.id.tvSubFamilia);
        tvTribu = findViewById(R.id.tvTribu);
        tvGenero = findViewById(R.id.tvGenero);
        tvEspecie = findViewById(R.id.tvEspecie);
        tvDescripcion = findViewById(R.id.tvDescripcion);

        toolbar = findViewById(R.id.tbDetalle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Card_Post bundle = (Card_Post) getIntent().getSerializableExtra("Post");
        getPlantae(bundle);

    }

    private void getPlantae(final Card_Post item){
        MyApolloClient.getApolloClient().query(GetFindPlantaeQuery.builder().path(item.getPath()).user(item.getUser()).build()).enqueue(new ApolloCall.Callback<GetFindPlantaeQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetFindPlantaeQuery.Data> response) {

                nombre = response.data().findPlantae().nombre();
                reino = response.data().findPlantae().reino();
                division = response.data().findPlantae().division();
                clase = response.data().findPlantae().clase();
                subclase = response.data().findPlantae().subclase();
                orden = response.data().findPlantae().orden();
                familia = response.data().findPlantae().familia();
                subfamilia = response.data().findPlantae().subfamilia();
                tribu = response.data().findPlantae().tribu();
                genero = response.data().findPlantae().genero();
                especie = response.data().findPlantae().especie();
                descripcion = response.data().findPlantae().descripcion();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Picasso.with(DetalleCardActivity.this).load(item.getImage()).fit().centerCrop().into(ivGraph);

                        tvNombre.setText(nombre);
                        tvReino.setText(reino);
                        tvDivision.setText(division);
                        tvClase.setText(clase);
                        tvSubclase.setText(subclase);
                        tvOrden.setText(orden);
                        tvFamilia.setText(familia);
                        tvSubfamilia.setText(subfamilia);
                        tvTribu.setText(tribu);
                        tvGenero.setText(genero);
                        tvEspecie.setText(especie);
                        tvDescripcion.setText(descripcion);

                    }
                });

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                System.out.println("Error : " + e);
            }
        });
    }

}
