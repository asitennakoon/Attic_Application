package com.example.myapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

//    DrawerLayout drawerLayout;
//    NavigationView navigationView;
//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        drawerLayout = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.nav_view);
//        toolbar = findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
    }



    public void loadCategory(View view){
        Intent loadProductsIntent = new Intent(view.getContext(), Category.class);
        startActivityForResult(loadProductsIntent,0);
    }

//        FirebaseApp.initializeApp(this);
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//
//        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
//
//        RadioGroup rg = findViewById(R.id.radioGroup1);
//        System.out.println("Assas");
//        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                System.out.println("bbbb");
//                switch (checkedId) {
//                    case R.id.radio1:
//                        StorageReference modelRef = storage.getReference().child("out.glb");
//                        try {
//                            File file1 = File.createTempFile("out", "glb");
//
//                            modelRef.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                                    buildModel(file1);
//
//                                }
//                            });
//
//                        } catch (IOException e ) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    case R.id.radio2:
//                        StorageReference modelRef2 = storage.getReference().child("out (1).glb");
//                        try {
//                            File file1 = File.createTempFile("out(1)", "glb");
//
//                            modelRef2.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                                    buildModel(file1);
//                                }
//                            });
//
//                        } catch (IOException e ) {
//                            e.printStackTrace();
//                        }
//                        break;
//                }
//            }
//        });

//        findViewById(R.id.downloadBtn).setOnClickListener(v -> {
//            try {
//                File file1 = File.createTempFile("chair", "glb");
//
//                modelRef.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        buildModel(file1);
//
//                    }
//                });
//
//            } catch (IOException e ) {
//                e.printStackTrace();
//            }
//        });

//        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
//            AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
//            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
//            andy.setParent(anchorNode);
//            andy.setRenderable(renderable);
//            arFragment.getArSceneView().getScene().addChild(anchorNode);
//        });
//    }
//
//    private ModelRenderable renderable;
//
//    private void buildModel(File file) {
//
//        RenderableSource renderableSource = RenderableSource
//                .builder()
//                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
//                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
//                .build();
//
//        ModelRenderable
//                .builder()
//                .setSource(this, renderableSource)
//                .setRegistryId(file.getPath())
//                .build()
//                .thenAccept(modelRenderable -> {
//                    Toast.makeText(this, "Model built", Toast.LENGTH_SHORT).show();;
//                    renderable = modelRenderable;
//                });
//    }
}
