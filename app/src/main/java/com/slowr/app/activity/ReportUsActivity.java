package com.slowr.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.slowr.app.R;
import com.slowr.app.adapter.RentalDurationAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.ReportResponsModel;
import com.slowr.app.models.ReportTypeModel;
import com.slowr.app.models.SortByModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class ReportUsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextView txt_page_title;
    LinearLayout img_back;
    ImageView img_drop_down;
    //    Spinner sp_report_type;
    EditText edt_name;
    EditText edt_email;
    EditText edt_description;
    EditText edt_report_typ;
    Button btn_submit;
    Button btn_continue;
    LinearLayout layout_report_details;
    LinearLayout layout_report_success;
    TextInputLayout til_edt_name;
    TextInputLayout til_email;
    TextInputLayout til_edt_type;
    TextView txt_ticket_id;
    TextView txt_description_count;
    TextView txt_description_same;
    ArrayAdapter<String> spinnerAdapter;
    private PopupWindow spinnerPopup;
    private Function _fun = new Function();

    ArrayList<SortByModel> reportTypeList = new ArrayList<>();
    ArrayList<String> reportTypeStringList = new ArrayList<>();

    HashMap<String, String> params = new HashMap<String, String>();
    RentalDurationAdapter rentalDurationAdapter;

    String reportTypeId = "";
    String pageFrom = "1";
    String adId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_us);
        doDeclaration();
    }

    private void doDeclaration() {
        if (getIntent().hasExtra("PageFrom")) {
            pageFrom = getIntent().getStringExtra("PageFrom");
            if (pageFrom.equals("2")) {
                adId = getIntent().getStringExtra("AdId");
            }
        }
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
//        sp_report_type = findViewById(R.id.sp_report_type);
        img_drop_down = findViewById(R.id.img_drop_down);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_description = findViewById(R.id.edt_description);
        btn_submit = findViewById(R.id.btn_submit);
        til_edt_name = findViewById(R.id.til_edt_name);
        layout_report_details = findViewById(R.id.layout_report_details);
        layout_report_success = findViewById(R.id.layout_report_success);
        btn_continue = findViewById(R.id.btn_continue);
        txt_ticket_id = findViewById(R.id.txt_ticket_id);
        edt_report_typ = findViewById(R.id.edt_report_typ);
        til_edt_type = findViewById(R.id.til_edt_type);
        txt_description_count = findViewById(R.id.txt_description_count);
        txt_description_same = findViewById(R.id.txt_description_same);

        rentalDurationAdapter = new RentalDurationAdapter(reportTypeStringList, getApplicationContext());

        spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.layout_spinner_item, reportTypeStringList);
// Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
// Apply the adapter to the spinner
//        sp_report_type.setAdapter(spinnerAdapter);
//        sp_report_type.setOnItemSelectedListener(this);

        txt_page_title.setText(getString(R.string.nav_report_us));
        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        til_edt_type.setOnClickListener(this);
        edt_report_typ.setOnClickListener(this);

        if (_fun.isInternetAvailable(ReportUsActivity.this)) {
            getReportType();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(ReportUsActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getReportType();
                        }
                    });
                }
            }, 200);

        }
        CallBackFunction();
        if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
            edt_name.setText(Sessions.getSession(Constant.UserName, getApplicationContext()));
            edt_email.setText(Sessions.getSession(Constant.UserEmail, getApplicationContext()));
        }

        if (pageFrom.equals("2")) {
            btn_continue.setText(getText(R.string.txt_continue));
        } else {
            btn_continue.setText(getText(R.string.txt_home_page));
        }
    }

    private void CallBackFunction() {
        txt_description_count.setText(getString(R.string.txt_report_count, "0"));
        edt_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int desValue = edt_description.getText().toString().length();
                txt_description_count.setText(getString(R.string.txt_report_count, String.valueOf(desValue)));
                if (desValue == 200) {
                    Function.CustomMessage(ReportUsActivity.this, getString(R.string.txt_limit_reached));
                }
            }
        });
        rentalDurationAdapter.setCallback(new RentalDurationAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                edt_report_typ.setText(reportTypeStringList.get(pos));
                reportTypeId = reportTypeList.get(pos).getSortId();
                if (spinnerPopup.isShowing()) {
                    spinnerPopup.dismiss();
                }
            }
        });

        edt_description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    doValidation();
                    return true;
                }
                return false;
            }
        });

    }

    private void getReportType() {
        RetrofitClient.getClient().create(Api.class).getReportType()
                .enqueue(new RetrofitCallBack(ReportUsActivity.this, cityValue, true, false));
    }

    retrofit2.Callback<ReportTypeModel> cityValue = new retrofit2.Callback<ReportTypeModel>() {
        @Override
        public void onResponse(Call<ReportTypeModel> call, retrofit2.Response<ReportTypeModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                ReportTypeModel dr = response.body();
                if (dr.isStatus()) {
                    if (reportTypeList.size() != 0) {
                        reportTypeList.clear();
                    }
                    reportTypeList.addAll(dr.getReportTypeList());
                    for (int i = 0; i < reportTypeList.size(); i++) {
                        if (pageFrom.equals("1")) {
                            if (reportTypeList.get(i).getActionFrom() == null) {
                                reportTypeStringList.add(reportTypeList.get(i).getSortValue());
                            }
                        } else {
                            if (reportTypeList.get(i).getActionFrom() != null) {
                                reportTypeStringList.add(reportTypeList.get(i).getSortValue());
                            }
                        }

                    }
                    if (reportTypeList.size() != 0) {
                        reportTypeId = reportTypeList.get(0).getSortId();
                        edt_report_typ.setText(reportTypeStringList.get(0));
                    }
//                    spinnerAdapter.notifyDataSetChanged();
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d("TAG", t.getMessage());
            call.cancel();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_submit:

//                txt_description_same.setText(Function.removeUrl(edt_description.getText().toString()));
                doValidation();
                break;
            case R.id.btn_continue:
//                layout_report_details.setVisibility(View.VISIBLE);
//                layout_report_success.setVisibility(View.GONE);
                if (pageFrom.equals("2")) {
                    finish();
                } else {
                    Intent h = new Intent(ReportUsActivity.this, HomeActivity.class);
                    h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(h);
                    finish();
                }
                break;
            case R.id.til_edt_type:
                ShowPopupProsper();
                break;
            case R.id.edt_report_typ:
                ShowPopupProsper();
                break;
        }
    }

    private void doValidation() {
        Function.hideSoftKeyboard(ReportUsActivity.this, edt_name);
        String name = edt_name.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        String comments = edt_description.getText().toString();
        if (name.isEmpty()) {
            Function.CustomMessage(ReportUsActivity.this, getString(R.string.enter_name));
            return;
        }
        if (name.length() < 3) {
            Function.CustomMessage(ReportUsActivity.this, getString(R.string.enter_name_minimum));
            return;
        }
        if (email.isEmpty()) {
            Function.CustomMessage(ReportUsActivity.this, getString(R.string.enter_your_phone_no_email_content));
            return;
        }
        if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Function.CustomMessage(ReportUsActivity.this, getString(R.string.enter_email));
            return;
        }
        if (comments.length() == 0) {
            Function.CustomMessage(ReportUsActivity.this, getString(R.string.enter_comments));
            return;
        }
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                ReportUsActivity.this);

        alertDialog2.setTitle("Report");

        alertDialog2.setMessage(getString(R.string.report_message));

        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (_fun.isInternetAvailable(ReportUsActivity.this)) {
                            SubmitReport();
                        } else {
                            _fun.ShowNoInternetPopup(ReportUsActivity.this, new Function.NoInternetCallBack() {
                                @Override
                                public void isInternet() {
                                    SubmitReport();
                                }
                            });

                        }
                    }
                });

        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog2.show();


    }

    private void SubmitReport() {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("name", edt_name.getText().toString());
        params.put("email", edt_email.getText().toString());
        params.put("mobile_no", "");
        params.put("type", reportTypeId);
        params.put("ads_id", adId);
        params.put("description", edt_description.getText().toString());
        Log.i("params", params.toString());
        RetrofitClient.getClient().create(Api.class).saveReport(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(ReportUsActivity.this, saveReportApi, true, false));
    }

    retrofit2.Callback<ReportResponsModel> saveReportApi = new retrofit2.Callback<ReportResponsModel>() {
        @Override
        public void onResponse(Call<ReportResponsModel> call, retrofit2.Response<ReportResponsModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                ReportResponsModel dr = response.body();
                if (dr.isStatus()) {
                    layout_report_details.setVisibility(View.GONE);
                    layout_report_success.setVisibility(View.VISIBLE);
                    txt_ticket_id.setText("Ticket ID : " + dr.getReportTicketModel().getTicketNo());
                    edt_description.setText("");
                    edt_email.setText("");
                    edt_name.setText("");

//                    sp_report_type.setSelection(0);

//                    finish();
                } else {
                    Function.CustomMessage(ReportUsActivity.this, dr.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {

            Log.d("TAG", t.getMessage());
            call.cancel();
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        reportTypeId = reportTypeList.get(position).getSortId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void ShowPopupProsper() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_report_type_popup, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();
        LinearLayout img_popup_back = view.findViewById(R.id.img_back);
        TextView txt_popup_title = view.findViewById(R.id.txt_page_title);
        final RecyclerView rc_filter = view.findViewById(R.id.rc_filter);
        txt_popup_title.setText(getString(R.string.txt_report_type_select));
        LinearLayoutManager listManager = new LinearLayoutManager(ReportUsActivity.this, RecyclerView.VERTICAL, false);
        rc_filter.setLayoutManager(listManager);
        rc_filter.setItemAnimator(new DefaultItemAnimator());
        rc_filter.setAdapter(rentalDurationAdapter);
        img_popup_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
            }
        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
