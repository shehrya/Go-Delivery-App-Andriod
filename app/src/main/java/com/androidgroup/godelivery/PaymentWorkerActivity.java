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


        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {





            if (result.equals("OK"))
            {


                amount = jobDetails[8];



                Double amountDoubleFormat = Double.valueOf(amount);


                double rounded = (double) Math.round(amountDoubleFormat * 100.0) / 100.0;

                String roundedAmount = String.valueOf(rounded);

                paymentDescription.setText("Payment Requested!\n\n" + jobDetails[1] + " is requested to pay you ₦" + roundedAmount +".");

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

                        if(complexJobListingString.charAt(i) == ' ')
                        {
                            continue;
                        }

                        if (complexJobListingString.charAt(i) == '|') {
                            ++counter;
                            continue;
                        }


                        jobDetails[counter] = jobDetails[counter] + complexJobListingString.charAt(i);


                    }


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







    private class GetPaymentStatusFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return GetPaymentStatus(urls[0]);
            } catch (IOException e) {
                return "NotFound";
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


            // progressBar.setVisibility(View.GONE);



            if (result.length() > 0) {


                if (result.equals("OK"))
                {
                    paymentDescription.setText("Job Completed!\n\nPayment has been made by the Job Owner.");

                    RemoveJob();
                }

                else if (result.equals("NotFound"))
                {

                    new FetchAcceptedJobDetails().execute("http://192.168.0.185/AndroidApps/GoDelivery/AcceptedJobs/" + jobFileName);

                }
                else if (result.equals("NetworkError"))
                {

                    Toast.makeText(getApplicationContext(), "Network Problem", Toast.LENGTH_SHORT).show();

                }





            }



            //progressBar.setVisibility(View.GONE);











        }
    }

    private String GetPaymentStatus(String myurl) throws IOException, UnsupportedEncodingException {
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


            String readlineTextRate;

            String jobStatusString = null;



            while ((readlineTextRate = textReader.readLine()) != null) {


                jobStatusString = readlineTextRate;



            }




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


    public void RemoveJob()
    {

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {


                new RemoveJobListing().execute("http://192.168.0.185/AndroidApps/GoDelivery/AcceptedJobs/removeJobListing.php");

            }
        }.start();
    }





    private class RemoveJobListing extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return RemoveJob(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            if (result.equals("OK"))
            {
                Intent intent = new Intent(PaymentJobWorkerActivity.this, DeciderActivity.class);

                startActivity(intent);

                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Network Problem", Toast.LENGTH_SHORT).show();
            }



        }
    }

    private String RemoveJob(String myurl) throws IOException, UnsupportedEncodingException {

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
                    .appendQueryParameter("removeJobListing", jobID);


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



    public void LogOutClicked(View v)
    {
        LogoutUser();

        Intent intent = new Intent(PaymentJobWorkerActivity.this, AlreadyLoggedInActivity.class);

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
        Intent intent = new Intent(PaymentJobWorkerActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();

    }


}






