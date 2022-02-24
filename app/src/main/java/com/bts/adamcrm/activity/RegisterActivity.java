package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.bts.adamcrm.model.User;
import com.bts.adamcrm.services.ApiRepository;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "LoginActivity";
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_username)
    EditText edt_username;
    @BindView(R.id.edt_confirm_password)
    EditText confirm_password;

    ProgressDialog progressDialog;

    public static void launch(Activity activity){
        activity.startActivity(new Intent(activity.getBaseContext(), RegisterActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateUI(){
        if (progressDialog != null && progressDialog.isShowing()){
            this.progressDialog.dismiss();
        }
        showToast(getString(R.string.success_add));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this, R.style.RedAppCompatAlertDialogStyle);
        this.btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            if (edt_username.getText().toString().equals("")
                    || edt_password.getText().toString().equals("")
                    || confirm_password.getText().toString().equals("")) {
                showToast(getString(R.string.invalid_input_userinfo));
                Log.e("junior", getString(R.string.invalid_input_userinfo));
            } else {
                progressDialog.setTitle(R.string.registering);
                progressDialog.show();
                // Login
                register();
            }
        }
    }

    private void register() {
        Log.e(TAG, "createUserWithEmail:success");

        apiRepository.getApiService().signUp(edt_username.getText().toString(),
                edt_password.getText().toString(), confirm_password.getText().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("junior", "onResponse :" + new Gson().toJson(response.body()));
                updateUI();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("junior", "onFailure massage : " + t.getMessage());
                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }
}
