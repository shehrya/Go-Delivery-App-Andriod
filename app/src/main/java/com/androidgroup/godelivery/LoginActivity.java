package com.androidgroup.godelivery;

import android.app.Activity;
import android.os.Bundle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends Activity {

    String loginEmail = null;
    String loginPassword = null;

    Button loginButton;
    Button signUpButton;
    Button forgotPasswordButton;

    TextView title;

    EditText emailField;
    EditText passwordField;

    ProgressBar progressBar;

    String []dataValues = new String[2];
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);



        dataValues[0] = "";
        dataValues[1] = "";

        counter = 0;

        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");

        loginButton = (Button) findViewById(R.id.loginButtonID);
        signUpButton = (Button) findViewById(R.id.SignUpButtonID);
        forgotPasswordButton = (Button) findViewById(R.id.ForgotPasswordButtonID);

        title = (TextView) findViewById(R.id.titleID);
        title.setText("Go Delivery");
        title.setTypeface(font);
        title.setTextColor(Color.WHITE);

        emailField = (EditText) findViewById(R.id.EmailFieldID);
        emailField.setTextColor(Color.WHITE);
        emailField.setHintTextColor(Color.WHITE);
        emailField.setTypeface(font);

        passwordField = (EditText) findViewById(R.id.PasswordFieldID);
        passwordField.setTextColor(Color.WHITE);
        passwordField.setHintTextColor(Color.WHITE);
        passwordField.setTypeface(font);

        progressBar = (ProgressBar) findViewById(R.id.LoginProgressBarID);

        loginButton.setTypeface(font);
        loginButton.setTextColor(Color.WHITE);

        signUpButton.setTypeface(font);
        signUpButton.setTextColor(Color.WHITE);

        forgotPasswordButton.setTypeface(font);
        forgotPasswordButton.setTextColor(Color.WHITE);


    }


    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void LoginButtonClicked (View v) {

        loginEmail = emailField.getText().toString();
        loginPassword = passwordField.getText().toString();

        if (isEmailValid(loginEmail) && loginPassword.length() >= 4)
        {
            new CheckForRegistration().execute("http://192.168.0.185/AndroidApps/GoDelivery/UsersDetails/EmailsPasswords/EmailsPasswordsList.txt");

        }
        else if (!isEmailValid(loginEmail))
        {
            Toast.makeText(LoginActivity.this , "Enter a valid email address", Toast.LENGTH_SHORT).show();
        }
        else if (loginPassword.length() == 0)
        {
            Toast.makeText(LoginActivity.this , "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if (loginPassword.length() > 0 && loginPassword.length() < 4)
        {
            Toast.makeText(LoginActivity.this , "Password is too short to be correct!", Toast.LENGTH_SHORT).show();
        }

    }


    public void SignUpButtonClicked (View v)
    {

        Intent intent = new Intent(LoginActivity.this , SignUpActivity.class);
        startActivity(intent);
        finish();



    }













    private class CheckForRegistration extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return CheckEmailPassword(urls[0]);
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


            if(!result.equals("NetworkError")) {

                if (result.equals("Matched")) {

                    SaveLoginEmail();

                    Intent intent = new Intent(LoginActivity.this, DeciderActivity.class);

                    startActivity(intent);

                    finish();


                } else if (result.equals("EmailMatched")) {
                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect Email & Password", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(LoginActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
            }




        }
    }

    private String CheckEmailPassword(String myurl) throws IOException, UnsupportedEncodingException {
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
            String email = null;
            String password = null;

            String userAuthentication = "UnMatched";

            while ((readlineText = textReader.readLine()) != null) {


                if (readlineText.length() > 0) {

                    for (int i = 0; i < readlineText.length(); ++i) {
                        if (readlineText.charAt(i) == '|') {
                            ++counter;

                            continue;
                        }

                        dataValues[counter] = (dataValues[counter] + readlineText.charAt(i));
                    }


                    email = dataValues[0];
                    password = dataValues[1];


                    if (email.equals(loginEmail) && password.equals(loginPassword)) {

                        userAuthentication = "Matched";

                        break;

                    } else if (email.equals(loginEmail)) {
                        userAuthentication = "EmailMatched";
                    } else {

                        userAuthentication = "UnMatched";

                    }


                    counter = 0;
                    dataValues[0] = "";
                    dataValues[1] = "";


                }


            }





            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return userAuthentication;
            } else {
                return "NetworkError";
            }


        }finally{


            if (is != null) {
                is.close();


            }

        }
    }


    public void SaveLoginEmail()
    {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("GoDeliveryLoginEmail", loginEmail);
        editor.apply();

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void ForgotPasswordButtonClicked(View v)
    {
        Intent intent = new Intent(LoginActivity.this , ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
    }


}
