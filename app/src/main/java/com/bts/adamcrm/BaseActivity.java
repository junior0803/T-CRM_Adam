package com.bts.adamcrm;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bts.adamcrm.util.SharedPreferencesManager;

public class BaseActivity extends AppCompatActivity {

    public SharedPreferencesManager sharedPreferencesManager;
    public static final int PICKER_REQUEST_CODE = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
    }

    public void exit(){
        finish();
    }

    public void showToast(String str){
        Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
    }
}
