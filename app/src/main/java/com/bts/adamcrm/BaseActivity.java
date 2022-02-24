package com.bts.adamcrm;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bts.adamcrm.services.ApiRepository;
import com.bts.adamcrm.util.SharedPreferencesManager;

public class BaseActivity extends AppCompatActivity {

    public SharedPreferencesManager sharedPreferencesManager;
    public static final int PICKER_REQUEST_CODE = 5000;
    public static final int FILE_SELECT_CODE = 1111;
    public static final int INVOICE_REQUEST_CODE = 9000;

    public static ApiRepository apiRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        apiRepository = apiRepository.getInstance();
    }

    public void exit(){
        finish();
    }

    public void showToast(String str){
        Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
    }

    public void Log(String string){
        Log.e("junior", string);
    }
}
