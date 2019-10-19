package com.example.restaurantorders;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SpecificOrder extends AppCompatActivity {

    TextView specificorderid,
            ownername,
            dispatchstatus,
            ownernumber,
            textownermail,
            textcreateddate,
            textcountry,
            textdeliveryplace,
            textdeliverynumber,
            textdeliverycharges,
            textpaytype,
            textpayamt,
            textpayableamt,
            textpaystatus,
            textdispatchstatus,
            textpayref,
            textpayno,
            textdispatchdate;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_order);

        specificorderid = findViewById(R.id.specificorderid);
        toolbar = findViewById(R.id.toolbar);
        ownername = findViewById(R.id.ownername);
        dispatchstatus = findViewById(R.id.dispatchstatus);
        ownernumber = findViewById(R.id.ownernumber);
        textownermail = findViewById(R.id.textownermail);
        textcreateddate = findViewById(R.id.textcreateddate);
        textcountry = findViewById(R.id.textcountry);
        textdeliveryplace = findViewById(R.id.textdeliveryplace);
        textdeliverynumber = findViewById(R.id.textdeliverynumber);
        textdeliverycharges = findViewById(R.id.textdeliverycharges);
        textpaytype = findViewById(R.id.textpaytype);
        textpayamt = findViewById(R.id.textpayamt);
        textpayableamt = findViewById(R.id.textpayableamt);
        textpaystatus = findViewById(R.id.textpaystatus);
        textdispatchstatus = findViewById(R.id.textdispatchstatus);
        textpayref = findViewById(R.id.textpayref);
        textpayno = findViewById(R.id.textpayno);
        textdispatchdate = findViewById(R.id.textdispatchdate);

        specificorderid.setText(getIntent().getStringExtra("text"));
        toolbar.setTitle(getIntent().getStringExtra("text"));
        ownername.setText(getIntent().getStringExtra("ownername"));
        dispatchstatus.setText(getIntent().getStringExtra("dispatchstatus"));
        if (getIntent().getStringExtra("dispatchstatus").equals("Pending")) {
            dispatchstatus.setTextColor(Color.GREEN);
            textdispatchstatus.setTextColor(Color.GREEN);
        } else {
            dispatchstatus.setTextColor(Color.RED);
            textdispatchstatus.setTextColor(Color.RED);
        }
        ownernumber.setText(getIntent().getStringExtra("ownernumber"));
        textownermail.setText(getIntent().getStringExtra("textownermail"));
        textcreateddate.setText(getIntent().getStringExtra("textcreateddate"));
        textcountry.setText(getIntent().getStringExtra("textcountry"));
        textdeliveryplace.setText(getIntent().getStringExtra("textdeliveryplace"));
        textdeliverynumber.setText(getIntent().getStringExtra("textdeliverynumber"));
        textdeliverycharges.setText(getIntent().getStringExtra("textdeliverycharges"));
        textpaytype.setText(getIntent().getStringExtra("textpaytype"));
        textpayamt.setText(getIntent().getStringExtra("textpayamt"));
        textpayableamt.setText(getIntent().getStringExtra("textpayableamt"));
        textpaystatus.setText(getIntent().getStringExtra("textpaystatus"));
        textdispatchstatus.setText(getIntent().getStringExtra("dispatchstatus"));
        textpayref.setText(getIntent().getStringExtra("textpayref"));
        textpayno.setText(getIntent().getStringExtra("textpayno"));
        textdispatchdate.setText(getIntent().getStringExtra("textdispatchdate"));
    }
}
