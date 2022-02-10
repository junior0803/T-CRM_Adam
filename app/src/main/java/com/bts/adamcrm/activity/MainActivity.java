package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bts.adamcrm.model.Attachment2;
import com.bts.adamcrm.model.Category;
import com.bts.adamcrm.model.Customer;
import com.bts.adamcrm.model.Nav;
import com.bts.adamcrm.util.RecyclerItemClickListener;
import com.bts.adamcrm.util.SharedPreferencesManager;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnStartDragListener {
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
    List<Customer> visibleList = new ArrayList();
    List<Customer> customerList = new ArrayList();
    CustomerAdapter customerAdapter;
    ItemTouchHelper mItemTouchHelper;

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity.getBaseContext(), MainActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loadingProgress = new ProgressDialog(this);
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
            selectedState = parseInt;
            btn_select_state.setText(statusList.get(selectedState).replace("Show", ""));
        }
        setupCurrentDate();
        loadUi();
        loadCategories();
        loadAllData();
        updateActiveDevices();
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

        mItemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(customerAdapter));
        mItemTouchHelper.attachToRecyclerView(tasks_recycler);

    }

    private void showCustomerData() {
        visibleList = new ArrayList<>();
        for (int i = 0; i < customerList.size(); i ++){
            if (!picked_date.equals("") && !end_picked_date.equals("")){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = simpleDateFormat.parse(customerList.get(i).getDate_updated());
                    Date pickDate = simpleDateFormat.parse(picked_date);
                    Date endPickDate = simpleDateFormat.parse(end_picked_date);
                    if (pickDate.getTime() <= date.getTime() && endPickDate.getTime() >= date.getTime()){
                        if (selected_category == null){
                            if (selectedState == 0){
                                visibleList.add(customerList.get(i));
                            } else if (customerList.get(i).getStatus() == selectedState){
                                visibleList.add(customerList.get(i));
                            }
                        }
                    } else if (selected_category.getTitle().equals(R.string.all_categories)){
                        if (selectedState == 0){
                            visibleList.add(customerList.get(i));
                        } else if (customerList.get(i).getStatus() == selectedState){
                            visibleList.add(customerList.get(i));
                        }
                    } else if (selected_category.getTitle().equals(customerList.get(i).getCategory().getTitle())){
                        if (selectedState == 0){
                            visibleList.add(customerList.get(i));
                        } else if (customerList.get(i).getStatus() == selectedState){
                            visibleList.add(customerList.get(i));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (customerList.get(i).getCategory() != null){
                if (selected_category.getTitle().equals(R.string.all_categories)){
                    if (selectedState == 4){
                        if (customerList.get(i).isReminder())
                            visibleList.add(customerList.get(i));
                    } else if (selectedState == 0){
                        visibleList.add(customerList.get(i));
                    } else if (customerList.get(i).getStatus() == selectedState){
                        visibleList.add(customerList.get(i));
                    }
                } else if (selected_category.getTitle().equals(customerList.get(i).getCategory().getTitle())){
                    if (selectedState == 4){
                        if (customerList.get(i).isReminder())
                            visibleList.add(customerList.get(i));
                    } else if (selectedState == 0 || customerList.get(i).getStatus() == selectedState)
                        visibleList.add(customerList.get(i));
                }
            }
            customerAdapter.updateAdapter(visibleList);
            txt_count.setText("Count : " + this.visibleList.size());
        }
    }

    private void setupCurrentDate() {
        txt_date.setText(new SimpleDateFormat("dd/MM/yyyy",
                Locale.getDefault()).format(Calendar.getInstance().getTime()));
    }

    private void loadUi(){
        progressDialog = new ProgressDialog(this, R.style.RedAppCompatAlertDialogStyle);
        progressDialog.setTitle(R.string.load_data);
        customerAdapter = new CustomerAdapter(visibleList);
        tasks_recycler.setAdapter(customerAdapter);
        tasks_recycler.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CustomerDetailActivity.launch(getBaseContext(), visibleList.get(position));
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
        // more implement
    }

    private void loadAllData(){
        progressDialog.show();
        // more implement
    }

    private void updateActiveDevices(){
        // more implement
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
                loadingProgress.setTitle(R.string.sync_data);
                loadingProgress.show();
                visibleList = customerAdapter.getCustomerList();
                for (int i = 0; i < visibleList.size(); i ++){
                    visibleList.get(i).setSort(i);
                }
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
            if (!customer.isSms_sent()){
                if (status == 5){
                    break;
                }
                if (status > 0){
                    sendMsg.append(manufactureName);
                }
                customer.setSms_sent(true);
                sendMsg.append(customer.getMobile());
                status ++;
            }
        }
        if (sendMsg.equals("")){
            showToast("You have already sent message to all this category members");
            return;
        }
        try {
            Log.i("Dimo", String.valueOf(sendMsg));
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + sendMsg));
            intent.putExtra("sms_body", toString);
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
            showToast("No sms app found");
        }
        dialog.dismiss();
    }

    private void sendSMSattachfile(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_attachments);
        RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
        Attachment2Adapter attachment2Adapter = new Attachment2Adapter(attachment2List);
        recyclerView.setAdapter(attachment2Adapter);;
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                listAttachFileSelect(dialog, attachment2Adapter,view, position);
            }
        }));
        dialog.show();
    }

    private void listAttachFileSelect(Dialog dialog, Attachment2Adapter attachment2Adapter, View view, int position) {
        view.findViewById(R.id.btn_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(attachment2List.get(position).getUrl()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.txt_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadUri = Uri.parse(attachment2List.get(position).getUrl());
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
                ((TextView) dialog1.findViewById(R.id.txt)).setText("Are you sure you want to delete attachment " + attachment2List.get(position).getName() + " ?");
                dialog1.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attachment2List.remove(position);
                        attachment2Adapter.updateAdapter(attachment2List);
                        attachment2List.size();
                        dialog.dismiss();
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

    public void showSelecterDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_selector);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.select_attach_file);
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // more implement
            }
        });
        dialog.findViewById(R.id.btn_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // more implement
            }
        });
        dialog.show();
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
        SharedPreferencesManager sharedPreferencesManager = this.sharedPreferencesManager;
        sharedPreferencesManager.setStringValue("last_status", position + "");
        loadAllData();
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
            customer.setSms_sent(false);
            // more implement
        }
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
                btn_start_date.setText(picked_date);
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
        dialog.dismiss();
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
                picked_date = mDay + "/" + mMonth + "/" + mYear;
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
                end_picked_date = mDay + "/" + mMonth + "/" + mYear;
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
                // more implement
            }
        });
        dialog.show();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
