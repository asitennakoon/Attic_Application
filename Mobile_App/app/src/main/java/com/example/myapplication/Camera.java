package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends AppCompatActivity  {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int IMAGE1_REQUEST_CODE = 102;
    public static final int IMAGE2_REQUEST_CODE = 103;
    public static String currentImagePath = null;
    public static String secondImagePath = null;
    private static final int IMAGE_REQUEST = 1;
    static String chosenCategory;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_screen);
    }

    public void loadProducts(View view){
        askCameraPermission();
        chosenCategory = String.valueOf(view.getContentDescription());
        Toast.makeText(this,chosenCategory + " category selected", Toast.LENGTH_SHORT).show();
    }


//    get the image file from the device storage
    public File getImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "attic_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }


//    get second image file
    public File getSecondImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String image2Name = "attic_2_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(image2Name,".jpg",storageDir);
        secondImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }



    private void askCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else {
                Toast.makeText(this,"Application need camera permission",Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void openCamera(){
        //Toast.makeText(this, "ViewOptions open request", Toast.LENGTH_SHORT).show();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager())!=null){
            File imageFile = null;
            try {
                imageFile = getImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Toast.makeText(this,"File created",Toast.LENGTH_SHORT).show();
            if( imageFile!=null ){
                Uri imageUri = FileProvider.getUriForFile(this,"com.example.myapplication.fileprovider",imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(cameraIntent, IMAGE1_REQUEST_CODE);
            }
        }
    }


    public void openSecondCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager())!=null){
            File imageFile = null;
            try {
                imageFile = getSecondImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Toast.makeText(this,"File created",Toast.LENGTH_SHORT).show();
            if( imageFile!=null ){
                Uri imageUri = FileProvider.getUriForFile(this,"com.example.myapplication.fileprovider",imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(cameraIntent, IMAGE2_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        * Whenever camera is open, it creates an activity which returns a result.
        * The result is processed here.
        * First, open the camera to capture the first image.
        * Then if the capture is successful, it opens the camera again to capture the second image.
        * If it's also successful, then open the activity for prediction and view options.
        * This method improves performance since activities are loaded one after the other and,
        *   it also prevents any errors occurring when there are no images available.
        * */

//        compare the request codes
        if (requestCode == IMAGE1_REQUEST_CODE){
            if (resultCode == RESULT_OK){
//                open camera to capture the second image.
                openSecondCamera();
            }
            else if (resultCode == RESULT_CANCELED){
//                If user press back button, the image won't be captured.
//                Therefore aborting the process and show categories again.
                Toast.makeText(getApplicationContext(),"Automated process cancelled by user",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Camera.class);
//                A flag to clear the activity stack to improve performance.
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else{
                openCamera();
            }
        }

//        compare the request codes
        if (requestCode == IMAGE2_REQUEST_CODE){
            if (resultCode == RESULT_OK){
//                since both images are captured, proceed to prediction and view options.
                Intent viewOptionsIntent = new Intent(getApplicationContext(), ViewOptions.class);
                startActivity(viewOptionsIntent);
            }
            else if (resultCode == RESULT_CANCELED){
//                If user press back button, the image won't be captured.
//                Therefore aborting the process and show categories again.
                Toast.makeText(getApplicationContext(),"Automated process cancelled by user",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Camera.class);
//                A flag to clear the activity stack to improve performance.
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else{
                openSecondCamera();
            }

        }
    }

}
