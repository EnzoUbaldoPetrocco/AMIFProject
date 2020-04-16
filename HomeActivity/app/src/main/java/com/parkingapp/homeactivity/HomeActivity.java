package com.parkingapp.homeactivity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.parkingapp.homeactivity.R;

public class HomeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
    }
}
