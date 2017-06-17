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


public class ForgotPasswordActivity extends Activity {

    TextView Title;
    TextView SecurityQuestion;

    EditText NewPasswordField;
    EditText ConfirmPasswordField;
    EditText SecurityAnswerField;
    EditText EmailField;


    Button ResetPassword;
    Button RefreshButton;

    String NewPassword = "";
    String ConfirmPassword = "";
    String Email = "";
    String SecretAnswer = "";


    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");


        Title = (TextView) findViewById(R.id.ForgotPasswordTitleID);
        SecurityQuestion = (TextView) findViewById(R.id.SecretQuestionID);

        NewPasswordField = (EditText) findViewById(R.id.NewPasswordID);
        ConfirmPasswordField = (EditText) findViewById(R.id.ConfirmPasswordID);
        SecurityAnswerField = (EditText) findViewById(R.id.SecurityAnswerID);
        EmailField = (EditText) findViewById(R.id.EmailID);



        ResetPassword = (Button) findViewById(R.id.ResetPasswordButtonID);

        RefreshButton = (Button) findViewById(R.id.RefreshButtonID);

        RefreshButton.setTypeface(font);
        RefreshButton.setTextColor(Color.WHITE);


        NewPasswordField.setTypeface(font);
        NewPasswordField.setTextColor(Color.WHITE);
        NewPasswordField.setHintTextColor(Color.WHITE);

        ConfirmPasswordField.setTypeface(font);
        ConfirmPasswordField.setTextColor(Color.WHITE);
        ConfirmPasswordField.setHintTextColor(Color.WHITE);

        SecurityAnswerField.setTypeface(font);
        SecurityAnswerField.setTextColor(Color.WHITE);
        SecurityAnswerField.setHintTextColor(Color.WHITE);

        EmailField.setTypeface(font);
        EmailField.setTextColor(Color.WHITE);
        EmailField.setHintTextColor(Color.WHITE);

        Title.setTypeface(font);
        Title.setTextColor(Color.WHITE);

        SecurityQuestion.setTypeface(font);
        SecurityQuestion.setTextColor(Color.WHITE);

        ResetPassword.setTypeface(font);
        ResetPassword.setTextColor(Color.WHITE);

        progressBar = (ProgressBar) findViewById(R.id.ProgressBarID);


    }


    public void ResetPasswordButtonClicked(View v)
    {

        SecretAnswer = SecurityAnswerField.getText().toString();
        Email = EmailField.getText().toString();
        NewPassword = NewPasswordField.getText().toString();
        ConfirmPassword = ConfirmPasswordField.getText().toString();

        if(SecretAnswer.length() > 0 && isEmailValid(Email) && NewPassword.length() >= 4 && ConfirmPassword.length() >= 4)
        {

            new VerifySecretAnswerFromServer().execute("http://192.168.0.185/AndroidApps/GoDelivery/UsersDetails/SecurityQuestions/" + Email + ".txt");



        }
        else
        {
            Toast.makeText(getApplicationContext(), "Fill all fields correctly", Toast.LENGTH_SHORT).show();
        }




    }




    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



    private class VerifySecretAnswerFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return VerifySecretAnswer(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            ResetPassword.setVisibility(View.GONE);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            if (!result.equals("NetworkError")) {

                if (result.equals("Matched")) {



                    if (NewPassword.equals(ConfirmPassword))
                    {
                        new ResetPasswordOnServer().execute("http://192.168.0.185/AndroidApps/GoDelivery/UsersDetails/EmailsPasswords/UpdatePassword.php");

                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        ResetPassword.setVisibility(View.VISIBLE);

                        Toast.makeText(getApplicationContext(), "New & Confirm Passwords does not match", Toast.LENGTH_SHORT).show();
                    }



                } else {

                    progressBar.setVisibility(View.GONE);
                    ResetPassword.setVisibility(View.VISIBLE);


                    Toast.makeText(ForgotPasswordActivity.this, "Secret Code or Email is incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            else
            {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(ForgotPasswordActivity.this, "Secret Code or Email is incorrect or network problem", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String VerifySecretAnswer(String myurl) throws IOException, UnsupportedEncodingException {
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

            String SecretAnswerStatus = "NotMatched";


            while ((readlineText = textReader.readLine()) != null) {


                if(readlineText.equals(SecretAnswer))
                {
                    SecretAnswerStatus = "Matched";
                    break;
                }

            }



            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                return SecretAnswerStatus;
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






    private class ResetPasswordOnServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return ResetPassword(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            ResetPassword.setVisibility(View.GONE);

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            progressBar.setVisibility(View.GONE);
            ResetPassword.setVisibility(View.VISIBLE);


            if (result.equals("OK")) {

                Intent intent = new Intent(ForgotPasswordActivity.this, AlreadyLoggedInActivity.class);
                startActivity(intent);
                finish();

            }
            else
            {
                Toast.makeText(ForgotPasswordActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private String ResetPassword(String myurl) throws IOException, UnsupportedEncodingException {

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
                    .appendQueryParameter("GoDeliveryEmail", Email)
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




    public void RefreshClicked(View v)
    {
        Intent intent = new Intent(ForgotPasswordActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();


    }


}
