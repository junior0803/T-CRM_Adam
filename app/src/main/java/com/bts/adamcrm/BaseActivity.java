package com.bts.adamcrm;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bts.adamcrm.services.ApiRepository;
import com.bts.adamcrm.util.SharedPreferencesManager;


public class BaseActivity extends AppCompatActivity {

    public SharedPreferencesManager sharedPreferencesManager;
    public static final int PICKER_REQUEST_CODE = 5000;
    public static final int FILE_SELECT_CODE = 1111;
    public static final int CAMERA_REQUEST = 1888;
    public static final int INVOICE_REQUEST_CODE = 9000;
    public static final boolean online = true;
    public static final String BASIC_URL = online ? "http://178.255.225.133/" : "http://10.10.11.153/";
    public static final String ATTACH_FILE_URI = BASIC_URL + "uploads/";
    public static final String LOGO_FILE_URI = ATTACH_FILE_URI + "invoice/";
    public static final String HOME_ATTACH_FILE_URI = ATTACH_FILE_URI + "attach/";
    public static final String INVOICE_PDF_URI = BASIC_URL + "pdfInvoiceExport/";

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
