package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

public class ViewOptions extends AppCompatActivity {

    private static final String TAG = "camera";
    private Button predictedCategoryBtn;
    private RoomClassifier.Device device1 = RoomClassifier.Device.CPU;
    private StyleClassifier.Device device2 = StyleClassifier.Device.CPU;
    private int numThreads = -1;
    static List<RoomClassifier.Recognition> roomClassifierProbabilityOutput;
    static List<StyleClassifier.Recognition> styleClassifierProbabilityOutput;
    static String[] recognizedRoom;
    static String[] recognizedStyle;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_screen);

        try {
            // initialized a bitmap with the image captured first
            Bitmap bitmap1 = BitmapFactory.decodeFile(Camera.currentImagePath);
            // ran inference on the bitmap and returned the classification results using the corresponding image classification model,
            // used to recognize the room type
            roomClassifierProbabilityOutput = RoomClassifier.create(this, device1, numThreads).recognizeImage(bitmap1, 0);
            Log.d(TAG, roomClassifierProbabilityOutput.toString());
            // returned the label with the top probability
            String topKProbability1 = roomClassifierProbabilityOutput.get(0).toString();
            recognizedRoom = topKProbability1.split(" ");

            // Same process is done on the image captured second, this time using the other image classification model, used to
            // recognize the furniture style
            Bitmap bitmap2 = BitmapFactory.decodeFile(Camera.secondImagePath);
            styleClassifierProbabilityOutput = StyleClassifier.create(this, device2, numThreads).recognizeImage(bitmap2, 0);
            Log.d(TAG, styleClassifierProbabilityOutput.toString());
            String topKProbability2 = styleClassifierProbabilityOutput.get(0).toString();
            recognizedStyle = topKProbability2.split(" ");

            predictedCategoryBtn = (Button) findViewById(R.id.categoryPrediction);
            predictedCategoryBtn.setText("Attic's Classification - " + String.valueOf(recognizedStyle[2]) + " " + String.valueOf(recognizedStyle[4]) + " | " + String.valueOf(recognizedRoom[1]) + " " + String.valueOf(recognizedRoom[2]));

        } catch (IOException e) {
            Toast.makeText(this, "Error occurred while running inference! Try capturing the images again", Toast.LENGTH_LONG).show();
            Log.d(TAG, e.toString());
        } catch (Exception e) {
//            Prevent app crash for uncaught errors.
            e.printStackTrace();
            finish();
        }
    }


    //navigate back to category screen
    public void gobackCategory(View view) {
        Intent loadProductsIntent = new Intent(view.getContext(), MainActivity.class);
        startActivityForResult(loadProductsIntent, 0);
    }

    //navigate foward to ar screen from screen btn
    public void navigateToAr(View view) {
        Intent loadARIntent = new Intent(view.getContext(), AR_Item.class);
        startActivityForResult(loadARIntent, 0);
    }


    //display background image for the user
    public void display_image(View view) {
        Intent loadImageIntent = new Intent(view.getContext(), ImageView.class);
        startActivityForResult(loadImageIntent, 0);
    }

}
