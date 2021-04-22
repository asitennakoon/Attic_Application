package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class AR_Item extends AppCompatActivity {
    private boolean state;
    private static final String TAG = "AR_Item";
    private ModelRenderable renderable;
    ArFragment arFragment;
    ImageView thumbnail;
    Dialog productInfo;
    Button closeBtn;
    Button viewBtn;
    ImageView preview;
    TextView manufacturer;
    TextView material;
    TextView color;
    TextView price;
    TextView stock;
    ProgressBar progressbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ar_screen);
        productInfo = new Dialog(this);
        Toast.makeText(AR_Item.this, "Showing previews for " + Camera.chosenCategory + " category of " + ViewOptions.recognizedStyle[2] + " " + ViewOptions.recognizedRoom[1], Toast.LENGTH_LONG).show();


        //loading progress bar
        progressbar = this.findViewById(R.id.progressBar);
        progressbar.setScaleY(4f);
        progressbar.setVisibility(View.INVISIBLE);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
            andy.getScaleController().setMaxScale(1.0f);
            andy.getScaleController().setMinScale(0.2f);
            andy.setParent(anchorNode);
            andy.setRenderable(renderable);
            arFragment.getArSceneView().getScene().addChild(anchorNode);
        });

        // Created an instance of FirebaseStorage to access the Cloud Storage bucket of Attic
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Created a storage reference to the root folder of the bucket to gain access to the folders inside
        StorageReference storageRef = storage.getReference();
        // Created child references, that point to respective folders for the recognized class, inside images and objects folders
        StorageReference imagesRef = storageRef.child("images/" + ViewOptions.recognizedRoom[1] + "/" + ViewOptions.recognizedStyle[2]);
        StorageReference objectsRef = storageRef.child("objects/" + ViewOptions.recognizedRoom[1] + "/" + ViewOptions.recognizedStyle[2]);

        // Created an instance of FirebaseFirestore to access the Cloud Firestore of Attic
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Created a reference to the furniture-info collection
        CollectionReference collectionRef = db.collection("furniture-info");

        // Listed all images in the respective folder in the storage
        imagesRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(ListResult listResult) {
                // Set all image views in the layout to an array, to be loaded with preview images of products
                int[] imageViews = {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView5, R.id.imageView6};
                int index = 0;
                boolean available = false;

                // Listed images are iterated, filtering the thumbnail images relevant to the selected furniture category
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().contains(Camera.chosenCategory)) {
                        thumbnail = findViewById(imageViews[index]);
                        // Downloaded thumbnail image directly from StorageReference using Glide
                        Glide.with(AR_Item.this).load(item).into(thumbnail);

                        findViewById(imageViews[index]).setOnClickListener(v -> {
                            // Displayed UI elements on the pop-up window
                            displayElements();

                            // Created a substring to set the path to load the preview image for the product
                            String itemName = item.getName().substring(0, item.getName().indexOf("."));
                            Glide.with(AR_Item.this).load(imagesRef.child("previews/" + itemName + ".gif")).into(preview);

                            // Retrieved the document in the cloud database, relevant to the product of recognized room and style
                            collectionRef.document(ViewOptions.recognizedRoom[1] + "-" + ViewOptions.recognizedStyle[2] + "-" + itemName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            // Loaded the data in necessary fields in the document
                                            loadData(document);
                                        } else {
                                            Toast.makeText(AR_Item.this, "Info for this product does not exist in the database", Toast.LENGTH_LONG).show();
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Toast.makeText(AR_Item.this, "Error! Check your network connection", Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });

                            closeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    productInfo.dismiss();
                                }
                            });

                            productInfo.show();

                            // Retrieved the 3D object relevant to the product, stored in the cloud storage
                            StorageReference object = objectsRef.child(itemName + ".glb");
                            viewBtn.setOnClickListener(v1 -> {
                                try {
                                    File file = File.createTempFile(itemName, "glb");
                                    object.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            // Rendered the 3D model and loaded it on to the AR fragment
                                            buildModel(file);
                                        }
                                    });
                                } catch (IOException e) {
                                    Toast.makeText(AR_Item.this, "Build failed! Check your network connection", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, e.toString());
                                } finally {
                                    productInfo.dismiss();
//                                    while (true){
//                                        if(!state){
//                                            progressbar.setVisibility(View.GONE);
//                                            break;
//                                        }
//                                    }
                                }
                            });

                        });
                        index++;
                        available = true;
                    }
                }
                if (!available) {
                    Toast.makeText(AR_Item.this, "Sorry! Currently, there are no products available for " + ViewOptions.recognizedStyle[2] + " " + ViewOptions.recognizedRoom[1] + " scene", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AR_Item.this, "Error occurred when accessing the cloud storage! Check your network connection", Toast.LENGTH_LONG).show();
                Log.d(TAG, e.toString());
            }
        });

    }

    private void loadData(DocumentSnapshot document) {
        manufacturer.setText(document.getString("manufacturer"));
        material.setText(document.getString("material"));
        color.setText(document.getString("color"));
        price.setText(document.getString("price"));
        stock.setText("Only " + document.getString("stock") + " left in stock");
    }

    private void displayElements() {
        productInfo.setContentView(R.layout.furniture_info_screen);
        closeBtn = (Button) productInfo.findViewById(R.id.closeBtn);
        viewBtn = (Button) productInfo.findViewById(R.id.viewBtn);
        preview = (ImageView) productInfo.findViewById(R.id.previewImg);
        manufacturer = (TextView) productInfo.findViewById(R.id.manufacturer);
        material = (TextView) productInfo.findViewById(R.id.material);
        color = (TextView) productInfo.findViewById(R.id.color);
        price = (TextView) productInfo.findViewById(R.id.price);
        stock = (TextView) productInfo.findViewById(R.id.stock);

        progressbar.setVisibility(View.VISIBLE);
    }

    public void gobackCamera(View view) {
        Intent loadProductsIntent = new Intent(view.getContext(), MainActivity.class);
        startActivityForResult(loadProductsIntent, 0);
    }

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
                    renderable = modelRenderable;
                });
        progressbar.setVisibility(View.INVISIBLE);
    }

}
