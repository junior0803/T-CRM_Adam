package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.bts.adamcrm.adapter.NavAdapter;
import com.bts.adamcrm.model.Attachment2;
import com.bts.adamcrm.model.Category;
import com.bts.adamcrm.model.Customer;
import com.bts.adamcrm.model.Nav;
import com.bts.adamcrm.util.RecyclerItemClickListener;
import com.bts.adamcrm.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    final int FILE_SELECT_CODE = R.color.md_cyan_600;
    final int PICKER_REQUEST_CODE = 5000;

    @BindView(R.id.action_add)
    ImageView action_add;
    @BindView(R.id.action_search)
    ImageView action_search;

    List<Attachment2> attachment2List = new ArrayList();
    @BindView(R.id.btn_attach_main)
    Button btn_attach_main;
    TextView btn_end_date;
    @BindView(R.id.btn_reminder)
    TextView btn_reminder;
    @BindView(R.id.btn_reset_sms)
    Button btn_reset_sms;
    @BindView(R.id.btn_select_period)
    TextView btn_select_period;
    @BindView(R.id.btn_select_state)
    TextView btn_select_state;
    @BindView(R.id.btn_send_sms)
    Button btn_send_sms;
    TextView btn_start_date;
    @BindView(R.id.btn_sync)
    ImageView btn_sync;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.edt_search)
    EditText edt_search;

    String end_picked_date = "";
    private int mDay;
    @BindView(R.id.menu_recycler)
    RecyclerView menu_recycler;
    @BindView(R.id.search_wrapper)
    LinearLayout search_wrapper;

    int selectedState = 1;
    Category selected_category;
    SharedPreferencesManager sharedPreferencesManager;
    boolean showedNotification = false;
    boolean shownReminderDialog = false;
    @BindView(R.id.spn_category)
    Spinner spn_category;

    List<String> statusList = new ArrayList<>();
    String str_reminder = "";
    List<String> taskTypes = new ArrayList<>();
    Uri downloadUri;

    @BindView(R.id.tasks_recycler)
    RecyclerView tasks_recycler;
    @BindView(R.id.title_text)
    TextView title_text;
    @BindView(R.id.toggle_button)
    ImageView toggle_button;
    @BindView(R.id.txt_count)
    TextView txt_count;
    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.txt_online_count)
    TextView txt_online_count;

    NavAdapter navAdapter;
    List<Nav> navList = new ArrayList<>();
    ProgressDialog loadingProgress;
    ProgressDialog progressDialog;
    List<Customer> visibleList = new ArrayList();

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity.getBaseContext(), MainActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.sharedPreferencesManager = SharedPreferencesManager.getInstance(getBaseContext());

        Intent intent = new Intent();
        String pkgName = getPackageName();
        if (!((PowerManager)getSystemService(POWER_SERVICE)).isIgnoringBatteryOptimizations(pkgName)){
            intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
            intent.setData(Uri.parse("package:" + pkgName));
            startActivity(intent);
        }
        this.statusList  = new ArrayList<>();
        statusList.add("Show All");
        statusList.add("Show Active");
        statusList.add("Show Completed");
        statusList.add("Show Waiting");
        statusList.add("Show with Reminder");

        if (!sharedPreferencesManager.getStringValue("last_status").equals("")){
            int parseInt = Integer.parseInt(sharedPreferencesManager.getStringValue("last_status"));

        }
        // Ui component init
        this.toggle_button.setOnClickListener(this);
        this.txt_online_count.setOnClickListener(this);
        this.action_add.setOnClickListener(this);
        this.action_search.setOnClickListener(this);
        this.btn_attach_main.setOnClickListener(this);
        this.btn_send_sms.setOnClickListener(this);
        this.btn_reset_sms.setOnClickListener(this);
        this.btn_select_period.setOnClickListener(this);
        this.btn_reminder.setOnClickListener(this);
        this.btn_sync.setOnClickListener(this);
        this.title_text.setText("Home");

        generateMenu();
        this.menu_recycler.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        handleMenu(position);
                    }
                }));
    }

    public void generateMenu() {
        NavAdapter navAdapter = new NavAdapter(this.navList);
        this.navAdapter = navAdapter;
        this.menu_recycler.setAdapter(navAdapter);
        this.navList = new ArrayList<>();
        this.navList.add(new Nav("Home", R.drawable.ic_nav_home));
        this.navList.add(new Nav("Category List", R.drawable.ic_nav_add_category));
        this.navList.add(new Nav("Parts List 1", R.drawable.ic_stock_list));
        this.navList.add(new Nav("Shopping List 1", R.drawable.ic_stock_list));
        this.navList.add(new Nav("Parts List 2", R.drawable.ic_stock_list));
        this.navList.add(new Nav("Shopping List 2", R.drawable.ic_stock_list));
        this.navList.add(new Nav("Parts List 3", R.drawable.ic_stock_list));
        this.navList.add(new Nav("Shopping List 3", R.drawable.ic_stock_list));
        this.navList.add(new Nav("Settings", R.drawable.ic_action_setting));
        this.navList.add(new Nav("Logout", R.drawable.ic_action_exit));
        this.navAdapter.updateAdapter(this.navList);
    }

    private void handleMenu(int i) {
        this.drawerLayout.closeDrawer(Gravity.LEFT);
        if (i != 0) {
            if (i == 1) {
                CategoryActivity.launch(this);
            } else if (i == 2) {
                StockListActivity.launch(this, 1, 1);
            } else if (i == 3) {
                StockListActivity.launch(this, 1, 2);
            } else if (i == 4) {
                StockListActivity.launch(this, 2, 1);
            } else if (i == 5) {
                StockListActivity.launch(this, 2, 2);
            } else if (i == 6) {
                StockListActivity.launch(this, 3, 1);
            } else if (i == 7) {
                StockListActivity.launch(this, 3, 2);
            } else if (i == 8) {
                startActivity(new Intent(getBaseContext(), SettingsActivity.class));
            } else if (i == 9) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                exit();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toggle_button:
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.action_add:
                CustomerDetailActivity.launch(this);
                break;
            case R.id.btn_sync:
                Toast.makeText(this, "Sync Button Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_search:
                if (search_wrapper.getVisibility() == View.VISIBLE){
                    search_wrapper.setVisibility(View.GONE);
                } else {
                    search_wrapper.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_attach_main:
                showSelecterDialog(this);
                break;
            case R.id.btn_send_sms:
                showSendSMSDialog(this);
                break;
            case R.id.btn_select_state:
                showStateDialog(this);
                break;
            case R.id.btn_reset_sms:
                resetSMSDialog(this);
                break;
            case R.id.btn_select_period:
                showDateTimeDialog(this);
                break;
            case R.id.btn_reminder:
                showReminderDialog(this, Html.fromHtml(str_reminder).toString());
        }
    }

    public void showSendSMSDialog(Activity activity) {
        Toast.makeText(this, "showSendSMSDialog", Toast.LENGTH_SHORT).show();
    }

    public void showSelecterDialog(Activity activity) {
        Toast.makeText(this, "showSelecterDialog", Toast.LENGTH_SHORT).show();
    }

    public void showStateDialog(Activity activity){
        Toast.makeText(this, "showStateDialog", Toast.LENGTH_SHORT).show();
    }

    public void resetSMSDialog(Activity activity){
        Toast.makeText(this, "resetSMSDialog", Toast.LENGTH_SHORT).show();
    }

    public void showDateTimeDialog(Activity activity){
        Toast.makeText(this, "showDateTimeDialog", Toast.LENGTH_SHORT).show();
    }

    public void showReminderDialog(Activity activity, String reminder){
        Toast.makeText(this, "showReminderDialog", Toast.LENGTH_SHORT).show();
    }
}
