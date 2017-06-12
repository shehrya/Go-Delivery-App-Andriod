package com.androidgroup.godelivery;

import android.app.Activity;
import android.os.Bundle;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.ByteArrayBuffer;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class PreJobPhotoEmployerActivity extends Activity {


    AlertDialog alertDw;
    AlertDialog.Builder builder;


    String JobID = null;
    String jobFileName = null;

    String[] jobDetails = new String[22];

    private Uri fileUri;

    ImageView imageView;

    String ba1 = "";

    ProgressBar progressBar;


    Bitmap photo = null;

    TextView jobStatusTitle;
    TextView jobStatus;

    TextView textDescription;

    Button approvePhotoButton;
    Button jobDetailsButton;

    Button LogOutButton;

    Button RefreshButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_photo_employer);

        Intent intent = getIntent();
        JobID = intent.getStringExtra("AcceptedJobIDNumber");

        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");

        jobStatusTitle = (TextView) findViewById(R.id.PreJobEmployerStatusTitleID);
        jobStatus = (TextView) findViewById(R.id.PreJobEmployerStatusID);
        progressBar = (ProgressBar) findViewById(R.id.PrePhotoEmployerProgressBarID);
        imageView = (ImageView) findViewById(R.id.PrePhotoEmployerImageID);
        textDescription = (TextView) findViewById(R.id.PrePhotoEmployerTextDescriptionID);
        approvePhotoButton = (Button) findViewById(R.id.PreJobEmployerApproveButtonID);
        jobDetailsButton = (Button) findViewById(R.id.PreJobEmployerJobDetailsButtonID);
        LogOutButton = (Button) findViewById(R.id.LogoutButtonID);

        LogOutButton.setTypeface(font);
        LogOutButton.setTextColor(Color.WHITE);

        RefreshButton = (Button) findViewById(R.id.RefreshButtonID);

        RefreshButton.setTypeface(font);
        RefreshButton.setTextColor(Color.WHITE);






        jobStatusTitle.setTypeface(font);
        jobStatusTitle.setTextColor(Color.WHITE);

        jobStatus.setTypeface(font);
        jobStatus.setTextColor(Color.WHITE);

        textDescription.setTypeface(font);
        textDescription.setTextColor(Color.WHITE);

        approvePhotoButton.setTypeface(font);
        approvePhotoButton.setTextColor(Color.WHITE);

        jobDetailsButton.setTypeface(font);
        jobDetailsButton.setTextColor(Color.WHITE);


        for (int i = 0; i < jobDetails.length; ++i) {
            jobDetails[i] = "";

        }


        jobFileName = (JobID + ".txt");


        new FetchAcceptedJobDetails().execute("http://192.168.0.185/AndroidApps/GoDelivery/AcceptedJobs/" + jobFileName);









    }




    public void jobDetailsPrePhotoEmployerButton(View v) {


        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");


        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_job_details, null);



        TextView jobTitleTextView = (TextView) dialoglayout.findViewById(R.id.GeneralJobTitleID);
        TextView distanceTextView = (TextView) dialoglayout.findViewById(R.id.GeneralDistanceID);
        TextView rateTextView = (TextView) dialoglayout.findViewById(R.id.GeneralRateID);
        TextView weightTextView = (TextView) dialoglayout.findViewById(R.id.GeneralWeightID);
        TextView dimensionsTextView = (TextView) dialoglayout.findViewById(R.id.GeneralDimensionID);
        TextView jobWorkerNameTextView = (TextView) dialoglayout.findViewById(R.id.GeneralJobOperaterNameID);
        TextView jobWorkerContactNoTextView = (TextView) dialoglayout.findViewById(R.id.GeneralJobOperatorNumberID);

        TextView pickUpAddressTextView = (TextView) dialoglayout.findViewById(R.id.GeneralPickUpPersonAddressID);
        TextView deliveryAddressTextView = (TextView) dialoglayout.findViewById(R.id.GeneralDeliveryPersonAddressID);

        TextView pickUpPersonNameTextView = (TextView) dialoglayout.findViewById(R.id.GeneralPickUpPersonNameID);
        TextView pickUpPersonContactNumberTextView = (TextView) dialoglayout.findViewById(R.id.GeneralPickUpPersonContactNumberID);

        TextView deliveryPersonNameTextView = (TextView) dialoglayout.findViewById(R.id.GeneralDeliveryPersonNameID);
        TextView deliveryPersonContactNumberTextView = (TextView) dialoglayout.findViewById(R.id.GeneralDeliveryPersonContactNumberID);


        jobTitleTextView.setTypeface(font);
        jobTitleTextView.setTextColor(Color.BLACK);

        distanceTextView.setTypeface(font);
        distanceTextView.setTextColor(Color.BLACK);

        rateTextView.setTypeface(font);
        rateTextView.setTextColor(Color.BLACK);

        weightTextView.setTypeface(font);
        weightTextView.setTextColor(Color.BLACK);

        dimensionsTextView.setTypeface(font);
        dimensionsTextView.setTextColor(Color.BLACK);

        jobWorkerNameTextView.setTypeface(font);
        jobWorkerNameTextView.setTextColor(Color.BLACK);

        jobWorkerContactNoTextView.setTypeface(font);
        jobWorkerContactNoTextView.setTextColor(Color.BLACK);

        pickUpAddressTextView.setTypeface(font);
        pickUpAddressTextView.setTextColor(Color.BLACK);

        deliveryAddressTextView.setTypeface(font);
        deliveryAddressTextView.setTextColor(Color.BLACK);



        pickUpPersonNameTextView.setTypeface(font);
        pickUpPersonNameTextView.setTextColor(Color.BLACK);


        pickUpPersonContactNumberTextView.setTypeface(font);
        pickUpPersonContactNumberTextView.setTextColor(Color.BLACK);


        deliveryPersonNameTextView.setTypeface(font);
        deliveryPersonNameTextView.setTextColor(Color.BLACK);


        deliveryPersonContactNumberTextView.setTypeface(font);
        deliveryPersonContactNumberTextView.setTextColor(Color.BLACK);





        Double distanceDoubleFormat = Double.valueOf(jobDetails[7]);
        double roundDistance = (double) Math.round(distanceDoubleFormat * 100.0) / 100.0;
        String roundedDistance = String.valueOf(roundDistance);

        Double amountDoubleFormat = Double.valueOf(jobDetails[8]);
        double roundAmount = (double) Math.round(amountDoubleFormat * 100.0) / 100.0;
        String roundedAmount = String.valueOf(roundAmount);





        jobTitleTextView.setText("Job Title\n" + jobDetails[9]);

        distanceTextView.setText("Distance\n" + roundedDistance + " KM");
        rateTextView.setText("Cost\n₦" + roundedAmount);
        weightTextView.setText("Weight\n" + jobDetails[10] + " KG");
        dimensionsTextView.setText("Size/Dimension (LxWxH)\n" + jobDetails[11] + " inches");



        jobWorkerNameTextView.setText("Job Worker Name\n" + jobDetails[3]);
        jobWorkerContactNoTextView.setText("Job Worker Contact Number\n" + jobDetails[6]);


        pickUpAddressTextView.setText("Pick Up Address\n" + jobDetails[14]);
        deliveryAddressTextView.setText("Delivery Address\n" + jobDetails[19]);

        pickUpPersonNameTextView.setText("Pick Up Person Name\n" + jobDetails[12]);
        pickUpPersonContactNumberTextView.setText("Pick Up Person Contact Number\n" + jobDetails[13]);

        deliveryPersonNameTextView.setText("Delivery Person Name\n" + jobDetails[17]);
        deliveryPersonContactNumberTextView.setText("Delivery Person Contact Number\n" + jobDetails[18]);


        builder = new AlertDialog.Builder(this);

        builder.setView(dialoglayout);
        builder.setTitle(JobID);

        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alertDw.dismiss();
            }
        });


        alertDw = builder.create();


        alertDw.show();





    }




}


}





