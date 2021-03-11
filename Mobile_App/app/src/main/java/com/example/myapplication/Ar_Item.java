package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Ar_Item extends AppCompatActivity {
//    DrawerLayout drawerLayout;
//    NavigationView navigationView;
//    Toolbar toolbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ar_screen);
        Toast.makeText(Ar_Item.this, "Showing previews for " + Category.chosenCategory + " categories of " + Camera.predictedClass[1], Toast.LENGTH_SHORT).show();

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

        // Created an instance of FirebaseStorage to access the Cloud Storage bucket of Attic
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Created a storage reference to the root folder of the bucket to gain access to the folders inside
        StorageReference storageRef = storage.getReference();
        // Created child references, that point to respective folders for the predicted class, inside images and objects folders
        StorageReference imagesRef = storageRef.child("images/" + Camera.predictedClass[1]);
        StorageReference objectsRef = storageRef.child("objects/" + Camera.predictedClass[1]);

        imagesRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        /*for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }*/

                        int[] imageViews = {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView5, R.id.imageView6};
                        int index = 0;

                        for (StorageReference item : listResult.getItems()) {
                            if (item.getName().contains(Category.chosenCategory)) {
                                ImageView imageView = findViewById(imageViews[index]);
                                // Preview image downloaded directly from StorageReference using Glide
                                Glide.with(Ar_Item.this).load(item).into(imageView);

                                String objectName = item.getName().substring(0, item.getName().indexOf("."));
                                StorageReference object = objectsRef.child(objectName + ".glb");
                                findViewById(imageViews[index]).setOnClickListener(v -> {
                                    try {
                                        File file = File.createTempFile(objectName, "glb");
                                        object.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                buildModel(file);
                                            }
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                                index++;
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
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
