package com.example.myapplication;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.palette.graphics.Palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Image_View extends AppCompatActivity {
    private static final String LOG_TAG = Image_View.class.getSimpleName();

    private ImageView capturedImage;
    private Bitmap capturedImgBitMap;
    private ArrayList<String> coloursHex = new ArrayList<>();
    private ArrayList<Integer> coloursRGB = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_screen);
        displayCapturedImage();
    }

    public void displayCapturedImage(){
        capturedImage = findViewById(R.id.camera_feed);
//        ImageView capturedImage2 = findViewById(R.id.camera_feed2);
        Log.d(LOG_TAG, Category.currentImagePath);

//        capturedImgBitMap = BitmapFactory.decodeFile(Category.currentImagePath);

        capturedImgBitMap = BitmapFactory.decodeFile(Category.secondImagePath);
        capturedImage.setImageBitmap(capturedImgBitMap);

//        capturedImage2.setImageBitmap(bitmap2);

        Button classifierOutput = findViewById(R.id.classifierOutput);
        classifierOutput.setText(Camera.probabilityOutput.toString());

        generateColours();
    }



//    the method to generate colours
    public void generateColours(){
        /*
        The colour palette is generated,
        using the previous image bitmap and using Palette API in android.
        7 colour profiles can be acquired by using this.
        */


        if (capturedImgBitMap != null){
            Palette.from(capturedImgBitMap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Log.d(LOG_TAG, "generating swatches");

                    /*
                    For 7 colour profiles.
                    The profiles can be null sometimes depending on the availability.
                    Then adding the RGB integer colour of the profile to an ArrayList.
                    */
                    try {
                        coloursRGB.add(palette.getDominantSwatch().getRgb());
                    }catch (NullPointerException e){
                        Log.d(LOG_TAG, "No dominant swatch");
                    }

                    try {
                        coloursRGB.add(palette.getVibrantSwatch().getRgb());
                    }catch (NullPointerException e){
                        Log.d(LOG_TAG, "No vibrant swatch");
                    }

                    try {
                        coloursRGB.add(palette.getMutedSwatch().getRgb());
                    }catch (NullPointerException e){
                        Log.d(LOG_TAG, "No muted swatch");
                    }

                    try {
                        coloursRGB.add(palette.getDarkVibrantSwatch().getRgb());
                    }catch (NullPointerException e){
                        Log.d(LOG_TAG, "No dark vibrant swatch");
                    }

                    try {
                        coloursRGB.add(palette.getDarkMutedSwatch().getRgb());
                    }catch (NullPointerException e){
                        Log.d(LOG_TAG, "No dark muted swatch");
                    }

                    try {
                        coloursRGB.add(palette.getLightVibrantSwatch().getRgb());
                    }catch (NullPointerException e){
                        Log.d(LOG_TAG, "No light vibrant swatch");
                    }

                    try {
                        coloursRGB.add(palette.getLightMutedSwatch().getRgb());
                    }catch (NullPointerException e){
                        Log.d(LOG_TAG, "No light uted swatch");
                    }

                    Log.d(LOG_TAG, "swatches generated");

                    /*
                    * Once the palette is generated,
                    * A CardView is created to display the colours with rounded corners
                    * And a TextView is created to display the name of the colour.
                    * Both are then added to a vertical linear layout
                    * Which then get added to a Horizontal Scroll view
                    * */

                    LinearLayout layout = findViewById(R.id.img_view_colour_layout);

                    for (Integer colorRGB : coloursRGB){
//                        Converting the RGB integer to Hex Colour
//                        Reference: https://stackoverflow.com/questions/6539879/how-to-convert-a-color-integer-to-a-hex-string-in-android
                        String hexColor = String.format("#%06X", (0xFFFFFF & colorRGB));
                        Log.d(LOG_TAG, hexColor);

//                        Linear layout to contain cardview and textview

                        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(200,300);
                        lParams.setMargins(10,10,10,10);

                        LinearLayout l = new LinearLayout(getApplicationContext());
                        l.setLayoutParams(lParams);
                        l.setGravity(Gravity.CENTER);
                        l.setOrientation(LinearLayout.VERTICAL);

//                      Cardview to displat the colour
                        CardView.LayoutParams cParams = new CardView.LayoutParams(150,150);
                        cParams.setMargins(5,5,5,5);

                        CardView c = new CardView(getApplicationContext());
                        c.setLayoutParams(cParams);
                        c.setCardElevation(6);
                        c.setRadius(40);

//                        Setting the background tint of the card view instead of background
//                        Otherwise border radius will be hidden.
//                        Reference: https://stackoverflow.com/questions/29801031/how-to-add-button-tint-programmatically

                        c.setBackgroundTintList(ColorStateList.valueOf(colorRGB));
                        c.setPadding(10,10,10,10);

//                      TextView for the colour name
                        LinearLayout.LayoutParams tParams = new LinearLayout.LayoutParams(190,100);
                        tParams.setMargins(5,5,5,8);

                        TextView colourName = new TextView(getApplicationContext());
                        colourName.setText(hexColor);
                        colourName.setLayoutParams(tParams);
                        colourName.setTextColor(Color.parseColor("#80640c"));
                        colourName.setTextSize(13);

                        colourName.setGravity(Gravity.CENTER);

//                        Adding the card view and text view to the linear layout
                        l.addView(c);
                        l.addView(colourName);
//                        Then adding the linear layout to the horizontal scroll view
                        layout.addView(l);


                    }
                }
            });
        }



    }






}