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
import java.net.URL

public class SignUpActivity extends Activity {

    EditText nameField;
    EditText emailField;
    EditText passwordField;
    EditText phoneField;
    EditText securityAnswerField;

    String name = "";
    String email = "";
    String password = "";
    String phone = "";
    String securityAnswer = "";

    TextView Title;
    TextView SecretQuestion;

    Button submitButton;
    Button RefreshButton;


    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);


            Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");

            progressBar = (ProgressBar) findViewById(R.id.SignUpProgressBarID);

            nameField = (EditText) findViewById(R.id.nameID);
            emailField = (EditText) findViewById(R.id.emailID);
            passwordField = (EditText) findViewById(R.id.passwordID);
            phoneField = (EditText) findViewById(R.id.phoneNumberID);
            securityAnswerField = (EditText) findViewById(R.id.SecurityAnswerID);

            Title = (TextView) findViewById(R.id.SignUpTitleID);
            SecretQuestion = (TextView) findViewById(R.id.SecretQuestionID);

            submitButton = (Button) findViewById(R.id.SignUpSubmitButtonID);

            RefreshButton = (Button) findViewById(R.id.RefreshButtonID);

            RefreshButton.setTypeface(font);
            RefreshButton.setTextColor(Color.WHITE);

            nameField.setTypeface(font);
            nameField.setTextColor(Color.WHITE);
            nameField.setHintTextColor(Color.WHITE);

            emailField.setTypeface(font);
            emailField.setTextColor(Color.WHITE);
            emailField.setHintTextColor(Color.WHITE);

            passwordField.setTypeface(font);
            passwordField.setTextColor(Color.WHITE);
            passwordField.setHintTextColor(Color.WHITE);

            phoneField.setTypeface(font);
            phoneField.setTextColor(Color.WHITE);
            phoneField.setHintTextColor(Color.WHITE);

            securityAnswerField.setTypeface(font);
            securityAnswerField.setTextColor(Color.WHITE);
            securityAnswerField.setHintTextColor(Color.WHITE);



            Title.setTypeface(font);
            Title.setTextColor(Color.WHITE);

            SecretQuestion.setTypeface(font);
            SecretQuestion.setTextColor(Color.WHITE);

            submitButton.setTypeface(font);
            submitButton.setTextColor(Color.WHITE);




        }


    public void SubmitButtonClicked(View v)
    {

        name = nameField.getText().toString();
        email = emailField.getText().toString();
        password = passwordField.getText().toString();
        phone = phoneField.getText().toString();
        securityAnswer = securityAnswerField.getText().toString();


        if (name.length() != 0 && isEmailValid(email) && password.length() >= 4 && phone.length() >= 3  && securityAnswer.length() > 1)
        {

            new CheckIfEmailIsAlreadyExists().execute("http://192.168.0.185/AndroidApps/GoDelivery/UsersDetails/EmailsPasswords/EmailsList.txt");
        }
        else if (name.length() <= 0)
        {
            Toast.makeText(SignUpActivity.this, "Name cannot be empty!" , Toast.LENGTH_SHORT).show();
        }
        else if (!isEmailValid(email))
        {
            Toast.makeText(SignUpActivity.this, "Enter valid Email Address" , Toast.LENGTH_SHORT).show();
        }

        else if (password.length() < 4)
        {
            Toast.makeText(SignUpActivity.this, "Password should be greater than or equal to four characters" , Toast.LENGTH_SHORT).show();
        }
        else if (phone.length() < 3)
        {
            Toast.makeText(SignUpActivity.this, "Enter Valid Phone number" , Toast.LENGTH_SHORT).show();
        }
        else if (securityAnswer.length() < 1)
        {
            Toast.makeText(SignUpActivity.this, "Security Answer cannot be empty or short" , Toast.LENGTH_SHORT).show();
        }



    }





    private class SignUpFormSubmission extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return SignUpForm(urls[0]);
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

            progressBar.setVisibility(View.GONE);


            if (result.equals("OK")) {


                SaveLoginEmail();

                Intent intent = new Intent(SignUpActivity.this, AlreadyLoggedInActivity.class);
                startActivity(intent);
                finish();


            }
            else
            {
                Toast.makeText(SignUpActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String SignUpForm(String myurl) throws IOException, UnsupportedEncodingException {

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
                    .appendQueryParameter("GoDeliveryEmail", email)
                    .appendQueryParameter("GoDeliveryPassword", password)
                    .appendQueryParameter("GoDeliveryPhone", phone)
                    .appendQueryParameter("GoDeliverySecurityAnswer", securityAnswer);

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






    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }





    private class CheckIfEmailIsAlreadyExists extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return CheckEmail(urls[0]);
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

            if (!result.equals("NetworkError")) {

                if (result.equals("isAvailable")) {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(SignUpActivity.this, "This email is already registered!", Toast.LENGTH_SHORT).show();
                } else {

                    new SignUpFormSubmission().execute("http://192.168.0.185/AndroidApps/GoDelivery/UsersDetails/Registration.php");
                }
            }

            else
            {
                Toast.makeText(SignUpActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String CheckEmail(String myurl) throws IOException, UnsupportedEncodingException {
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
            String registeredEmail = null;


            String emailStatus = "NotAvaiable";


            while ((readlineText = textReader.readLine()) != null) {
                registeredEmail = readlineText;


                if(registeredEmail.equals(email))
                {
                    emailStatus = "isAvailable";
                    break;
                }

            }



            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                return emailStatus;
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



    public void SaveLoginEmail()
    {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("GoDeliveryLoginEmail", email);
        editor.apply();

    }



    public void RefreshClicked(View v)
    {
        Intent intent = new Intent(SignUpActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();
    }



}
