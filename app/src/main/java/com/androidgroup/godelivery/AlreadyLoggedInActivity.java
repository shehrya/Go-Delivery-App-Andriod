package com.androidgroup.godelivery;

import android.app.Activity;
import android.os.Bundle;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
public class AlreadyLoggedInActivity extends Activity {

    String loginEmailString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.already_logged_in);



        LoadLoginEmail();

        if(loginEmailString != null)


        {



            Intent intent = new Intent(AlreadyLoggedInActivity.this, DeciderActivity.class);

            startActivity(intent);

            finish();

        }

        else
        {



            Intent intent = new Intent(AlreadyLoggedInActivity.this, LoginActivity.class);

            startActivity(intent);

            finish();



        }



    }
    public void LoadLoginEmail()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loginEmailString = prefs.getString("GoDeliveryLoginEmail", null);


    



}



}
