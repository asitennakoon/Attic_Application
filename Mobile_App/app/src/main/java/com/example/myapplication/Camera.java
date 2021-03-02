package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Camera extends AppCompatActivity {

    private static final String TAG = "camera";
    private Button btnCamera;
    private ImageView capturedImage;
    private Classifier.Device device = Classifier.Device.CPU;
    private int numThreads = -1;
    private Integer sensorOrientation;
    List<Classifier.Recognition> probabilityOutput;
    static String predictedClass;
//    public static String currentImagePath = null;
//    private static final int IMAGE_REQUEST = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_screen);

        Bitmap bitmap = BitmapFactory.decodeFile(Category.currentImagePath);
        try {
            probabilityOutput = Classifier.create(this,device,numThreads).recognizeImage(bitmap,0);
            Toast.makeText(this, probabilityOutput.toString(), Toast.LENGTH_SHORT).show();
            predictedClass = probabilityOutput.get(0).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        capturedImage = findViewById(R.id.image_view);
//        btnCamera = findViewById(R.id.btnCamera);

//        capture_image();
    }



    //navigate back to category screen
    public void gobackCategory(View view){
        Intent loadProductsIntent = new Intent(view.getContext(), MainActivity.class);
        startActivityForResult(loadProductsIntent,0);
    }

    //navigate foward to ar screen from screen btn
    public void navigateToAr(View view){
        Intent loadARIntent = new Intent(view.getContext(), Ar_item.class);
        startActivityForResult(loadARIntent,0);
    }



    //capture background image
//    public void capture_image(){
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(cameraIntent.resolveActivity(getPackageManager())!=null){
//            File imageFile = null;
//
//            try {
//                imageFile = getImageFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if( imageFile!=null ){
//                Uri imageUri = FileProvider.getUriForFile(this,"com.example.myapplication.fileprovider",imageFile);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//                startActivityForResult(cameraIntent,IMAGE_REQUEST);
////                startActivity(cameraIntent);
////                setContentView(R.layout.camera_screen);
//
//            }
//        }
//    }

    //display background image for the user
    public void display_image(View view){
        Intent loadImageIntent = new Intent(view.getContext(),Image_View.class);
        startActivityForResult(loadImageIntent,0);

//        Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath);
//        capturedImage.setImageBitmap(bitmap);
//        btnCamera.setText(currentImagePath);
    }



    //get the image file from the device storage
//    public File getImageFile() throws IOException{
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageName = "attic_"+timeStamp+"_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//
//        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
//        currentImagePath = imageFile.getAbsolutePath();
//        return imageFile;
//    }
}
