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
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentWorkerActivity extends Activity {

    String jobID = null;
    String jobFileName = null;

    TextView paymentDescription;
    TextView paymentTitle;

    String[] jobDetails = new String[22];

    String amount = null;

    Button LogOutButton;
    Button RefreshButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_job_worker);

        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");

        paymentTitle = (TextView) findViewById(R.id.PaymentJobWorkerTitleID);
        paymentDescription = (TextView) findViewById(R.id.PaymentJobWorkerDescriptionID);

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



        Intent intent  = getIntent();

        jobID = intent.getStringExtra("AcceptedJobIDNumber");


        for (int i = 0; i < jobDetails.length; ++i) {
            jobDetails[i] = "";

        }


        jobFileName = (jobID + ".txt");







        new GetPaymentStatusFromServer().execute("http://192.168.0.185/AndroidApps/GoDelivery/PaymentsStatus/" + jobID + "-Status.txt");



    }

    private class FetchAcceptedJobDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return FetchJobDetails(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //progressBar.setVisibility(View.VISIBLE);
        }






}



}
