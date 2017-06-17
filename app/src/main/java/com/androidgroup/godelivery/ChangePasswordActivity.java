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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class ChangePasswordActivity extends Activity {

    TextView Title;

    EditText NewPasswordField;
    EditText ConfirmPasswordField;

    Button ChangePassword;
    Button RefreshButton;

    String NewPassword = "";
    String ConfirmPassword = "";

    String loginEmailString = null;

    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        LoadLoginEmail();

        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");


        Title = (TextView) findViewById(R.id.ChangePasswordTitleID);

        NewPasswordField = (EditText) findViewById(R.id.NewPasswordID);
        ConfirmPasswordField = (EditText) findViewById(R.id.ConfirmPasswordID);

        ChangePassword = (Button) findViewById(R.id.ChangePasswordButtonID);
        RefreshButton = (Button) findViewById(R.id.RefreshButtonID);

        RefreshButton.setTypeface(font);
        RefreshButton.setTextColor(Color.WHITE);

        NewPasswordField.setTypeface(font);
        NewPasswordField.setTextColor(Color.WHITE);
        NewPasswordField.setHintTextColor(Color.WHITE);

        ConfirmPasswordField.setTypeface(font);
        ConfirmPasswordField.setTextColor(Color.WHITE);
        ConfirmPasswordField.setHintTextColor(Color.WHITE);

        Title.setTypeface(font);
        Title.setTextColor(Color.WHITE);

        ChangePassword.setTypeface(font);
        ChangePassword.setTextColor(Color.WHITE);

        progressBar = (ProgressBar) findViewById(R.id.ProgressBarID);


    }


    public void ChangePasswordButtonClicked(View v)
    {

        NewPassword = NewPasswordField.getText().toString();
        ConfirmPassword = ConfirmPasswordField.getText().toString();

        if(NewPassword.length() >= 4 && ConfirmPassword.length() >= 4)
        {
            if (NewPassword.equals(ConfirmPassword))
            {
                new ChangePasswordOnServer().execute("http://192.168.0.185/AndroidApps/GoDelivery/UsersDetails/EmailsPasswords/UpdatePassword.php");

            }
            else
            {
                Toast.makeText(getApplicationContext(), "New & Confirm Passwords does not match", Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(), "New & Confirm Passwords should be greater than or equal to four digits and both passwords should match", Toast.LENGTH_SHORT).show();
        }




    }







    private class ChangePasswordOnServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return ChangePassword(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            ChangePassword.setVisibility(View.GONE);

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            progressBar.setVisibility(View.GONE);
            ChangePassword.setVisibility(View.VISIBLE);


            if (result.equals("OK")) {

                Intent intent = new Intent(ChangePasswordActivity.this, AlreadyLoggedInActivity.class);
                startActivity(intent);
                finish();

            }
            else
            {
                Toast.makeText(ChangePasswordActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private String ChangePassword(String myurl) throws IOException, UnsupportedEncodingException {

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
                    .appendQueryParameter("GoDeliveryEmail", loginEmailString)
                    .appendQueryParameter("GoDeliveryPassword", NewPassword);;

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








    public void LoadLoginEmail()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loginEmailString = prefs.getString("GoDeliveryLoginEmail", null);


    }



    public void RefreshClicked(View v)
    {
        Intent intent = new Intent(ChangePasswordActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();


    }



}
