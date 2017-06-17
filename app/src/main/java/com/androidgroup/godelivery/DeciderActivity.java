package com.androidgroup.godelivery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;




public class DeciderActivity extends Activity {

    ProgressBar progressBar;

    String loginEmailString = null;

    String jobID = null;

    String name = null;
    String phone = null;



    String UserTypeString = null;

    String []dataValues = new String[2];
    int counter = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decider);

        LoadLoginEmail();


        dataValues[0] = "";
        dataValues[1] = "";

        counter = 0;

        progressBar = (ProgressBar) findViewById(R.id.LoginProgressBarID);

        new GetUserDetails().execute("http://192.168.0.185/AndroidApps/GoDelivery/UsersDetails/Profiles/" + loginEmailString + ".txt");



    }




    private class GetUserDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return GetDetails(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);

            dataValues[0] = "";
            dataValues[1] = "";

            counter = 0;


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            if (result.equals("OK"))
            {
                if (name != null && phone != null) {

                    SaveUserDetails();

                    new CheckForAcceptedJob().execute("http://192.168.0.185/AndroidApps/GoDelivery/AcceptedJobs/AcceptedJobsList.txt");
                }
            }
            else
            {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext() , "Network Problem" , Toast.LENGTH_SHORT).show();
            }


        }
    }

    private String GetDetails(String myurl) throws IOException, UnsupportedEncodingException {
        InputStream is = null;

        // Only display the first 500 characters of the retrieved
        // web page content.


        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();



            is = conn.getInputStream();

            BufferedReader textReader = new BufferedReader(new InputStreamReader(is));


            String readlineText;

            boolean FirstLineIsEmail = true;


            while ((readlineText = textReader.readLine()) != null) {




                if (readlineText.length() > 0) {

                    for (int i = 0; i < readlineText.length(); ++i) {
                        if (readlineText.charAt(i) == '|') {
                            ++counter;

                            continue;
                        }

                        dataValues[counter] = (dataValues[counter] + readlineText.charAt(i));
                    }


                    name = dataValues[0];
                    phone = dataValues[1];





                    counter = 0;
                    dataValues[0] = "";
                    dataValues[1] = "";


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


        } finally {


            if (is != null)
            {
                is.close();


            }

        }
    }








    private class CheckForAcceptedJob extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return CheckAcceptedJob(urls[0]);
            } catch (IOException e) {
                return "NotFound";
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {



            Log.e("chekcy", result + "");


            if(result.equals("OK"))
            {
                Intent intent = new Intent(DeciderActivity.this, UserTypeActivity.class);

                startActivity(intent);

                finish();
            }

            else if (result.equals("NotFound"))
            {

                Intent intent = new Intent(DeciderActivity.this, UserTypeActivity.class);

                startActivity(intent);

                finish();
            }
            else
            {


                if(result.equals("JobEmployer"))
                {

                    UserTypeString = "JobEmployer";
                }

                if(result.equals("JobWorker"))
                {
                    UserTypeString = "JobWorker";

                }

                new GetJobStatusFromServer().execute("http://192.168.0.185/AndroidApps/GoDelivery/JobsStatus/" + jobID + "-Status.txt");


            }




        }
    }

    private String CheckAcceptedJob(String myurl) throws IOException, UnsupportedEncodingException {
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


            String status = "OK";

            String readlineText;

            String complexAcceptedJobString = null;

            while ((readlineText = textReader.readLine()) != null) {


                if(readlineText.length() > 0 ) {

                    complexAcceptedJobString = readlineText;

                    String[] details = new String[3];

                    for (int i = 0; i < details.length; ++i) {
                        details[i] = "";

                    }

                    int counter = 0;


                    for (int i = 0; i < complexAcceptedJobString.length(); ++i) {

                        if (complexAcceptedJobString.charAt(i) == '|') {
                            ++counter;
                            continue;
                        }


                        details[counter] = details[counter] + complexAcceptedJobString.charAt(i);


                    }

                    if (details[1].equals(loginEmailString)) {
                        status = "JobEmployer";

                        jobID = details[0];

                        break;

                    } else if (details[2].equals(loginEmailString)) {
                        status = "JobWorker";

                        jobID = details[0];

                        break;
                    } else {
                        status = "OK";
                    }


                }
            }


            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                conn.disconnect();
                return status;

            }
            else
            {
                conn.disconnect();
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


    public void LoadLoginEmail()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loginEmailString = prefs.getString("GoDeliveryLoginEmail", null);


    }



    public void SaveUserDetails()
    {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("GoDeliveryName", name);
        editor.putString("GoDeliveryPhone", phone);

        editor.apply();

    }






    private class GetJobStatusFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return GetJobStatus(urls[0]);
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


            progressBar.setVisibility(View.GONE);



            if (result.length() > 0) {


                if (!result.equals("NetworkError")) {


                    //  Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();


                    if (result.equals("ACCEPTED"))
                    {


                        if(UserTypeString != null) {

                            if (UserTypeString.equals("JobEmployer")) {

                                Intent intent = new Intent(DeciderActivity.this, PreJobPhotoEmployerActivity.class);
                                intent.putExtra("AcceptedJobIDNumber", jobID);
                                startActivity(intent);
                                finish();
                            }

                            if (UserTypeString.equals("JobWorker")) {

                                Intent intent = new Intent(DeciderActivity.this, PreJobPhotoWorkerActivity.class);
                                intent.putExtra("AcceptedJobIDNumber", jobID);
                                startActivity(intent);
                                finish();
                            }
                        }



                    }
                    else if (result.equals("STARTED"))
                    {



                        if(UserTypeString != null) {

                            if (UserTypeString.equals("JobEmployer")) {

                                Intent intent = new Intent(DeciderActivity.this, EmployerMapActvity.class);
                                intent.putExtra("AcceptedJobIDNumber", jobID);
                                startActivity(intent);
                                finish();
                            }

                            if (UserTypeString.equals("JobWorker")) {

                                Intent intent = new Intent(DeciderActivity.this, WokerMapActivity.class);
                                intent.putExtra("AcceptedJobIDNumber", jobID);
                                startActivity(intent);
                                finish();
                            }
                        }


                    }


                    else if (result.equals("COMPLETED"))
                    {


                        if(UserTypeString != null) {

                            if (UserTypeString.equals("JobEmployer")) {

                                Intent intent = new Intent(DeciderActivity.this, PostJobPhotoEmployerActivity.class);
                                intent.putExtra("AcceptedJobIDNumber", jobID);
                                startActivity(intent);
                                finish();
                            }

                            if (UserTypeString.equals("JobWorker")) {

                                Intent intent = new Intent(DeciderActivity.this, PostJobPhotoWorkerActivity.class);
                                intent.putExtra("AcceptedJobIDNumber", jobID);
                                startActivity(intent);
                                finish();
                            }
                        }


                    }


                    else if (result.equals("PAYMENT"))
                    {


                        if(UserTypeString != null) {

                            if (UserTypeString.equals("JobEmployer")) {

                                Intent intent = new Intent(DeciderActivity.this, PaymentEmployerActivity.class);
                                intent.putExtra("AcceptedJobIDNumber", jobID);
                                startActivity(intent);
                                finish();
                            }

                            if (UserTypeString.equals("JobWorker")) {

                                Intent intent = new Intent(DeciderActivity.this, PaymentWorkerActivity.class);
                                intent.putExtra("AcceptedJobIDNumber", jobID);
                                startActivity(intent);
                                finish();
                            }
                        }


                    }





                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }



            //progressBar.setVisibility(View.GONE);











        }
    }

    private String GetJobStatus(String myurl) throws IOException, UnsupportedEncodingException {
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
                return jobStatusString;
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






}



}
