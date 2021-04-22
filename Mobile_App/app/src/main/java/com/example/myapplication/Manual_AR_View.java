package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Manual_AR_View extends AppCompatActivity {
    public static final String TITLE_KEY = "com.example.myapplication.Manual_AR_View.TITLE";
    public static final String LOG_TAG = Manual_AR_View.class.getSimpleName();
    // a request code for the second activity (popup)
    public static final int REQUEST_CODE = 10;
    private static String[] roomTypes = {"Livingroom", "Diningroom", "Bedroom"};
    private String roomType;
    StorageReference objectsRef;
    private ModelRenderable renderable;
    ArFragment arFragment;
    ProgressBar progressBarManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual__a_r__view);

        // initialized an instance of ArFragment in the Sceneform library for the AR functionality
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
            andy.getScaleController().setMaxScale(1.0f);
            andy.getScaleController().setMinScale(0.2f);
            andy.setParent(anchorNode);
            andy.setRenderable(renderable);
            arFragment.getArSceneView().getScene().addChild(anchorNode);
        });

        // getting the selected style from the intent
        Intent intent = getIntent();
        int styleIndex = Integer.parseInt(intent.getStringExtra(MainActivity.ROOM_KEY));
        roomType = roomTypes[styleIndex];

        // created a reference to the folder of the selected room type in the cloud storage
        objectsRef = FirebaseStorage.getInstance().getReference().child("objects/" + roomType);


        //set up progress bar
        progressBarManual=this.findViewById(R.id.progressBarManual);
        progressBarManual.setScaleY(3f);
        progressBarManual.setVisibility(View.INVISIBLE);
    }

    public void showPopUp(View view) {
        // setting animations
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_anim);
        view.startAnimation(animation);

        // starting the popup for the result.
        Intent in = new Intent(getApplicationContext(), ManualView.class);
        in.putExtra(TITLE_KEY, roomType);
        startActivityForResult(in, REQUEST_CODE);
        // transition --> drag from bottom
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            // checks whether the result request code matches the sent request code
            if (resultCode == RESULT_OK) {
                // if the result status is OK, load the model.
                String model_path = data.getStringExtra(ManualView.PATH_KEY);
                Log.d(LOG_TAG, "path: " + model_path);
                Toast.makeText(this, "Building model", Toast.LENGTH_SHORT).show();

                //showing progress bar
                progressBarManual.setVisibility(View.VISIBLE);

                // this variable is assigned with the path to the 3D object in the storage
                StorageReference object = objectsRef.child(model_path);
                try {
                    File file = File.createTempFile((model_path.substring(0, model_path.indexOf("/"))) + (model_path.substring(model_path.indexOf("/") + 1, model_path.indexOf("."))), "glb");
                    object.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            buildModel(file);
                        }
                    });
                } catch (IOException e) {
                    Toast.makeText(this, "Build failed! Check your network connection", Toast.LENGTH_LONG).show();
                    Log.d(LOG_TAG, e.toString());
                }
            }
        }
    }

    // built-in methods in the Sceneform library are used to build and render 3D objects in an AR environment
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

        //removing progress bar
        progressBarManual.setVisibility(View.INVISIBLE);
    }

    public void exit(View view) {
        // setting animations for the exit button
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_anim);
        view.startAnimation(animation);

        finish();
    }
}