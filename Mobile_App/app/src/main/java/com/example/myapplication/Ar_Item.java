package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Ar_Item extends AppCompatActivity {
    Dialog productInfo;
    TextView closeTxt;
    Button viewBtn;
    ImageView preview;
    TextView manufacturer;
    TextView material;
    TextView color;
    TextView price;
    TextView stock;
//    DrawerLayout drawerLayout;
//    NavigationView navigationView;
//    Toolbar toolbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ar_screen);
        productInfo = new Dialog(this);
        Toast.makeText(Ar_Item.this, "Showing previews for " + Category.chosenCategory + " category of " + Camera.predictedClass[1], Toast.LENGTH_LONG).show();

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

        // Created an instance of FirebaseFirestore to access the Cloud Firestore of Attic
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Created a reference to the furniture-info collection
        CollectionReference collectionRef = db.collection("furniture-info");

        imagesRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(ListResult listResult) {
                        int[] imageViews = {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView5, R.id.imageView6};
                        int index = 0;

                        for (StorageReference item : listResult.getItems()) {
                            if (item.getName().contains(Category.chosenCategory)) {
                                ImageView imageView = findViewById(imageViews[index]);
                                // Preview image downloaded directly from StorageReference using Glide
                                Glide.with(Ar_Item.this).load(item).into(imageView);

                                findViewById(imageViews[index]).setOnClickListener(v -> {
                                    productInfo.setContentView(R.layout.furniture_info_screen);
                                    closeTxt = (TextView) productInfo.findViewById(R.id.closeTxt);
                                    viewBtn = (Button) productInfo.findViewById(R.id.viewBtn);
                                    preview = (ImageView) productInfo.findViewById(R.id.imageView);
                                    manufacturer = (TextView) productInfo.findViewById(R.id.manufacturer);
                                    material = (TextView) productInfo.findViewById(R.id.material);
                                    color = (TextView) productInfo.findViewById(R.id.color);
                                    price = (TextView) productInfo.findViewById(R.id.price);
                                    stock = (TextView) productInfo.findViewById(R.id.stock);

                                    String itemName = item.getName().substring(0, item.getName().indexOf("."));
                                    Glide.with(Ar_Item.this).load(imagesRef.child("previews/" + itemName + ".gif")).into(preview);

                                    collectionRef.document(Camera.predictedClass[1] + "-" + itemName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    manufacturer.setText(document.getString("manufacturer"));
                                                    material.setText(document.getString("material"));
                                                    color.setText(document.getString("color"));
                                                    price.setText(document.getString("price"));
                                                    stock.setText("Only " + document.get("stock").toString() + " left in stock");
                                                }
                                            }
                                        }
                                    });

                                    closeTxt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            productInfo.dismiss();
                                        }
                                    });
                                    productInfo.show();

                                    StorageReference object = objectsRef.child(itemName + ".glb");
                                    viewBtn.setOnClickListener(v1 -> {
                                        try {
                                            File file = File.createTempFile(itemName, "glb");
                                            object.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    buildModel(file);
                                                }
                                            });
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } finally {
                                            productInfo.dismiss();
                                        }
                                    });

                                });

                                index++;
                            }
                        }
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
                    Toast.makeText(this, "3D model built", Toast.LENGTH_SHORT).show();
                    ;
                    renderable = modelRenderable;
                });
    }
}
