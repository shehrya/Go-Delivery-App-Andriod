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



public class PostJobPhotoWorkerActivity extends Activity {


    String JobID = null;

    String jobFileName = null;

    String[] jobDetails = new String[22];





    AlertDialog alertDw;
    AlertDialog.Builder builder;


    private Uri fileUri;

    ImageView imageView;

    String ba1 = "";

    ProgressBar progressBar;



    Bitmap photo =  null;

    TextView jobStatus;

    TextView textDescription;

    Button postJobPhotoButton;




    TextView jobStatusTitle;

    Button jobDetailsButton;

    Button LogOutButton;

    Button RefreshButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_photo_worker);


        Intent intent = getIntent();
        JobID = intent.getStringExtra("AcceptedJobIDNumber");


        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");

        jobStatusTitle = (TextView) findViewById(R.id.PostJobWorkerStatusTitleID);

        jobStatus = (TextView) findViewById(R.id.PostJobWorkerStatusID);

        progressBar = (ProgressBar) findViewById(R.id.PostPhotoWorkerProgressBarID);

        imageView = (ImageView) findViewById(R.id.PostPhotoWorkerImageID);

        textDescription = (TextView) findViewById(R.id.PostPhotoWorkerTextDescriptionID);

        postJobPhotoButton = (Button) findViewById(R.id.PostPhotoWorkerButtonID);

        jobDetailsButton = (Button) findViewById(R.id.PostJobWorkerJobDetailsButtonID);

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

        postJobPhotoButton.setTypeface(font);
        postJobPhotoButton.setTextColor(Color.WHITE);

        jobDetailsButton.setTypeface(font);
        jobDetailsButton.setTextColor(Color.WHITE);



        for (int i = 0; i < jobDetails.length; ++i) {
            jobDetails[i] = "";

        }


        jobFileName = (JobID + ".txt");


        new FetchAcceptedJobDetails().execute("http://192.168.0.185/AndroidApps/GoDelivery/AcceptedJobs/" + jobFileName);






    }




}


}
