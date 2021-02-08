package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Category extends AppCompatActivity  {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_screen);
    }

    public void loadProducts(View view){
        Intent loadProductsIntent = new Intent(view.getContext(), Camera.class);
        startActivityForResult(loadProductsIntent,0);
    }
}
