package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class Manual_AR_View extends AppCompatActivity {
    public static final String TITLE_KEY = "com.example.myapplication.Manual_AR_View.TITLE";
    public static final String LOG_TAG = Manual_AR_View.class.getSimpleName();

    // a request code for the second activity (popup)
    public static final int REQUEST_CODE = 10;

    //TODO: add Styles in the array
    private static String[] styles ={"Modern","","","","",""};
    private String style;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual__a_r__view);

        // getting the selected style from the intent
        Intent intent = getIntent();
        int styleIndex = Integer.parseInt(intent.getStringExtra(MainActivity.STYLE_KEY));
        style=styles[styleIndex];
    }

    public void showPopUp(View view) {
        // setting animations
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_anim);
        view.startAnimation(animation);

        // starting the popup for the result.
        Intent in = new Intent(getApplicationContext(), Manual_View.class);
        in.putExtra(TITLE_KEY, style);
        startActivityForResult(in, REQUEST_CODE);
        // transition --> drag from bottom
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            // checks whether the result request code matches the sent rrequest code
            if (resultCode == RESULT_OK){
                // if the result status is OK, load the model.
                String model_path = data.getStringExtra(Manual_View.PATH_KEY);
                Log.d(LOG_TAG,"path: "+model_path);
                Toast.makeText(this, "Building model", Toast.LENGTH_SHORT).show();

                //TODO: load model from path
            }
        }
    }

    public void exit(View view) {
        // setting animations for the exit button
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_anim);
        view.startAnimation(animation);

        finish();
    }
}