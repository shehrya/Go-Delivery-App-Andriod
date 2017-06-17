package com.androidgroup.godelivery;

import android.app.Activity;
import android.os.Bundle;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;


public class UserTypeActivity extends Activity {

    Button submitJob;
    Button carryJob;
    Button logOut;
    Button profile;

    String loginEmailString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usertype);

        LoadLoginEmail();

        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");



        submitJob = (Button) findViewById(R.id.UserTypeSubmitJobButtonID);
        carryJob = (Button) findViewById(R.id.UserTypeCarryJobButtonID);
        logOut = (Button) findViewById(R.id.LogoutButtonID);
        profile = (Button) findViewById(R.id.UserTypeProfileButtonID);






        submitJob.setTypeface(font);
        submitJob.setTextColor(Color.WHITE);

        carryJob.setTypeface(font);
        carryJob.setTextColor(Color.WHITE);

        logOut.setTypeface(font);
        logOut.setTextColor(Color.WHITE);

        profile.setTypeface(font);
        profile.setTextColor(Color.WHITE);



    }

    public void LogOutClicked(View v)
    {
        LogoutUser();

        Intent intent = new Intent(UserTypeActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();

    }

    public void ClientButtonClicked(View v)
    {

        Intent intent = new Intent(UserTypeActivity.this, EmployerActivity.class);

        startActivity(intent);

        finish();
    }


    public void DeliveryButtonClicked(View v)
    {

        Intent intent = new Intent(UserTypeActivity.this, WorkerActivity.class);

        startActivity(intent);

        finish();

    }

    public void ProfileButtonClicked(View v)
    {

        Intent intent = new Intent(UserTypeActivity.this, ProfileActivity.class);

        startActivity(intent);

        finish();

    }



    public void LoadLoginEmail()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loginEmailString = prefs.getString("GoDeliveryLoginEmail", null);


    }

    public void LogoutUser()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("GoDeliveryLoginEmail", null);
        editor.apply();
    }


}
