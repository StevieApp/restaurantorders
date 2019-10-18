package com.example.restaurantorders;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SpecificOrder extends AppCompatActivity {

    TextView specificorderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_order);

        specificorderid = findViewById(R.id.specificorderid);
        specificorderid.setText(getIntent().getStringExtra("text"));
    }
}
