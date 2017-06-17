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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100 && resultCode == RESULT_OK) {

            Bitmap bitmapPhoto = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(bitmapPhoto);

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmapPhoto.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] ba = bao.toByteArray();
            ba1 =  Base64.encodeToString(ba, Base64.DEFAULT);

            // Upload image to server
            new SendPostJobPhotoToServer().execute("http://192.168.0.185/AndroidApps/GoDelivery/PostJobPhotos/PostJobPhotoUpload.php");


        }



    }




    public void jobDetailsPostPhotoWorkerButton(View v)
    {


        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");


        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_job_details, null);



        TextView jobTitleTextView = (TextView) dialoglayout.findViewById(R.id.GeneralJobTitleID);
        TextView distanceTextView = (TextView) dialoglayout.findViewById(R.id.GeneralDistanceID);
        TextView rateTextView = (TextView) dialoglayout.findViewById(R.id.GeneralRateID);
        TextView weightTextView = (TextView) dialoglayout.findViewById(R.id.GeneralWeightID);
        TextView dimensionsTextView = (TextView) dialoglayout.findViewById(R.id.GeneralDimensionID);
        TextView jobEmployerNameTextView = (TextView) dialoglayout.findViewById(R.id.GeneralJobOperaterNameID);
        TextView jobEmployerContactNoTextView = (TextView) dialoglayout.findViewById(R.id.GeneralJobOperatorNumberID);

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

        jobEmployerNameTextView.setTypeface(font);
        jobEmployerNameTextView.setTextColor(Color.BLACK);

        jobEmployerContactNoTextView.setTypeface(font);
        jobEmployerContactNoTextView.setTextColor(Color.BLACK);


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



        jobEmployerNameTextView.setText("Job Employer Name\n" + jobDetails[1]);
        jobEmployerContactNoTextView.setText("Job Employer Contact Number\n" + jobDetails[5]);


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

    public void PostPhotoWorkerButton(View v)
    {

        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {

            textDescription.setVisibility(View.GONE);
            // Open default camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, 100);

        } else {
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }

    }






    private class SendPostJobPhotoToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return SendPostJobPhoto(urls[0]);
            } catch (IOException e) {

                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            progressBar.setVisibility(View.GONE);

            if (result.equals("OK")) {

                new RetrievePostJobPhotoToServer().execute("http://192.168.0.185/AndroidApps/GoDelivery/PostJobPhotos/" + JobID + "-PostPhoto.jpg");
            }
            else
            {

            }


        }
    }

    private String SendPostJobPhoto(String myurl) throws IOException, UnsupportedEncodingException {

        OutputStream os = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // Starts the query
            conn.connect();


            os = conn.getOutputStream();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("GoDeliveryPostbase64", ba1)
                    .appendQueryParameter("GoDeliveryPostImageName", (JobID + "-PostPhoto.jpg"));



            String query = builder.build().getEncodedQuery();



            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();

            // Convert the InputStream into a string
            // String contentAsString = readIt(is, len);


            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                return "OK";
            }
            else
            {
                return "NetworkError";
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {

            if (os != null)
            {
                os.close();

            }

        }
    }







    private class RetrievePostJobPhotoToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return RetrievePostJobPhoto(urls[0]);
            } catch (IOException e) {


                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            textDescription.setVisibility(View.GONE);


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            progressBar.setVisibility(View.GONE);

            if(result.equals("OK")) {


                if (photo != null)
                {
                    imageView.setImageBitmap(photo);
                    imageView.setVisibility(View.VISIBLE);
                    postJobPhotoButton.setVisibility(View.GONE);
                    jobStatus.setText("Waiting for Post-Delivery Photo approval by Job Employer");

                }

            }
            else if (result.equals("NetworkError"))
            {



            }
            else
            {
                textDescription.setVisibility(View.VISIBLE);
                postJobPhotoButton.setVisibility(View.VISIBLE);

                jobStatus.setText("Post Delivery");
            }




        }
    }

    private String RetrievePostJobPhoto(String myurl) throws IOException, UnsupportedEncodingException {
        InputStream is = null;

        // Only display the first 500 characters of the retrieved
        // web page content.


        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();



            is = conn.getInputStream();

            BufferedReader textReader = new BufferedReader(new InputStreamReader(is));



            BufferedInputStream bis = new BufferedInputStream(is, 8190);

            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte)current);
            }
            byte[] imageData = baf.toByteArray();
            photo = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);



            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                return "OK";
            }
            else
            {
                return "NetworkError";
            }


        } finally {


            if (is != null)
            {
                is.close();


            }

        }
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

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {



            if(result.equals("OK"))
            {
                jobDetailsButton.setVisibility(View.VISIBLE);

                new RetrievePostJobPhotoToServer().execute("http://192.168.0.185/AndroidApps/GoDelivery/PostJobPhotos/" + JobID + "-PostPhoto.jpg");

            }




        }
    }

    private String FetchJobDetails(String myurl) throws IOException, UnsupportedEncodingException {
        InputStream is = null;

        // Only display the first 500 characters of the retrieved
        // web page content.


        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();



            is = conn.getInputStream();

            BufferedReader textReader = new BufferedReader(new InputStreamReader(is));


            String readlineTextListing;
            String complexJobListingString= null;




            while ((readlineTextListing = textReader.readLine()) != null) {

                complexJobListingString = readlineTextListing;


                if(complexJobListingString.length() > 25) {



                    int counter = 0;


                    for (int i = 0; i < complexJobListingString.length(); ++i) {

                        if (complexJobListingString.charAt(i) == '|') {
                            ++counter;
                            continue;
                        }


                        jobDetails[counter] = jobDetails[counter] + complexJobListingString.charAt(i);


                    }


                    break;

                }





            }






            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                return "OK";
            }
            else
            {
                return "NetworkError";
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {


            if (is != null)
            {
                is.close();


            }

        }
    }



    public void LogOutClicked(View v)
    {
        LogoutUser();

        Intent intent = new Intent(PostJobPhotoWorkerActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();

    }

    public void LogoutUser()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("GoDeliveryLoginEmail", null);
        editor.apply();
    }


    public void RefreshClicked(View v)
    {
        Intent intent = new Intent(PostJobPhotoWorkerActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();


    }


}
