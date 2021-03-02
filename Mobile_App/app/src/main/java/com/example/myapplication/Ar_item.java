package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Ar_item extends AppCompatActivity {
    // When trained, the output of the deep learning model (as a string) would be assigned here
//    DrawerLayout drawerLayout;
//    NavigationView navigationView;
//    Toolbar toolbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ar_screen);

        FirebaseApp.initializeApp(this);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

//        drawerLayout = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.nav_view);
//        toolbar = findViewById(R.id.toolbarAR);
//
//        setSupportActionBar(toolbar);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        StorageReference previewRef1 = storage.getReference().child("out_img.jpg");

        try {
            File preview1 = File.createTempFile("out_img", "jpg");
            previewRef1.getFile(preview1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Ar_item.this, "Showing previews for " + Category.chosenCategory + " categories of " + Camera.predictedClass, Toast.LENGTH_SHORT).show();
                    Bitmap bitmap = BitmapFactory.decodeFile(preview1.getAbsolutePath());
                    ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        StorageReference previewRef2 = storage.getReference().child("wooden_chair_img.jpg");

        try {
            File preview2 = File.createTempFile("wooden_chair_img", "jpg");
            previewRef2.getFile(preview2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(Ar_item.this,"Showing previews for " + Category.chosenCategory + " category of " + predictedClass,Toast.LENGTH_SHORT).show();
                    Bitmap bitmap = BitmapFactory.decodeFile(preview2.getAbsolutePath());
                    ((ImageView) findViewById(R.id.imageView2)).setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        StorageReference modelRef = storage.getReference().child("out.glb");
        findViewById(R.id.imageView).setOnClickListener(v -> {
            try {
                File file1 = File.createTempFile("out", "glb");

                modelRef.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        buildModel(file1);

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        StorageReference modelRef2 = storage.getReference().child("wooden_chair.glb");
        findViewById(R.id.imageView2).setOnClickListener(v -> {
            try {
                File file2 = File.createTempFile("wooden_chair", "glb");

                modelRef2.getFile(file2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        buildModel(file2);

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
            andy.getScaleController().setMaxScale(1.0f);
            andy.getScaleController().setMinScale(0.2f);
            andy.setParent(anchorNode);
            andy.setRenderable(renderable);
            arFragment.getArSceneView().getScene().addChild(anchorNode);
        });

    }

    public void gobackCamera(View view) {
        Intent loadProductsIntent = new Intent(view.getContext(), MainActivity.class);
        startActivityForResult(loadProductsIntent, 0);
    }

    public void displayARitem01(View view) {

    }

    public void displayARitem02(View view) {

    }

    private ModelRenderable renderable;

    private void buildModel(File file) {

        RenderableSource renderableSource = RenderableSource
                .builder()
                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();

        ModelRenderable
                .builder()
                .setSource(this, renderableSource)
                .setRegistryId(file.getPath())
                .build()
                .thenAccept(modelRenderable -> {
                    Toast.makeText(this, "Model built", Toast.LENGTH_SHORT).show();
                    ;
                    renderable = modelRenderable;
                });
    }
}
