package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Image_View extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_screen);
        displayCapturedImage();
    }

    public void displayCapturedImage(){
        ImageView capturedImage = findViewById(R.id.camera_feed);
        //
//        ImageView capturedImage2 = findViewById(R.id.camera_feed2);

        Bitmap bitmap = BitmapFactory.decodeFile(Category.currentImagePath);
        //
//        Bitmap bitmap2 = BitmapFactory.decodeFile(Category.secondImagePath);
        capturedImage.setImageBitmap(bitmap);
        //
//        capturedImage2.setImageBitmap(bitmap2);

        Button classifierOutput = findViewById(R.id.classifierOutput);
        classifierOutput.setText(Camera.probabilityOutput.toString());
    }


}