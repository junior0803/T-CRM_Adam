package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCategoryActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_add)
    Button btn_add;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    String category_str = "";
    @BindView(R.id.edt_title)
    EditText edt_title;
    ProgressDialog progressDialog;
    @BindView(R.id.title_text)
    TextView title_text;

    public static void launch(Activity activity){
        activity.startActivity(new Intent(activity.getBaseContext(), AddCategoryActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        ButterKnife.bind(this);

        title_text.setText(R.string.new_category);
        btn_add.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                exit();
                break;
            case R.id.btn_add:
                if (edt_title.getText().toString().equals("")){
                    showToast("Please enter a name!");
                }
                // Add more
        }
    }
}
