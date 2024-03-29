package com.bts.adamcrm.activity;

import static com.bts.adamcrm.MyApplication.mConnManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.bts.adamcrm.adapter.Attachment3Adapter;
import com.bts.adamcrm.adapter.AttachmentAdapter;
import com.bts.adamcrm.adapter.CategoryAdapter2;
import com.bts.adamcrm.adapter.InvoiceAdapter;
import com.bts.adamcrm.adapter.InvoiceAdapter2;
import com.bts.adamcrm.database.CategoryQueryImplementation;
import com.bts.adamcrm.database.InvoiceQueryImplementation;
import com.bts.adamcrm.database.QueryContract;
import com.bts.adamcrm.database.QueryResponse;
import com.bts.adamcrm.model.Category;
import com.bts.adamcrm.model.Customer;
import com.bts.adamcrm.model.Invoice;
import com.bts.adamcrm.receiver.AlarmReceiver;
import com.bts.adamcrm.util.FileUtils;
import com.bts.adamcrm.util.ImageFileFilter;
import com.bts.adamcrm.util.ImageUtils;
import com.bts.adamcrm.util.RecyclerItemClickListener;
import com.bts.adamcrm.util.TimeUtils;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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

public class CustomerDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final int FILE_SELECT_CODE = 1111;
    AttachmentAdapter attachmentAdapter;
    @BindView(R.id.btn_add_invoice)
    Button btn_add_invoice;
    @BindView(R.id.btn_attach)
    Button btn_attach;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.btn_category)
    Button btn_category;
    @BindView(R.id.btn_date)
    TextView btn_date;
    @BindView(R.id.btn_delete)
    Button btn_delete;
    @BindView(R.id.btn_save)
    Button btn_save;
    @BindView(R.id.btn_send_email)
    Button btn_send_email;
    @BindView(R.id.btn_send_sms)
    Button btn_send_sms;
    private Calendar calendar;
    @BindView(R.id.chk_remind_me)
    CheckBox chk_remind_me;
    List<Category> categoryList = new ArrayList<>();
    Customer customer;
    String customer_str;
    Uri downloadUri;
    @BindView(R.id.edt_address)
    EditText edt_address;
    @BindView(R.id.edt_date_attachment)
    EditText edt_date_attachment;
    @BindView(R.id.edt_date_created)
    EditText edt_date_created;
    @BindView(R.id.edt_date_updated)
    EditText edt_date_updated;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_further_none)
    EditText edt_further_none;
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_post_code)
    EditText edt_post_code;
    @BindView(R.id.edt_reminder_date)
    EditText edt_reminder_date;
    @BindView(R.id.edt_title)
    EditText edt_title;
    @BindView(R.id.edt_town)
    EditText edt_town;
    String fileName;
    String currentPhotoPath;

    List<Invoice> invoiceList = new ArrayList();
    InvoiceAdapter invoiceAdapter = new InvoiceAdapter(invoiceList);
    ArrayList<String> attachmentList = new ArrayList<>();
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mSecond;
    private int mMonth;
    private int mYear;

    @BindView(R.id.recycler_attachments)
    RecyclerView recycler_attachments;
    @BindView(R.id.recycler_invoices)
    RecyclerView recycler_invoices;
    boolean reminder = false;
    int selectedIndex = -1;

    Category selected_category;
    int sms_sent = 0;
    @BindView(R.id.spn_state)
    Spinner spn_state;
    @BindView(R.id.title_text)
    TextView title_text;
    @BindView(R.id.txt_no_attachment)
    TextView txt_no_attachment;
    @BindView(R.id.txt_no_invoice)
    TextView txt_no_invoice;
    @BindView(R.id.txt_sms_staus)
    TextView txt_sms_staus;

    private static Activity mActivity;
    int initInvoice = 0;
    int customerID = 0;

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            showFileChooser();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            showToast("Permission Denied\n" + deniedPermissions.toString());
        }
    };

    public void showFileChooser(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.putExtra("android.intent.extra.LOCAL_ONLY", true);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    public static void launch(Activity activity, Customer customer){
        Intent intent = new Intent(activity, CustomerDetailActivity.class);
        intent.putExtra("customer", new Gson().toJson(customer));
        activity.startActivity(intent);
        mActivity = activity;
    }

    public static void launch(Activity activity){
        activity.startActivity(new Intent(activity.getBaseContext(), CustomerDetailActivity.class));
    }

    public void getCurrentDate(){
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void setupStateSpinner() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select state");
        arrayList.add("Active");
        arrayList.add("Completed");
        arrayList.add("Waiting");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spn_state.setAdapter(arrayAdapter);
    }

    private void setupCategory(){
        categoryList = new ArrayList<>();
        QueryContract.CategoryQuery categoryQuery = new CategoryQueryImplementation();
        categoryQuery.getAllCategories(new QueryResponse<List<Category>>() {
            @Override
            public void onSuccess(List<Category> data) {
                categoryList = data;
                Log("setupCategory size : "+ data.size());
                if (customer != null) {
                    Log("selected_category : " + customer.getName() + " ID : " + customer.getCategory_id());
                    for (Category category : categoryList){
                        if (category.getId() == customer.getCategory_id()){
                            selected_category = category;
                            btn_category.setText("Select Category : " + selected_category.getName());
                        }
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                Log("Category load Failed. message : " + message);
            }
        });
    }

    private void setupInvoice(int id) {
        QueryContract.InvoiceQuery invoiceQuery = new InvoiceQueryImplementation();
        invoiceQuery.getInvoiceById(id, new QueryResponse<List<Invoice>>() {
            @Override
            public void onSuccess(List<Invoice> data) {
                invoiceList = data;
                if (invoiceList.size() == 0){
                    recycler_invoices.setVisibility(View.GONE);
                    txt_no_invoice.setVisibility(View.VISIBLE);
                } else {
                    txt_no_invoice.setVisibility(View.GONE);
                    recycler_invoices.setVisibility(View.VISIBLE);
                    initInvoice = invoiceList.size();
                }
                invoiceAdapter.updateAdapter(invoiceList);
                recycler_invoices.setAdapter(invoiceAdapter);
                recycler_invoices.addOnItemTouchListener(new RecyclerItemClickListener(CustomerDetailActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        view.findViewById(R.id.icon_delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clickInvoiceDelete(CustomerDetailActivity.this, position);
                            }
                        });
                        view.findViewById(R.id.btn_view).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clickInvoiceView(position);
                            }
                        });
                    }
                }));
            }

            @Override
            public void onFailure(String message) {
                Log("Invoice load Failed. message : " + message);
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        ButterKnife.bind(this);
        setupStateSpinner();
        getCurrentDate();
        recycler_invoices.setVisibility(View.GONE);
        txt_no_invoice.setVisibility(View.VISIBLE);
        recycler_attachments.setVisibility(View.GONE);
        txt_no_attachment.setVisibility(View.VISIBLE);
        if (getIntent() != null){
            customer_str = getIntent().getStringExtra("customer");
            if (customer_str != null && !customer_str.equals("")){
                customer = new Gson().fromJson(getIntent().getStringExtra("customer"), Customer.class);
                edt_title.setText(customer.getTitle());
                edt_mobile.setText(customer.getMobile_phone());
                edt_email.setText(customer.getEmail());
                edt_name.setText(customer.getName());
                edt_town.setText(customer.getTown());
                edt_address.setText(customer.getAddress());
                edt_post_code.setText(customer.getPostal_code());
                edt_date_created.setText(
                        TimeUtils.formatDate(customer.getDate_created(),
                                TimeUtils.DB_DATE_TIME_FORMAT, TimeUtils.UI_DATE_FORMAT));
                edt_date_updated.setText(
                        TimeUtils.formatDate(customer.getDate_updated(),
                                TimeUtils.DB_DATE_TIME_FORMAT, TimeUtils.UI_DATE_FORMAT));
                edt_further_none.setText(customer.getFurther_note());

                edt_reminder_date.setEnabled(false);
                if (customer.getReminder_date() != null && !customer.getReminder_date().equals("")){
                    reminder = customer.getReminder_date() != null;
                    edt_reminder_date.setText(
                            TimeUtils.formatDate(customer.getReminder_date(),
                                    TimeUtils.DB_DATE_TIME_FORMAT, TimeUtils.UI_DATE_TIME_FORMAT));
                    edt_reminder_date.setEnabled(true);
                }
                spn_state.setSelection(customer.getState());
                sms_sent = customer.isSms_sent();
                chk_remind_me.setChecked(reminder);
                if (sms_sent == 1){
                    txt_sms_staus.setText(R.string.sms_sent);
                    txt_sms_staus.setTextColor(getResources().getColor(R.color.md_green_900));
                } else {
                    txt_sms_staus.setText(R.string.sms_not_sent);
                    txt_sms_staus.setTextColor(getResources().getColor(R.color.md_grey_700));
                }
                if (customer.getAttached_files() != null){
                    String [] strings = customer.getAttached_files().replaceAll("\\[", "").replaceAll("]", "")
                            .replaceAll("[-\\\\[\\\\]^/'*:!><~@#$%+=?|\"()]+", "").split(",");
                    if (strings.length > 0 && !strings[0].equals(""))
                        Collections.addAll(attachmentList, strings);
                }
                setupCategory();
                setupInvoice(customer.getId());
            }
        }
        setupCategory();
        attachmentAdapter  = new AttachmentAdapter(attachmentList);
        recycler_attachments.setAdapter(attachmentAdapter);

        if (invoiceList.size() == 0){
            recycler_invoices.setVisibility(View.GONE);
            txt_no_invoice.setVisibility(View.VISIBLE);
        } else {
            txt_no_invoice.setVisibility(View.GONE);
            recycler_invoices.setVisibility(View.VISIBLE);
        }
        recycler_invoices.setAdapter(invoiceAdapter);

        if (attachmentList.size() == 0){
            recycler_attachments.setVisibility(View.GONE);
            txt_no_attachment.setVisibility(View.VISIBLE);
        } else {
            txt_no_attachment.setVisibility(View.GONE);
            recycler_attachments.setVisibility(View.VISIBLE);
        }

        recycler_attachments.addOnItemTouchListener(new RecyclerItemClickListener(CustomerDetailActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.findViewById(R.id.icon_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickAttachDelete(CustomerDetailActivity.this, position);
                    }
                });
                view.findViewById(R.id.btn_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickAttachView(position);
                    }
                });
            }
        }));

        title_text.setText(R.string.customer_detail);
        edt_reminder_date.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_send_sms.setOnClickListener(this);
        btn_send_email.setOnClickListener(this);
        btn_add_invoice.setOnClickListener(this);
        btn_category.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        edt_date_created.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        txt_sms_staus.setOnClickListener(this);
        edt_date_attachment.setOnClickListener(this);
        btn_attach.setOnClickListener(this);
        btn_date.setOnClickListener(this);
        chk_remind_me.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                reminder = b;
                edt_reminder_date.setEnabled(b);
            }
        });
    }


    private void clickAttachDelete(Activity activity, int position) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_delete_item);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.delete_item);
        ((TextView) dialog.findViewById(R.id.txt)).setText("Are you sure you want to delete attachment " + this.attachmentList.get(position) + " ?");
        dialog.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attachmentList.size() > 0){
                    attachmentList.remove(position);
                    attachmentAdapter.updateAdapter(attachmentList);
                    if (attachmentList.size() == 0){
                        recycler_attachments.setVisibility(View.GONE);
                        txt_no_attachment.setVisibility(View.VISIBLE);
                    } else {
                        txt_no_attachment.setVisibility(View.GONE);
                        recycler_attachments.setVisibility(View.VISIBLE);
                    }
                    dialog.dismiss();
                }
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

    private void clickAttachView(int position){
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(ATTACH_FILE_URI + attachmentList.get(position)));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void clickInvoiceDelete(Activity activity, int position){
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_delete_item);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.delete_item);
        ((TextView) dialog.findViewById(R.id.txt)).setText("Are you sure you want to delete invoice " + this.invoiceList.get(position).getInvoice_no() + " ?");
        dialog.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (invoiceList.size() > 0) {
                    apiRepository.getApiService().deleteInvoice(invoiceList.get(position).getId()).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful() && response.body() != null){
                                Log("response body : " + response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            showToast("Please Activate internet connection!");
                        }
                    });
                    invoiceList.remove(position);
                    invoiceAdapter.updateAdapter(invoiceList);
                    if (invoiceList.size() == 0){
                        recycler_invoices.setVisibility(View.GONE);
                        txt_no_invoice.setVisibility(View.VISIBLE);
                    } else {
                        recycler_invoices.setVisibility(View.VISIBLE);
                        txt_no_invoice.setVisibility(View.GONE);
                    }
                    dialog.dismiss();
                }
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

    private void clickInvoiceView(int position){
        selectedIndex = position;
        if (position >= 0)
            CreateInvoiceActivity.launch(this, new Gson().toJson(invoiceList.get(position)));
    }

    public void showSelectorDialog(Activity activity) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage(R.string.permission_check_message)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

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

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()){
            case R.id.btn_add_invoice:
                CreateInvoiceActivity.launch(this, "");
                break;
            case R.id.btn_attach:
                if (edt_date_attachment.getText().toString().equals("")){
                    edt_date_attachment.setText(mDay + "/" + (mMonth + 1) + "/" + (mYear));
                }
                showSelectorDialog(this);
                break;
            case R.id.btn_back:
                clickBackDialog();
                break;
            case R.id.btn_category:
                showCategoryDialog(this);
                break;
            case R.id.btn_date:
                appendDateTime();
                break;
            case R.id.btn_delete:
                if (customer != null)
                    deleteItem(this, customer);
                break;
            case R.id.btn_save:
                if (edt_title.getText().toString().equals("") || edt_mobile.getText().toString().equals("")){
                    showToast("Please Enter Title and mobile number and category for this customers data!");
                } else if (spn_state.getSelectedItemPosition() == 0){
                    showToast("Please Select a proper state!");
                } else {
                    NetworkInfo nInfo = mConnManager.getActiveNetworkInfo();
                    boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                    if (connected) {
                        saveCustomer(this, true);
                    } else {
                        showToast("Please Activate internet connection!");
                    }
                }
                break;
            case R.id.btn_send_email:
                if (!edt_email.getText().toString().equals("") || customer != null && customer.getEmail() != null){
                    showSendEmailDialog(this);
                } else {
                    showToast("Please enter an email address!");
                }
                break;
            case R.id.btn_send_sms:
                if (!edt_mobile.getText().toString().equals("") || customer != null && customer.getMobile_phone() != null){
                    showSendSMSDialog(this);
                } else {
                    showToast("Please enter a mobile number!");
                }
                break;
            case R.id.edt_date_attachment:
                showAttachmentDateTimeDialog();
                break;
            case R.id.edt_date_created:
                showDateTimeDialog();
                break;
            case R.id.edt_reminder_date:
                showReminderDialog();
                break;
            case R.id.txt_sms_staus:
                showResetSMSDialog(this);
        }
    }

    private void showResetSMSDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_reset_sms);
        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        TextView txtMsg = dialog.findViewById(R.id.txt_msg);
        TextView btnNo = dialog.findViewById(R.id.btn_no);
        TextView btnYes = dialog.findViewById(R.id.btn_yes);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.edit_sms_state);
        if (sms_sent == 1){
            txtMsg.setText("Dou you want to set sms state to not sent?");
        } else {
            txtMsg.setText("Dou you want to set sms state to sent?");
        }
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sms_sent == 1){
                    txt_sms_staus.setText(R.string.sms_not_sent);
                    txt_sms_staus.setTextColor(getResources().getColor(R.color.md_grey_700));
                    sms_sent = 0;
                } else {
                    txt_sms_staus.setText(R.string.sms_sent);
                    txt_sms_staus.setTextColor(getResources().getColor(R.color.md_green_900));
                    sms_sent = 1;
                }
                dialog.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showReminderDialog() {
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
            mYear = i;
            mMonth = i1 + 1;
            mDay = i2;
            edt_reminder_date.setText(String.format("%02d/%02d", mDay, mMonth) + "/" + mYear);
            showReminderTimeDialog();
        }, mYear, mMonth, mDay).show();
    }

    private void showReminderTimeDialog() {
        calendar = Calendar.getInstance();
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                edt_reminder_date.append(String.format(" %02d:%02d", i, i1) + ":00");
            }
        }, mHour, mMinute, true).show();
    }

    private void showDateTimeDialog() {
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mYear = i;
                mMonth = i1 + 1;
                mDay = i2;
                edt_date_created.setText(mDay + "/" + mMonth + "/" + mYear);
            }
        }, mYear, mMonth, mDay).show();
    }

    private void showAttachmentDateTimeDialog() {
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mYear = i;
                mMonth = i1 + 1;
                mDay = i2;
                edt_date_attachment.setText(mDay + "/" + mMonth + "/" + mYear);
            }
        }, mYear, mMonth, mDay).show();
    }

    private void showSendSMSDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_send_sms);
        EditText editText = dialog.findViewById(R.id.edt_message);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.send_sms);
        dialog.findViewById(R.id.btn_attach).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectAttachments(activity);
            }
        });
        dialog.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.append("\n\n");
                if (selected_category == null){
                    showToast("Please select a category first");
                    return;
                }
                if (downloadUri != null)
                    editText.append(downloadUri.toString());
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

    private void sendSMS(String string, Dialog dialog) {
        String strNumber;
        if (customer.getMobile_phone() != null){
            strNumber = customer.getMobile_phone();
        } else {
            strNumber = edt_mobile.getText().toString();
        }
        sms_sent = 1;
        txt_sms_staus.setText(R.string.sms_sent);
        txt_sms_staus.setTextColor(getResources().getColor(R.color.md_green_900));
        try {
            Log.i("junior", strNumber);
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + strNumber));
//            intent.putExtra("address", strNumber);
            intent.putExtra("sms_body", string);
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
            showToast("No sms app found");
        }
        dialog.dismiss();
    }

    private void showSelectAttachments(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_attachments2);
        RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
        RecyclerView recyclerView2 = dialog.findViewById(R.id.recycler2);
        Attachment3Adapter attachment3Adapter = new Attachment3Adapter(attachmentList);
        InvoiceAdapter2 invoiceAdapter2 = new InvoiceAdapter2(invoiceList);
        recyclerView.setAdapter(attachment3Adapter);
        recyclerView2.setAdapter(invoiceAdapter2);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView2.setVisibility(View.VISIBLE);

        if (attachmentList.size() == 0)
            recyclerView.setVisibility(View.GONE);
        if (invoiceList.size() == 0)
            recyclerView2.setVisibility(View.GONE);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.findViewById(R.id.btn_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(Uri.parse(ATTACH_FILE_URI + attachmentList.get(position)));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                });
                view.findViewById(R.id.txt_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downloadUri = Uri.parse(ATTACH_FILE_URI + attachmentList.get(position));
                        showToast("Attachment selected!");
                        dialog.dismiss();
                    }
                });
            }
        }));
        recyclerView2.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.findViewById(R.id.btn_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(Uri.parse(INVOICE_PDF_URI + invoiceList.get(position).getId()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                });
                view.findViewById(R.id.txt_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (invoiceList.get(position).getInvoice_no() == null
                                || invoiceList.get(position).getInvoice_no().equals("")){
                            showToast("Please create invoice file first");
                        } else {
                            downloadUri = Uri.parse(INVOICE_PDF_URI + invoiceList.get(position).getId());
                            showToast("Attachment selected!");
                        }
                        dialog.dismiss();
                    }
                });
            }
        }));
        dialog.show();
    }

    private void showSendEmailDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_send_sms);
        EditText editText = dialog.findViewById(R.id.edt_message);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.send_email);
        dialog.findViewById(R.id.btn_attach).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectAttachments(activity);
            }
        });
        dialog.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.append("\n\n");
                if (selected_category == null){
                    showToast("Please select a category first");
                    return;
                }
                Log.i("junior", customer.getMobile_phone());
                Log.i("junior", edt_email.getText().toString());
                if (downloadUri != null)
                    editText.append(downloadUri.toString());
                sendEmail(editText.getText().toString(), dialog);
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

    private void sendEmail(String strMsg, Dialog dialog) {
        String strNumber;
        if (customer.getEmail() != null)
            strNumber = customer.getEmail();
        else
            strNumber = edt_email.getText().toString();
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.EMAIL", new String[]{strNumber});
        intent.putExtra("android.intent.extra.TEXT", strMsg);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        dialog.dismiss();
    }

    private void saveCustomer(Activity activity, boolean b) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_delete_item);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.save_item);
        ((TextView) dialog.findViewById(R.id.txt)).setText("Are you sure you want to save customer ?");
        dialog.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrUpdate(b, dialog);
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

    private void saveOrUpdate(boolean b, Dialog dialog) {
        if (selected_category == null){
            showToast("Please select a category first");
            return;
        }
        if (customer == null || customer.getTitle().equals("")){
            customer = new Customer();
            customer.setTitle(edt_title.getText().toString());
            customer.setMobile_phone(edt_mobile.getText().toString());
            customer.setEmail(edt_email.getText().toString());
            customer.setName(edt_name.getText().toString());
            customer.setAddress(edt_address.getText().toString());
            customer.setTown(edt_town.getText().toString());
            customer.setPostal_code(edt_post_code.getText().toString());
            customer.setFurther_note(edt_further_none.getText().toString());
            customer.setCategory_id(selected_category.getId());
            customer.setSms_sent(sms_sent);
            customer.setState(spn_state.getSelectedItemPosition());
            if (reminder) {
                customer.setReminder_date(edt_reminder_date.getText().toString());
                getDateAndSetupAlarm(customer.getReminder_date());
            } else {
                customer.setReminder_date("");
            }
            calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH) + 1;
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR);
            mMinute = calendar.get(Calendar.MINUTE);
            mSecond = calendar.get(Calendar.SECOND);
            String CurrentTime = String.format("%02d/%02d", mDay, mMonth) + "/" + mYear +
                    " " + String.format("%02d:%02d:%02d", mHour, mMinute, mSecond);
            if (edt_date_created.getText().toString().equals("")){
                customer.setDate_created(CurrentTime);
            }

            customer.setAttached_files(new Gson().toJson(attachmentList));

            apiRepository.getApiService().insertCustomer(customer.getTitle(), customer.getMobile_phone(), customer.getEmail(),
                    customer.getName(), customer.getAddress(), customer.getTown(), customer.getPostal_code(), customer.getFurther_note(),
                    customer.getState(), customer.getReminder_date(), customer.getCategory_id(), customer.getSms_sent(),
                    customer.getAttached_files(), customer.getDate_created()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null){
                        showToast("Customer Saved!");
                        customerID = Integer.parseInt(response.body());
                        saveInvoice(invoiceList);
                        dialog.dismiss();
                        sharedPreferencesManager.setBooleanValue("update", true);
                        exit();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, Throwable t) {
                    showToast("Please Activate internet connection!");
                }
            });
        } else {
            customer.setTitle(edt_title.getText().toString());
            customer.setMobile_phone(edt_mobile.getText().toString());
            customer.setEmail(edt_email.getText().toString());
            customer.setName(edt_name.getText().toString());
            customer.setAddress(edt_address.getText().toString());
            customer.setTown(edt_town.getText().toString());
            customer.setPostal_code(edt_post_code.getText().toString());
            customer.setState(spn_state.getSelectedItemPosition());
            customer.setFurther_note(edt_further_none.getText().toString());
            customer.setDate_created(edt_date_created.getText().toString());
            customer.setDate_updated(edt_date_updated.getText().toString());
            customer.setCategory_id(selected_category.getId());
            customer.setSms_sent(sms_sent);

            if (reminder) {
                customer.setReminder_date(edt_reminder_date.getText().toString());
                getDateAndSetupAlarm(customer.getReminder_date());
            } else {
                if (customer.getReminder_date() != null && !customer.getReminder_date().equals("")) {
                    getDateAndCancelAlarm(customer.getReminder_date());
                }
                customer.setReminder_date("");
            }
            customer.setAttached_files(new Gson().toJson(attachmentList));
            customerID = customer.getId();
            apiRepository.getApiService().updateCustomer(customer.getId(), customer.getTitle(), customer.getMobile_phone(), customer.getEmail(),
                    customer.getName(), customer.getAddress(), customer.getTown(), customer.getPostal_code(), customer.getFurther_note(),
                    customer.getState(), customer.getReminder_date(), customer.getCategory_id(), customer.getSms_sent(),
                    customer.getAttached_files()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null){
                        showToast("Customer Updated!");
                        saveInvoice(invoiceList);
                    } else {
                        showToast("Update Failed");
                    }
                    dialog.dismiss();
                    sharedPreferencesManager.setBooleanValue("update", true);
                    exit();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showToast("Please Activate internet connection!");
                }
            });
        }
    }

    private void saveInvoice(List<Invoice> invoiceList) {
        for (int i = 0; i < invoiceList.size(); i ++){
            Invoice invoice = invoiceList.get(i);
            Log("initInvoice : " + initInvoice + " customerID : " + customerID);
            String mode = "";
            int id = 0;
            if (i < initInvoice ) {
                mode = "update";
                id = invoice.getId();
            } else {
                mode = "add";
                id = 0;
            }
            apiRepository.getApiService().insertInvoice(
                    invoice.getInvoice_no(), invoice.getEmail(), invoice.getInvoice_date(), invoice.getMobile_num() ,
                    invoice.getTo(), invoice.getFrom_address(), invoice.getItems(), invoice.getExclude_vat(), invoice.getVat_amount(),
                    invoice.getInvoice_total(), invoice.getPayed_amount(), invoice.getDue_total(), invoice.getComment(),
                    customerID, invoice.getLogo1(), invoice.getLogo2(), id, mode
            ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null){
                        Log("Invoice Saved Successfully!");
                    } else {
                        showToast("Invoice Save Failed!");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showToast("Please Activate internet connection!");
                }
            });
        }
    }

    private void getDateAndSetupAlarm(String strDate) {
        try {
            if (new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(strDate).getTime() >= new Date().getTime()){
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(strDate);
                setupAlarm(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setupAlarm(long time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("time", time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) time, intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    private void getDateAndCancelAlarm(String strDate) {
        try {
            if (new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(strDate).getTime() >= new Date().getTime()){
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(strDate);
                cancelAlarm(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void cancelAlarm(long time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("time", time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) time, intent, PendingIntent.FLAG_ONE_SHOT);
        am.cancel(pendingIntent);
    }

    private void deleteItem(Activity activity, Customer customer) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_delete_item);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.delete_item);
        ((TextView) dialog.findViewById(R.id.txt)).setText("Are you sure you want to delete customer " + customer.getTitle() + " ?");
        dialog.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiRepository.getApiService().deleteCustomer(customer.getId()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful() && response.body() != null){
                            showToast("item deleted");
                            sharedPreferencesManager.setBooleanValue("update", true);
                            dialog.dismiss();
                            exit();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        showToast("Please Activate internet connection!");
                    }
                });
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

    private void appendDateTime() {
        edt_further_none.append(" " + getDateTime());
    }

    private String getDateTime() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
    }

    private void showCategoryDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_category);
        RecyclerView recyclerView = dialog.findViewById(R.id.recycler_category);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.select_category);
//        categoryList = new ArrayList<>();
        CategoryAdapter2 categoryAdapter2 = new CategoryAdapter2(categoryList);
        recyclerView.setAdapter(categoryAdapter2);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selected_category = categoryList.get(position);
                btn_category.setText("Select Category : " + selected_category.getName());
                dialog.dismiss();
            }
        }));

        ((TextView) dialog.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            if (requestCode == FILE_SELECT_CODE){
                Log("data : " + data.getData());
                String FilePath = FileUtils.getPath(this, data.getData());
                File file = new File(Objects.requireNonNull(FilePath));
                if (new ImageFileFilter().accept(file) && FileUtils.decideToCompress(FilePath)){
                    Uri imageUri = ImageUtils.getInstant().getCompressedBitmap(this, FilePath);
                    String tmpFilePath = FileUtils.getRealPathFromURI(this, imageUri);
                    file = new File(tmpFilePath);
                }
                uploadFile(file);
            } else if (requestCode == PICKER_REQUEST_CODE){
                String strAttach = data.getExtras().getString("attachment");
                boolean update = data.getExtras().getBoolean("update");
                //String attachment = new Gson().fromJson(strAttach);
                if (update && selectedIndex > -1){
                    attachmentList.remove(selectedIndex);
                    selectedIndex = -1;
                }
                //attachmentList.add(attachment);
                attachmentAdapter.updateAdapter(attachmentList);
                if (attachmentList.size() == 0){
                    recycler_attachments.setVisibility(View.GONE);
                    txt_no_attachment.setVisibility(View.VISIBLE);
                } else {
                    recycler_attachments.setVisibility(View.VISIBLE);
                    txt_no_attachment.setVisibility(View.GONE);
                }
            } else if (requestCode == INVOICE_REQUEST_CODE){
                String strInvoice = data.getExtras().getString("invoice");
                boolean update = data.getExtras().getBoolean("update");
                Invoice invoice = new Gson().fromJson(strInvoice, Invoice.class);
                if (update && selectedIndex > -1){
                    invoiceList.remove(selectedIndex);
                    selectedIndex = -1;
                }
                invoiceList.add(invoice);
                invoiceAdapter.updateAdapter(invoiceList);
                if (invoiceList.size() == 0){
                    recycler_invoices.setVisibility(View.GONE);
                    txt_no_invoice.setVisibility(View.VISIBLE);
                } else {
                    recycler_invoices.setVisibility(View.VISIBLE);
                    txt_no_invoice.setVisibility(View.GONE);
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
        NetworkInfo nInfo = mConnManager.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        if (!connected) {
            showToast("Please Activate internet connection!");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        RequestBody requestFile = RequestBody.create(
                MediaType.parse("*/*"),
                file
        );
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        apiRepository.getApiService().uploadCustomerAttach(body).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Log("response : " + response.body());
                    String uploadFile = response.body();
                    showToast("upload File :" + uploadFile);
                    attachmentList.add(uploadFile);
                    attachmentAdapter.updateAdapter(attachmentList);
                    if (attachmentList.size() > 0) {
                        recycler_attachments.setVisibility(View.VISIBLE);
                        txt_no_attachment.setVisibility(View.GONE);
                    } else {
                        recycler_attachments.setVisibility(View.GONE);
                        txt_no_attachment.setVisibility(View.VISIBLE);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("Please Activate internet connection!");
                progressDialog.dismiss();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clickBackDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void clickBackDialog() {
        new AlertDialog.Builder(this)
            .setMessage(R.string.discard_change)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            })
            .show();
    }
}
