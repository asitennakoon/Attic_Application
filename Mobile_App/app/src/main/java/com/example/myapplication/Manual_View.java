package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;


public class Manual_View extends AppCompatActivity {

    private static final String LOG_TAG = Manual_View.class.getSimpleName();
    // a key for the path variable
    public static final String PATH_KEY = "com.example.myapplication.Manual_View.PATH_KEY";

    private LinearLayout layout;
    ArrayList<Furniture> furniture = new ArrayList<>();

    private String roomType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_view);

        // getting the window size using display metrics
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // height and width percentages for the popup activity
        double width = dm.widthPixels*0.8;
        double height = dm.heightPixels*0.8;

        // setting the window size to the new size
        getWindow().setLayout((int) width, (int) height);

        // hide the status bar and bottom navigation for full screen
        hideSystemUI();

        // initialising variables
        TextView title = findViewById(R.id.activity_manual_title);
        Intent intent = getIntent();
        roomType = intent.getStringExtra(Manual_AR_View.TITLE_KEY);
        title.setText(roomType);

        layout = findViewById(R.id.activity_manual_linear_layout);

        //TODO: Remove these hardcoded data after firebase load data is implemented.

        furniture.add(new Furniture("Living Room Bedroom Chair 2","Black", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        , "Damro", "leather", "price", "10"));

        furniture.add(new Furniture("Bedroom Table 2","Black", "AAAAAAAAAAAAAAAAAAAAAAAAAAAjskgnlsgn"
                , "Damro", "leather", "price", "10"));

        furniture.add(new Furniture("Bedroom Sofa 2","Black", "AAAAAAAAAAAAAAAAAAAAAAAAAAAjskgnlsgn"
                , "Damro", "leather", "price", "10"));

        // load data from firebase
        loadFirebaseData();


    }

    public void addProducts(){
        for (Furniture f: furniture){
            setLayout(f);
        }
    }

    public void setLayout(Furniture f){

        // ---------------------------- Card View ---------------------------------//
        // layout parameters and margins
        CardView.LayoutParams cParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.MATCH_PARENT);
        cParams.setMargins(30,40,30,40);

        CardView c = new CardView(this);
        c.setLayoutParams(cParams);
        c.setCardElevation(8);
        c.setRadius(40);
        Log.d(LOG_TAG,"CardView "+f.getName()+"initialised.");

        // ----------------- Linear Layout to contain CardView content ------------------------//
        // layout parameters and margins

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lParams.setMargins(10,10,10,10);

        LinearLayout l = new LinearLayout(this);
        l.setLayoutParams(lParams);
        l.setGravity(Gravity.CENTER);
        l.setOrientation(LinearLayout.VERTICAL);
        Log.d(LOG_TAG,"CardView LinearLayout initialised.");

        // ---------------------------- Product heading ---------------------------------//
        // layout parameters and margins

        LinearLayout.LayoutParams tParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tParams.setMargins(5,8,5,8);

        TextView heading = new TextView(this);
        heading.setText(f.getName());
        heading.setLayoutParams(tParams);
        heading.setTextColor(Color.parseColor("#80640c"));
        heading.setTextSize(20);
        heading.setTranslationX(10);
        heading.setTypeface(null, Typeface.BOLD);
        heading.setGravity(Gravity.CENTER);
        Log.d(LOG_TAG,"CardView LinearLayout Heading initialised.");

        // ---------------------------- Product image ----------------------------------//
        ImageView image = new ImageView(this);
        //TODO: get relevant image from firebase storage using furniture name ( f.getName() )
        image.setImageResource(R.drawable.sample_bed);
//

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(450,450);
        image.setLayoutParams(p);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: generate model path and replace "path" below

                Intent intent = new Intent();
                intent.putExtra(PATH_KEY, "path");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        Log.d(LOG_TAG,"CardView LinearLayout image initialised.");

        //----------------------------- product data ----------------------------------//

        // Table
        // layout parameters and margins
        TableLayout.LayoutParams tableParams =new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tableParams.setMargins(10,20,10,20);

        TableLayout table = new TableLayout(this);
        table.setLayoutParams(tableParams);
        table.setStretchAllColumns(true);
        table.setColumnShrinkable(2,true);
        Log.d(LOG_TAG,"CardView LinearLayout Table initialised.");

        /*
          * Table rows
          * Table rows are in the same format.
          * Therefore, using another method to avoid duplication.
         */
        table.addView(getRowView("Colour", f.getColour()));
        table.addView(getRowView("Material", f.getMaterial()));
        table.addView(getRowView("Manufacturer", f.getManufacturer()));
        table.addView(getRowView("Description", f.getDescription()));
        table.addView(getRowView("Price", f.getPrice()));
        table.addView(getRowView("Stock", f.getStock()));

        Log.d(LOG_TAG,"CardView LinearLayout Table Rows initialised.");

        /*
        * In the linear layout, items are in following order
        * heading - containing the item name
        * image - an image of the item
        * item data in a table
        */
        l.addView(heading);
        l.addView(image);
        l.addView(table);
        Log.d(LOG_TAG,"CardView LinearLayout Items Added.");


        // finally, adding the linear layout to CardView
        c.addView(l);
        Log.d(LOG_TAG,"CardView Items Added.");

        // Then add CardView to the linear layout inside the ScrollView to display.
        layout.addView(c);
        Log.d(LOG_TAG,"CardView added to LinearLayout");
        Log.d(LOG_TAG,"");


    }


    public TableRow getRowView(String Key, String value){

        /*
        * A Table Row consists of 3 columns
        * Key - description of value
        * : - to separate the key from its' value
        * value - the value of the key.
        * */

        // ----------------------------- Initialising Row --------------------------------------//
        TableRow.LayoutParams tRowParams =new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);


        TableRow row = new TableRow(this);
        row.setLayoutParams(tRowParams);
        Log.d(LOG_TAG,"Table Row initialised.");


        // ----------------------------- First Column - Key --------------------------------------//
        // layout parameters for TextView
        TableRow.LayoutParams tParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        tParams.setMargins(10,8,0,8);
        tParams.setMarginStart(10);

        // TextView and styles
        TextView txt1 = new TextView(this);
        txt1.setText(Key);
        txt1.setTextSize(17);
        txt1.setTextColor(Color.parseColor("#80640c"));
        txt1.setLayoutParams(tParams);
        txt1.setTypeface(null, Typeface.BOLD);

        // adding TextView to the TableRow
        row.addView(txt1);
        Log.d(LOG_TAG,"Table Row Column 1 initialised and added to TableRow");


        // --------------------------------- Second Column----------------------------------------//
        // layout parameters for TextView
        tParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        tParams.setMargins(10,8,20,8);
        // TextView and styles
        TextView txt2 = new TextView(this);
        txt2.setText(":");
        txt2.setTextSize(18);
        txt2.setTextColor(Color.parseColor("#80640c"));
        txt2.setLayoutParams(tParams);

        // adding TextView to the TableRow
        row.addView(txt2);
        Log.d(LOG_TAG,"Table Row Column 2 initialised and added to TableRow");


        // ----------------------------- Third Column - value --------------------------------------//
        // layout parameters for TextView
        tParams = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        tParams.setMargins(0,8,0,8);
        // TextView and styles
        TextView txt3 = new TextView(this);
        txt3.setText(value);
        txt3.setTextSize(17);
        txt3.setTextColor(Color.parseColor("#806007"));
        txt3.setLayoutParams(tParams);

        txt3.setSingleLine(false);
        txt3.bringToFront();

        // adding TextView to the TableRow
        row.addView(txt3);
        Log.d(LOG_TAG,"Table Row Column 3 initialised and added to TableRow");

        return row;
    }

    public void loadFirebaseData(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                //TODO: Implement Methods to retrieve data from fireStore and initialise the array.
                // Use roomType variable for furniture roomType.

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!furniture.isEmpty()){
                            addProducts();
                        }
                    }
                });
            }


        }).start();
    }

    private void hideSystemUI() {
        /**
         * References: https://developer.android.com/training/system-ui/immersive
         * */
        // to hide the status bar and bottom navigation bar for full screen mode.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE

                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    public void onExit(View view) {
        // setting animations for the exit button
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_anim);
        view.startAnimation(animation);
        finish();
    }
}