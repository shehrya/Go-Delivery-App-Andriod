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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.jar.Attributes;

public class ProfileActivity extends Activity {

    String loginEmailString = null;

    TextView ProfileSettingsTitle;

    EditText NameField;
    EditText PhoneNumberField;

    Button SaveChanges;
    Button ChangePassword;
    Button RefreshButton;

    ProgressBar progressBar;


    String name = "";
    String phone  = "";

    String []dataValues = new String[2];
    int counter = 0;

    LinearLayout wholeLayoutl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        LoadLoginEmail();

        dataValues[0] = "";
        dataValues[1] = "";

        counter = 0;



        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");

        wholeLayoutl = (LinearLayout) findViewById(R.id.wholeLayout);

        ProfileSettingsTitle = (TextView) findViewById(R.id.ProfileSettingsTitleID);

        NameField = (EditText) findViewById(R.id.nameID);
        PhoneNumberField = (EditText) findViewById(R.id.phoneNumberID);

        SaveChanges = (Button) findViewById(R.id.SaveChangesButtonID);
        ChangePassword = (Button) findViewById(R.id.ChangePasswordButtonID);

        RefreshButton = (Button) findViewById(R.id.RefreshButtonID);

        RefreshButton.setTypeface(font);
        RefreshButton.setTextColor(Color.WHITE);


        ProfileSettingsTitle.setTypeface(font);
        ProfileSettingsTitle.setTextColor(Color.WHITE);

        NameField.setTypeface(font);
        NameField.setTextColor(Color.WHITE);
        NameField.setHintTextColor(Color.WHITE);

        PhoneNumberField.setTypeface(font);
        PhoneNumberField.setTextColor(Color.WHITE);
        PhoneNumberField.setHintTextColor(Color.WHITE);

        SaveChanges.setTypeface(font);
        SaveChanges.setTextColor(Color.WHITE);

        ChangePassword.setTypeface(font);
        ChangePassword.setTextColor(Color.WHITE);

        progressBar = (ProgressBar) findViewById(R.id.ProgressBarID);


        new FetchProfileDetailsFromServer().execute("http://192.168.0.185/AndroidApps/GoDelivery/UsersDetails/Profiles/" + loginEmailString +".txt");

    }


    public void SaveChangesButtonClicked(View v)
    {

        if (NameField.length() > 0 && PhoneNumberField.length() > 0)
        {
            name  = NameField.getText().toString();
            phone = PhoneNumberField.getText().toString();

            new UpdateProfileOnServer().execute("http://192.168.0.185/AndroidApps/GoDelivery/UsersDetails/Profiles/UpdateProfile.php");
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Fill name, and phone fields correctly." , Toast.LENGTH_SHORT);
        }


    }

    public void ChangePasswordButtonClicked(View v)
    {

        Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
        finish();



    }


    private class FetchProfileDetailsFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return FetchProfileDetails(urls[0]);
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


            progressBar.setVisibility(View.GONE);

            SaveChanges.setVisibility(View.VISIBLE);
            ChangePassword.setVisibility(View.VISIBLE);



            if(result.equals("OK")) {

                NameField.setText(name);
                PhoneNumberField.setText(phone);

                wholeLayoutl.setVisibility(View.VISIBLE);



            }
            else
            {
                Toast.makeText(ProfileActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
            }




        }
    }

    private String FetchProfileDetails(String myurl) throws IOException, UnsupportedEncodingException {
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


            String readlineText;


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





            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return "OK";
            } else {
                return "NetworkError";
            }


        }finally{


            if (is != null) {
                is.close();


            }

        }
    }



    public void LoadLoginEmail()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loginEmailString = prefs.getString("GoDeliveryLoginEmail", null);


    }




    private class UpdateProfileOnServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return UpdateProfile(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressBar.setVisibility(View.VISIBLE);

            SaveChanges.setVisibility(View.GONE);
            ChangePassword.setVisibility(View.GONE);


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {



            if (result.equals("OK")) {


                new FetchProfileDetailsFromServer().execute("http://192.168.0.185/AndroidApps/GoDelivery/UsersDetails/Profiles/" + loginEmailString +".txt");


            }
            else
            {
                progressBar.setVisibility(View.GONE);

                SaveChanges.setVisibility(View.VISIBLE);
                ChangePassword.setVisibility(View.VISIBLE);


                Toast.makeText(ProfileActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String UpdateProfile(String myurl) throws IOException, UnsupportedEncodingException {

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
                    .appendQueryParameter("GoDeliveryName", name)
                    .appendQueryParameter("GoDeliveryEmail", loginEmailString)
                    .appendQueryParameter("GoDeliveryPhone", phone);

            String query = builder.build().getEncodedQuery();



            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
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


    public void RefreshClicked(View v)
    {
        Intent intent = new Intent(ProfileActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();

    }

}
