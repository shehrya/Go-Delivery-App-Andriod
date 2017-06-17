package com.androidgroup.godelivery;

import android.app.Activity;
import android.os.Bundle;

public class PreJobPhotoWorkerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_photo_worker);


    }

    public void RefreshClicked4(View v)
    {
        Intent intent = new Intent(PreJobPhotoWorkerActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();




    }

}
