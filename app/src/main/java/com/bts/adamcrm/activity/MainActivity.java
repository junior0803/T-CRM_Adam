package com.bts.adamcrm.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.bts.adamcrm.adapter.Attachment2Adapter;
import com.bts.adamcrm.adapter.CustomerAdapter;
import com.bts.adamcrm.adapter.NavAdapter;
import com.bts.adamcrm.adapter.StatusAdapter;
import com.bts.adamcrm.helper.OnStartDragListener;
import com.bts.adamcrm.helper.SimpleItemTouchHelperCallback;
import com.bts.adamcrm.model.Attachment;
import com.bts.adamcrm.model.Category;
import com.bts.adamcrm.model.Customer;
import com.bts.adamcrm.model.Nav;
import com.bts.adamcrm.receiver.AlarmReceiver;
import com.bts.adamcrm.util.FileUtils;
import com.bts.adamcrm.util.ImageFileFilter;
import com.bts.adamcrm.util.ImageUtils;
import com.bts.adamcrm.util.RecyclerItemClickListener;
import com.bts.adamcrm.util.SharedPreferencesManager;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnStartDragListener {

    @BindView(R.id.action_add)
    ImageView action_add;
    @BindView(R.id.action_search)
    ImageView action_search;
    List<Attachment> attachmentList = new ArrayList<>();
    TextView btn_end_date;
    @BindView(R.id.btn_reminder)
    TextView btn_reminder;
    @BindView(R.id.btn_reset_sms)
    Button btn_reset_sms;
    @BindView(R.id.btn_attach_main)
    Button btn_attach_main;
    @BindView(R.id.btn_select_period)
    TextView btn_select_period;
    @BindView(R.id.btn_select_state)
    TextView btn_select_state;
    @BindView(R.id.btn_send_sms)
    Button btn_send_sms;
    TextView btn_start_date;
    @BindView(R.id.btn_sync)
    ImageView btn_sync;
    private Calendar calendar;
    private int mMonth;
    private int mYear;
    private int mDay;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.edt_search)
    EditText edt_search;
    String picked_date = "";
    String end_picked_date = "";
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
    String currentPhotoPath;

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            showFileChooser();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(getBaseContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    PermissionListener permissionListener2;

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
    List<Category> categoryList = new ArrayList<>();
    NavAdapter navAdapter;
    List<Nav> navList = new ArrayList<>();
    ProgressDialog loadingProgress;
    ProgressDialog progressDialog;
    List<Customer> visibleList = new ArrayList<>();
    List<Customer> customerList = new ArrayList<>();
    CustomerAdapter customerAdapter;
    ItemTouchHelper mItemTouchHelper;

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity.getBaseContext(), MainActivity.class));
    }

    @Override
    public void onResume(){
        super.onResume();
        if (sharedPreferencesManager.getBooleanValue("update")){
            loadCategories();
            loadAllData();
            loadAllAttachments();
            sharedPreferencesManager.setBooleanValue("update", false);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loadingProgress = new ProgressDialog(this);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getBaseContext());
        sharedPreferencesManager.setBooleanValue("update", true);

        Intent intent = new Intent();
        String pkgName = getPackageName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!((PowerManager)getSystemService(POWER_SERVICE)).isIgnoringBatteryOptimizations(pkgName)){
                intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                intent.setData(Uri.parse("package:" + pkgName));
                startActivity(intent);
            }
        }
        statusList  = new ArrayList<>();
        statusList.add("Show All");
        statusList.add("Show Active");
        statusList.add("Show Completed");
        statusList.add("Show Waiting");
        statusList.add("Show with Reminder");

        if (!sharedPreferencesManager.getStringValue("last_status").equals("")){
            selectedState = Integer.parseInt(sharedPreferencesManager.getStringValue("last_status"));
            btn_select_state.setText(statusList.get(selectedState).replace("Show", ""));
        }
        setupCurrentDate();
        loadUi();

        //first boot
        updateActiveDevices();
        // Ui component init
        toggle_button.setOnClickListener(this);
        txt_online_count.setOnClickListener(this);
        action_add.setOnClickListener(this);
        action_search.setOnClickListener(this);
        btn_attach_main.setOnClickListener(this);
        btn_send_sms.setOnClickListener(this);
        btn_reset_sms.setOnClickListener(this);
        btn_select_period.setOnClickListener(this);
        btn_reminder.setOnClickListener(this);
        btn_sync.setOnClickListener(this);
        title_text.setText("Home");

        generateMenu();
        menu_recycler.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        handleMenu(position);
                    }
                }));
        spn_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_category = categoryList.get(i);
                showCustomerData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Permission more implement

        permissionListener2 = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                showToast("Permission Granted!");
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                showToast("Permission Denied");
            }
        };
        
        TedPermission.create()
                .setPermissionListener(permissionListener2)
                .setDeniedMessage(R.string.permission_check_message)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
        
        mItemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(customerAdapter));
        mItemTouchHelper.attachToRecyclerView(tasks_recycler);

    }

    private void showCustomerData() {
        visibleList = new ArrayList<>();
        //visibleList = customerList;
        String startTime = " 00:00:00";
        String endTime = " 23:59:59";
        for (int i = 0; i < customerList.size(); i ++){
            if (!picked_date.equals("") || !end_picked_date.equals("")){
                SimpleDateFormat datePickerFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = new Date();
                    if (customerList.get(i).getDate_updated() != null)
                        date = dateFormat.parse(customerList.get(i).getDate_updated());
                    else
                        date = dateFormat.parse(customerList.get(i).getDate_created());

                    Date StartPickDate = datePickerFormat.parse(picked_date + startTime);
                    Date endPickDate = datePickerFormat.parse(end_picked_date + endTime);
                    Log("date : " + date + " pickDate : " + StartPickDate + " endDate" + endPickDate);
                    if (StartPickDate != null && endPickDate != null && date != null) {
                        if (StartPickDate.getTime() <= date.getTime() && endPickDate.getTime() >= date.getTime()) {
                            if (selected_category == null
                                    || selected_category.getName().equals("All Categories")
                                    || selected_category.getId() == customerList.get(i).getCategory_id()) {
                                if (selectedState == 4 && customerList.get(i).getReminder_date() != null) {
                                    visibleList.add(customerList.get(i));
                                } else if (selectedState == 0 || customerList.get(i).getState() == selectedState) {
                                    visibleList.add(customerList.get(i));
                                }
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Log("showCustomerData case 1 selected_category : " + selected_category);
                if (selected_category == null
                        || selected_category.getName().equals("All Categories")
                        || selected_category.getId() == customerList.get(i).getCategory_id()){
                    Log("All Category");
                    if (selectedState == 4 && customerList.get(i).getReminder_date() != null){
                        visibleList.add(customerList.get(i));
                    } else if (selectedState == 0 || customerList.get(i).getState() == selectedState) {
                        visibleList.add(customerList.get(i));
                    }
                }
            }
        }
        customerAdapter.updateAdapter(visibleList);
        txt_count.setText("Count : " + visibleList.size());
    }

    private void setupCurrentDate() {
        txt_date.setText(new SimpleDateFormat("MM/dd/yyyy",
                Locale.getDefault()).format(Calendar.getInstance().getTime()));
    }

    private void loadUi(){
        progressDialog = new ProgressDialog(this, R.style.RedAppCompatAlertDialogStyle);
        progressDialog.setTitle(R.string.load_data);
        customerAdapter = new CustomerAdapter(visibleList);
        tasks_recycler.setAdapter(customerAdapter);
        tasks_recycler.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CustomerDetailActivity.launch(MainActivity.this, visibleList.get(position));
            }
        }));
        btn_select_state.setOnClickListener(this);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customerAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void loadCategories(){
        apiRepository.getApiService().categoryList().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                Log(response.raw().toString());
                if (response.body() != null) {
                    categoryList = new ArrayList<>();
                    Category category = new Category(getString(R.string.all_categories));
                    categoryList.add(category);
                    selected_category = category;
                    Log("size : "+ response.body().size());
                    categoryList.addAll(response.body());
                    setupCategorySpinner(categoryList);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    public void setupCategorySpinner(List<Category> list) {
        taskTypes = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            taskTypes.add(list.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, this.taskTypes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_category.setAdapter(arrayAdapter);
    }

    private void loadAllAttachments(){
        apiRepository.getApiService().get_attach_files().enqueue(new Callback<List<Attachment>>(){
            @Override
            public void onResponse(Call<List<Attachment>> call, Response<List<Attachment>> response) {
                if (response.isSuccessful() && response.body() != null){
                    Log("response : " + response.body().size());
                    attachmentList = new ArrayList<>(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Attachment>> call, Throwable t) {
            }
        });
    }

    private void loadAllData(){
        progressDialog.show();
        apiRepository.getApiService().getAllCustomerList().enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                str_reminder = "";
                customerList = new ArrayList<>();
                if (response.isSuccessful() && response.body() != null){
                    Log("customList : " + response.body() + " size : " + response.body().size());
                    for (Customer customer : response.body()){
                        customerList.add(customer);
                        if (customer.getReminder_date() != null) {
                            try {
                                if (new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(customer.getReminder_date()).getTime() <= new Date().getTime()) {
                                    str_reminder = str_reminder + customer.getTitle() + " <br>";
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (str_reminder.equals("") || shownReminderDialog){
                    btn_reminder.setVisibility(View.GONE);
                } else {
                    if (!showedNotification){
                        showReminderDialog(MainActivity.this, Html.fromHtml(str_reminder).toString());
                    }
                    btn_reminder.setVisibility(View.VISIBLE);
                }
                showCustomerData();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    private void updateActiveDevices(){
    }

    public void generateMenu() {
        navAdapter = new NavAdapter(navList);
        menu_recycler.setAdapter(navAdapter);
        navList = new ArrayList<>();
        navList.add(new Nav("Home", R.drawable.ic_nav_home));
        navList.add(new Nav("Category List", R.drawable.ic_nav_add_category));
        navList.add(new Nav("Parts List 1", R.drawable.ic_stock_list));
        navList.add(new Nav("Shopping List 1", R.drawable.ic_stock_list));
        navList.add(new Nav("Parts List 2", R.drawable.ic_stock_list));
        navList.add(new Nav("Shopping List 2", R.drawable.ic_stock_list));
        navList.add(new Nav("Parts List 3", R.drawable.ic_stock_list));
        navList.add(new Nav("Shopping List 3", R.drawable.ic_stock_list));
        navList.add(new Nav("Settings", R.drawable.ic_action_setting));
        navList.add(new Nav("Logout", R.drawable.ic_action_exit));
        navAdapter.updateAdapter(navList);
    }

    private void handleMenu(int i) {
        drawerLayout.closeDrawer(Gravity.LEFT);
        if (i != 0) {
            if (i == 1) {
                CategoryActivity.launch(this);
            } else if (i == 2) {
                StockListActivity.launch(this, 0, 1);
            } else if (i == 3) {
                StockListActivity.launch(this, 1, 1);
            } else if (i == 4) {
                StockListActivity.launch(this, 0, 2);
            } else if (i == 5) {
                StockListActivity.launch(this, 1, 2);
            } else if (i == 6) {
                StockListActivity.launch(this, 0, 3);
            } else if (i == 7) {
                StockListActivity.launch(this, 1, 3);
            } else if (i == 8) {
                startActivity(new Intent(getBaseContext(), SettingsActivity.class));
            } else if (i == 9) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                logout();
                exit();
            }
        }
    }

    private void logout(){
        sharedPreferencesManager.setStringValue("edgewatch", "~xP`MR9Ha)XPU4");

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
                loadCategories();
                loadAllData();
                loadAllAttachments();
                break;
            case R.id.action_search:
                if (search_wrapper.getVisibility() == View.VISIBLE){
                    search_wrapper.setVisibility(View.GONE);
                } else {
                    search_wrapper.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_attach_main:
                showSelectorDialog(this);
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
                break;
            case R.id.txt_online_count:
                updateActiveDevices();
        }
    }

    public void showSendSMSDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_send_sms);
        EditText editText = dialog.findViewById(R.id.edt_message);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.send_sms);
        dialog.findViewById(R.id.btn_attach).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMSattachfile(activity);
            }
        });
        dialog.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.append("\n\n");
                Uri uri = downloadUri;
                if (uri != null)
                    editText.append(uri.toString());
                sendSMS(editText.getText().toString(), dialog);
            }
        });
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void sendSMS(String toString, Dialog dialog) {
        String manufactureName = Build.MANUFACTURER.equalsIgnoreCase("Samsung")? "," : ";";
        StringBuilder sendMsg = new StringBuilder();
        int status = 0;
        for (int i = 0; i < visibleList.size(); i ++){
            Customer customer = visibleList.get(i);
            if (customer.isSms_sent() == 0){
                if (status == 5){
                    break;
                }
                if (status > 0){
                    sendMsg.append(manufactureName);
                }
                customer.setSms_sent(1);
                sendMsg.append(customer.getMobile_phone());
                status ++;
                apiRepository.getApiService().updateCustomer(customer.getId(), customer.getTitle(), customer.getMobile_phone(), customer.getEmail(),
                        customer.getName(), customer.getAddress(), customer.getTown(), customer.getPostal_code(), customer.getFurther_note(),
                        customer.getState(), customer.getReminder_date(), customer.getCategory_id(), customer.getSms_sent(),
                        customer.getAttached_files()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToast("Please Activate internet connection!");
                    }
                });
            }
        }
        if (sendMsg.equals("")){
            showToast("You have already sent message to all this category members");
            return;
        }
        try {
            Log.i("junior", String.valueOf(sendMsg));
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + sendMsg));
            intent.putExtra("sms_body", toString);
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
            showToast("No sms app found");
        }
        showCustomerData();
        dialog.dismiss();
    }

    private void sendSMSattachfile(Activity activity) {
        if (attachmentList.size() > 0){
            Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(1);
            dialog.setContentView(R.layout.dialog_attachments);
            RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
            Attachment2Adapter attachment2Adapter = new Attachment2Adapter(attachmentList);
            recyclerView.setAdapter(attachment2Adapter);;
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    listAttachFileSelect(dialog, attachment2Adapter,view, position);
                }
            }));
            dialog.show();
        } else {
            showToast("No Attachment Files!!");
        }
    }

    private void listAttachFileSelect(Dialog dialog, Attachment2Adapter attachment2Adapter, View view, int position) {
        view.findViewById(R.id.btn_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(HOME_ATTACH_FILE_URI + attachmentList.get(position).getFile_path()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.txt_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadUri = Uri.parse(HOME_ATTACH_FILE_URI + attachmentList.get(position).getFile_path());
                showToast("Attachment selected!");
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.icon_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog1 = new Dialog(view.getContext());
                dialog1.requestWindowFeature(1);
                dialog1.setContentView(R.layout.dialog_delete_item);
                ((TextView) dialog1.findViewById(R.id.dialog_title)).setText(R.string.delete_item);
                ((TextView) dialog1.findViewById(R.id.txt)).setText("Are you sure you want to delete attachment " + attachmentList.get(position).getFile_path() + " ?");
                dialog1.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        apiRepository.getApiService().deleteAttachFile(attachmentList.get(position).getFile_path()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String > call, Response<String> response) {
                                if (response.isSuccessful()
                                        && response.body() != null
                                        && response.body().equals("success")){
                                    attachmentList.remove(position);
                                    attachment2Adapter.updateAdapter(attachmentList);
                                    attachmentList.size();
                                    dialog1.dismiss();
                                    if (attachmentList.size() == 0){
                                        dialog.dismiss();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                showToast("Please Activate internet connection!");
                            }
                        });

                    }
                });
                dialog1.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
            }
        });
    }

    public void showSelectorDialog(Activity activity) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage(R.string.permission_check_message)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
        // file select dialog begin
//        Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(1);
//        dialog.setContentView(R.layout.dialog_selector);
//        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.select_attach_file);
//        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialog.findViewById(R.id.btn_file).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TedPermission.create()
//                        .setPermissionListener(permissionListener)
//                        .setDeniedMessage(R.string.permission_check_message)
//                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .check();
//                dialog.dismiss();
//            }
//        });
//        dialog.findViewById(R.id.btn_img).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
//                } else {
//                    dispatchTakePictureIntent();
//                }
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
        // file select dialog end
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 100) {
            return;
        }
        if (grantResults[0] == 0) {
            Toast.makeText(this, "camera permission granted", Toast.LENGTH_SHORT).show();
            dispatchTakePictureIntent();
            return;
        }
        Toast.makeText(this, "camera permission denied", Toast.LENGTH_SHORT).show();
    }

    public File createImageFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile =  timeStamp + "_attach";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoFile = createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.bts.adamcrm.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    public void showStateDialog(Activity activity){
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_state);
        RecyclerView recyclerView = dialog.findViewById(R.id.recycler_category);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.select_status);
        recyclerView.setAdapter(new StatusAdapter(statusList));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setStateItem(dialog, position);
            }
        }));
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setStateItem(Dialog dialog, int position) {
        selectedState = position;
        btn_select_state.setText(statusList.get(position).replace("Show", ""));
        sharedPreferencesManager.setStringValue("last_status", position + "");
        showCustomerData();
        dialog.dismiss();
    }

    public void resetSMSDialog(Activity activity){
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_delete_item);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.reset_sms);
        ((TextView) dialog.findViewById(R.id.txt)).setText(R.string.reset_sms_request);
        dialog.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetList();
                showToast("Sms Status Reset!");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void resetList() {
        for (int i = 0; i < visibleList.size(); i ++){
            Customer customer = visibleList.get(i);
            customer.setSms_sent(0);
            apiRepository.getApiService().updateCustomer(customer.getId(), customer.getTitle(), customer.getMobile_phone(), customer.getEmail(),
                    customer.getName(), customer.getAddress(), customer.getTown(), customer.getPostal_code(), customer.getFurther_note(),
                    customer.getState(), customer.getReminder_date(), customer.getCategory_id(), customer.getSms_sent(),
                    customer.getAttached_files()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showToast("Please Activate internet connection!");
                }
            });
        }
        showCustomerData();
    }

    public void showDateTimeDialog(Activity activity){
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_date_time);
        TextView txt_title = dialog.findViewById(R.id.dialog_title);
        TextView btn_show = dialog.findViewById(R.id.btn_show);
        TextView btn_reset = dialog.findViewById(R.id.btn_reset);
        btn_end_date = dialog.findViewById(R.id.btn_end_date);
        btn_start_date = dialog.findViewById(R.id.btn_start_date);
        if (!end_picked_date.equals(""))
            btn_end_date.setText(end_picked_date);

        if (!picked_date.equals(""))
            btn_start_date.setText(picked_date);

        txt_title.setText(R.string.select_date);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end_picked_date = "";
                picked_date = "";
                btn_end_date.setText("");
                btn_start_date.setText("");
                showCustomerData();
                btn_select_period.setText(R.string.select_period);
                dialog.dismiss();
            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (end_picked_date.equals("") || picked_date.equals("")){
                    end_picked_date = "";
                    picked_date = "";
                    btn_end_date.setText("");
                    btn_start_date.setText("");
                } else {
                    btn_select_period.setText(R.string.reset_period);
                    showCustomerData();
                }
                dialog.dismiss();
            }
        });

        btn_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartDateTimeDialog();
            }
        });
        btn_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndDateTimeDialog();
            }
        });
        dialog.show();
    }



    private void showStartDateTimeDialog() {
        calendar = Calendar.getInstance();
        mYear = calendar.get(1);
        mMonth = calendar.get(2);
        mDay = calendar.get(5);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mMonth = i1 + 1;
                mYear = i;
                mDay = i2;
                picked_date = mMonth + "/" + mDay + "/" + mYear;
                btn_start_date.setText(picked_date);
            }
        }, mYear, mMonth, mDay).show();
    }

    private void showEndDateTimeDialog() {
        calendar = Calendar.getInstance();
        mYear = calendar.get(1);
        mMonth = calendar.get(2);
        mDay = calendar.get(5);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mYear = i;
                mMonth = i1 + 1;
                mDay = i2;
                end_picked_date = mMonth + "/" + mDay + "/" + mYear;
                btn_end_date.setText(end_picked_date);
            }
        }, mYear, mMonth, mDay).show();
    }

    public void showReminderDialog(Activity activity, String reminder){
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_reminder);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.reminder);
        ((TextView) dialog.findViewById(R.id.txt_msg)).setText(reminder);
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showedNotification = true;
            }
        });
        dialog.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showedNotification = true;
            }
        });
        dialog.show();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == FILE_SELECT_CODE) {
                String FilePath = FileUtils.getPath(this, data.getData());
                File file = new File(Objects.requireNonNull(FilePath));
                if (new ImageFileFilter().accept(file) && FileUtils.decideToCompress(FilePath)) {
                    Uri imageUri = ImageUtils.getInstant().getCompressedBitmap(this, FilePath);
                    String tmpFilePath = FileUtils.getRealPathFromURI(this, imageUri);
                    file = new File(tmpFilePath);
                }
                uploadFile(file);
            } else if (requestCode == PICKER_REQUEST_CODE){
                String[] strArr = data.getExtras().getStringArray("images");
                if (strArr.length > 0 && strArr[0] != null){
                    uploadFile(new File(strArr[0]));
                }
            } else if (requestCode == CAMERA_REQUEST){
                File file = new File(currentPhotoPath);
                if (new ImageFileFilter().accept(file) && FileUtils.decideToCompress(currentPhotoPath)){
                    Uri imageUri = ImageUtils.getInstant().getCompressedBitmap(this, currentPhotoPath);
                    String tmpFilePath = FileUtils.getRealPathFromURI(this, imageUri);
                    file = new File(tmpFilePath);
                }
                uploadFile(file);
            }
        }
    }

    private void uploadFile(File file) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        RequestBody requestFile = RequestBody.create(
                MediaType.parse("*/*"),
                file
        );
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        apiRepository.getApiService().uploadAttach(body).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Log("response : " + response.body());
                    String uploadFile = response.body();
                    showToast("upload File :" + uploadFile);
                    loadAllAttachments();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("Please Activate internet connection!");
                progressDialog.show();
            }
        });
    }
}
