package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    String TAG = "LoginActivity";
    @BindView(R.id.btn_login)
    Button btn_login;
    int counter = 0;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_username)
    EditText edt_username;
    @BindView(R.id.name)
    TextView name;
    ProgressDialog progressDialog;



    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity.getBaseContext(), LoginActivity.class));
    }


    private void launchMain() {
        MainActivity.launch(this);
        exit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.progressDialog = new ProgressDialog(this, R.style.RedAppCompatAlertDialogStyle);
        this.btn_login.setOnClickListener(this);
        this.name.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_login:
                if (edt_password.getText().toString().equals("")
                        || edt_username.getText().toString().equals(""))
                    showToast(getString(R.string.invalid_input_userinfo));
                else {
                    this.progressDialog.setTitle(R.string.logging);
                    this.progressDialog.show();
                    Log.e("junior", "LoginActivity - btn_login");
                    login();
                }
                break;
            case R.id.name:
                int i = counter;
                if (i < 5){
                    counter = i + 1;
                    return;
                }
                counter = 0;
                RegisterActivity.launch(this);
            default:

        }
    }

    private void login() {
        progressDialog.show();
        Log.d(TAG, "signInWithEmail:success");
        apiRepository.getApiService().logIn(edt_username.getText().toString(), edt_password.getText().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("junior", "onResponse :" + response.body());
                if (response.isSuccessful()){
                    launchMain();
                } else {
                    showToast("Invalid User and Password");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("junior", "onFailure : " + t.getMessage());
                progressDialog.dismiss();
            }
        });
    }
}
