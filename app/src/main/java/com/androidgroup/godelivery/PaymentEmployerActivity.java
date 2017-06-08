package com.androidgroup.godelivery;

import android.app.Activity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;




public class PaymentEmployerActivity extends Activity {

    String jobID = null;
    String jobFileName = null;

    TextView paymentDescription;
    TextView paymentTitle;

    Button LogOutButton;
    Button RefreshButton;


    Button payButton;

    String[] jobDetails = new String[22];

    String amount = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_job_employer);

        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");

        paymentDescription = (TextView) findViewById(R.id.PaymentJobEmployerDescriptionID);

        paymentTitle = (TextView) findViewById(R.id.PaymentJobEmployerTitleID);

        payButton = (Button) findViewById(R.id.PaymentJobEmployerButtonID);

        LogOutButton = (Button) findViewById(R.id.LogoutButtonID);

        LogOutButton.setTypeface(font);
        LogOutButton.setTextColor(Color.WHITE);

        RefreshButton = (Button) findViewById(R.id.RefreshButtonID);

        RefreshButton.setTypeface(font);
        RefreshButton.setTextColor(Color.WHITE);



        paymentTitle.setTypeface(font);
        paymentTitle.setTextColor(Color.WHITE);

        paymentDescription.setTypeface(font);
        paymentDescription.setTextColor(Color.WHITE);

        payButton.setTypeface(font);
        payButton.setTextColor(Color.WHITE);






    }


}
