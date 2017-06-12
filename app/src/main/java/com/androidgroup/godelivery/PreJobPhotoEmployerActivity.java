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





    }


}





