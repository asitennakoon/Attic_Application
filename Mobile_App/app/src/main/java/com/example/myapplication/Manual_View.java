package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

//TODO: Use this sample code to pass the item type
// Put the following  4 lines of code in the on click listener used to create this activity.
// Add <CLASS_NAME>.
//public static final String TITLE_KEY = "com.example.myapplication.<CLASS_NAME>.TITLE";
//Intent in = new Intent(getApplicationContext(), Manual_View.class);
//        in.putExtra(TITLE_KEY, "SOFA");
//        startActivity(in);

public class Manual_View extends AppCompatActivity {

    private static final String LOG_TAG = Manual_View.class.getSimpleName();

    private LinearLayout layout;
    ArrayList<Furniture> furniture = new ArrayList<>();

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_view);

        TextView title = findViewById(R.id.activity_manual_title);
        Intent intent = getIntent();
        //TODO: add the Key
//        type = intent.getStringExtra(MainActivity.TITLE_KEY);
        title.setText(type);

        layout = findViewById(R.id.activity_manual_linear_layout);

        //TODO: Remove these hardcoded data after firebase load data is implemented.

        furniture.add(new Furniture("Bedroom Chair 2","Black", "AAAAAAAAAAAAAAAAAAAAAAAAAAAjskgnlsgn"
        , "Damro", "leather", "price", "10"));

        furniture.add(new Furniture("Bedroom Table 2","Black", "AAAAAAAAAAAAAAAAAAAAAAAAAAAjskgnlsgn"
                , "Damro", "leather", "price", "10"));

        furniture.add(new Furniture("Bedroom Sofa 2","Black", "AAAAAAAAAAAAAAAAAAAAAAAAAAAjskgnlsgn"
                , "Damro", "leather", "price", "10"));


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
        cParams.setMargins(40,40,40,40);

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
        tParams.setMargins(0,8,0,8);

        TextView heading = new TextView(this);
        heading.setText(f.getName());
        heading.setLayoutParams(tParams);
        heading.setTextColor(Color.parseColor("#80640c"));
        heading.setTextSize(18);
        heading.setTranslationX(10);
        Log.d(LOG_TAG,"CardView LinearLayout Heading initialised.");

        // ---------------------------- Product image ----------------------------------//
        ImageView image = new ImageView(this);
        //TODO: get relevant image from firebase storage using furniture name ( f.getName() )
        image.setImageResource(R.drawable.sofa);
        image.setMaxWidth(238);
        // layout parameters and margins
        image.setMaxHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        image.setMinimumHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        Log.d(LOG_TAG,"CardView LinearLayout image initialised.");

        //----------------------------- product data ----------------------------------//

        // Table
        // layout parameters and margins
        TableLayout.LayoutParams tableParams =new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tableParams.setMargins(20,20,20,20);

        TableLayout table = new TableLayout(this);
        table.setLayoutParams(tableParams);
        table.setStretchAllColumns(true);
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
        txt1.setTextSize(18);
        txt1.setTextColor(Color.parseColor("#80640c"));
        txt1.setLayoutParams(tParams);

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
        ScrollView s = new ScrollView(this);
        tParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        tParams.setMargins(0,8,0,8);
        // TextView and styles
        TextView txt3 = new TextView(this);
        txt3.setText(value);
        txt3.setTextSize(18);
        txt3.setTextColor(Color.parseColor("#806007"));
        txt3.setLayoutParams(tParams);

        s.addView(txt3);

        // adding TextView to the TableRow
        row.addView(s);
        Log.d(LOG_TAG,"Table Row Column 3 initialised and added to TableRow");


        return row;
    }

    public void loadFirebaseData(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                //TODO: Implement Methods to retrieve data from fireStore and initialise the array.
                // Use type variable for furniture type.

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
}