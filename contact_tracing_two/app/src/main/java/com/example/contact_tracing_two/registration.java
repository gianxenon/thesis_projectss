package com.example.contact_tracing_two;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class registration extends AppCompatActivity {

    EditText reg_edt_Fname,reg_edt_Mname,reg_edt_Lname,reg_edt_Email,reg_edt_usr,reg_edt_ContactNum;
    Spinner edt_spinner_gender,reg_spnr_Region,
            reg_spnr_Province, reg_spnr_Municipality,reg_spnr_brgy;
    TextView reg_edt_Bdate;
    TextInputLayout reg_txtin_pass;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    String gender_choice,region_choice,province_choice,municipality_choice,brgy_choice;
    Button reg_Btn_SignUp;
    private final String registrationUrl = "https://bscs2018db.000webhostapp.com/android_gpstrace/registration.php";
    private RequestQueue requestQueue;
    private static final String TAG= registration.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //black icon status Bar
        Utils.blackiconStatusBar(registration.this,R.color.light_Background);

        reg_edt_Fname           = findViewById(R.id.reg_edt_Fname);
        reg_edt_Mname           = findViewById(R.id.reg_edt_Mname);
        reg_edt_Lname           = findViewById(R.id.reg_edt_Lname);
        reg_edt_Email           = findViewById(R.id.reg_edt_email);
        reg_edt_Bdate           = findViewById(R.id.reg_edt_Bdate);
        edt_spinner_gender     = findViewById(R.id.reg_edt_gender);
        reg_spnr_Region         = findViewById(R.id.reg_spnr_Region);
        reg_spnr_Province       = findViewById(R.id.reg_spnr_Province);
        reg_spnr_Municipality   = findViewById(R.id.reg_spnr_Municipality);
        reg_spnr_brgy           = findViewById(R.id.reg_spnr_brgy);
        reg_txtin_pass          = findViewById(R.id.reg_txtIn_pass);
        reg_edt_usr             = findViewById(R.id.reg_edt_usr);
        reg_edt_ContactNum      = findViewById(R.id.reg_edt_ContactNum);
        reg_edt_Bdate.setOnClickListener((View v) -> {
            reg_edt_Bdate.setFocusable(false);
            reg_edt_Bdate.clearFocus();
            reg_edt_Bdate.setError(null);
            Calendar calendar = Calendar.getInstance();
            int year =calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(registration.this,onDateSetListener,year,month,day);
            datePickerDialog.show();

            edt_spinner_gender.requestFocus();

        });
        onDateSetListener = (view, year, month, dayOfMonth) -> {
            month = month +1;
            String date = dayOfMonth + "/" + month + "/" + year;
            reg_edt_Bdate.setText(date);


        };

        //REGISTRATION GENDER SPINNER
        ArrayList<String> gender = new ArrayList<>();
        gender.add("-Select-");
        gender.add("MALE");
        gender.add("FEMALE");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,gender);
        edt_spinner_gender.setAdapter(adapter);
        //spinner_choice;
        edt_spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender_choice = gender.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//REGISTRATION REGION SPINNER
         ArrayList<String> region = new ArrayList<>();
        region.add("-Select-");
        region.add("NATIONAL CAPITAL REGION (NCR)");
        ArrayAdapter<String> region2 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,region);
        reg_spnr_Region.setAdapter(region2);
        //spinner_choice;
        reg_spnr_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                region_choice = region.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         ArrayList<String> province = new ArrayList<>();
        province.add("-Select-");
        province.add("NCR, FOURTH DISTRICT");
        ArrayAdapter<String> province1 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,province);
        reg_spnr_Province.setAdapter(province1);
        //spinner_choice;
        reg_spnr_Province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                province_choice = province.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


         ArrayList<String> municipality = new ArrayList<>();
        municipality.add("-Select-");
        municipality.add("TAGUIG CITY");
        ArrayAdapter<String> municipality1 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,municipality);
        reg_spnr_Municipality.setAdapter(municipality1);
        //spinner_choice;
        reg_spnr_Municipality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                municipality_choice = municipality.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayList<String> brgy_choice1 = new ArrayList<>();
        brgy_choice1.add("-Select-");
        brgy_choice1.add("BAGUMBAYAN");
        brgy_choice1.add("TANYAG (BAGONG TANYAG");
        brgy_choice1.add("WESTERN BICUTAN");
        brgy_choice1.add("UPPER BICUTAN");
        brgy_choice1.add("LOWER BICUTAN");
        brgy_choice1.add("CENTRAL BICUTAN");
        brgy_choice1.add("NEW LOWER BICUTAN");
        brgy_choice1.add("HAGONOY");
        brgy_choice1.add("NORTH DAANG HARI");
        brgy_choice1.add("NORTH SIGNAL VILLAGE");
        brgy_choice1.add("CENTRAL SIGNAL VILLAGE (SIGNAL VILLAGE");
        brgy_choice1.add("PALINGON");
        brgy_choice1.add("PINAGSAMA");
        brgy_choice1.add("SAN MIGUEL");
        brgy_choice1.add("SANTA ANA");
        brgy_choice1.add("SOUTH DAANG HARI");
        brgy_choice1.add("SOUTH SIGNAL VILLAGE");
        brgy_choice1.add("TUKTUKAN");
        brgy_choice1.add("USUSAN");
        brgy_choice1.add("WAWA");
        brgy_choice1.add("BAMBANG");
        brgy_choice1.add("CALZADA");
        brgy_choice1.add("FORT BONIFACIO");
        brgy_choice1.add("IBAYO-TIPAS");
        brgy_choice1.add("KATUPARAN");
        brgy_choice1.add("LIGID-TIPAS");
        brgy_choice1.add("MAHARLIKA VILLAGE");
        brgy_choice1.add("NAPINDAN");
        ArrayAdapter<String> brgy_choice2 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,brgy_choice1);
        reg_spnr_brgy.setAdapter(brgy_choice2);
        //spinner_choice;
        reg_spnr_brgy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brgy_choice = brgy_choice1.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reg_Btn_SignUp = findViewById(R.id.reg_Btn_SignUp);
        reg_Btn_SignUp.setOnClickListener(v -> {
            if(reg_edt_Fname.getText().length() == 0){
                reg_edt_Fname.setError("Enter Name");
                reg_edt_Fname.requestFocus();
            }else if (reg_edt_Lname.getText().length() == 0){
                reg_edt_Bdate.setFocusable(true);
                reg_edt_Lname.setError("Enter Last Name");
                reg_edt_Lname.requestFocus();
            }else if (reg_edt_Email.getText().length() == 0){
                reg_edt_Email.setError("Enter Email Address");
                reg_edt_Email.requestFocus();
            }else if (reg_edt_ContactNum.getText().length() == 0){
                reg_edt_ContactNum.setError("Enter Contact Number");
                reg_edt_ContactNum.requestFocus();
            } else if (reg_edt_Bdate.getText().toString().length() == 0){
                Toast.makeText(this, "Select your Birth Date",Toast.LENGTH_SHORT).show();
                reg_edt_Bdate.setError("Choose Birth Date");
                edt_spinner_gender.setFocusable(true);
                edt_spinner_gender.setFocusableInTouchMode(true);
                reg_edt_Bdate.requestFocus();
            }else if(reg_edt_usr.getText().length() == 0 ){
                reg_edt_usr.setError("Enter Username");
                reg_edt_usr.requestFocus();
            }else if(reg_txtin_pass.getEditText().getText().length() == 0 ||reg_txtin_pass.getEditText().getText().length() < 9 ){
                reg_txtin_pass.setError("Must Contain at least 9 characters");
                reg_txtin_pass.requestFocus();
            }else if (gender_choice.equals("-Select-")){
                Toast.makeText(this, "Select your Gender",Toast.LENGTH_SHORT).show();
                ((TextView)edt_spinner_gender.getChildAt(0)).setError("Message");

                edt_spinner_gender.setFocusable(true);
                edt_spinner_gender.setFocusableInTouchMode(true);
                edt_spinner_gender.requestFocus();
            }else if (region_choice.equals("-Select-")){
                Toast.makeText(this, "Select your Region",Toast.LENGTH_SHORT).show();
                ((TextView)reg_spnr_Region.getChildAt(0)).setError("Message");

                reg_spnr_Region.setFocusable(true);
                reg_spnr_Region.setFocusableInTouchMode(true);
                reg_spnr_Region.requestFocus();
            }else if (municipality_choice.equals("-Select-")){
                Toast.makeText(this, "Select your Municipality",Toast.LENGTH_SHORT).show();
                ((TextView)reg_spnr_Municipality.getChildAt(0)).setError("Message");

                reg_spnr_Municipality.setFocusable(true);
                reg_spnr_Municipality.setFocusableInTouchMode(true);
                reg_spnr_Municipality.requestFocus();
            }else if (province_choice.equals("-Select-")){
                Toast.makeText(this, "Select your Province",Toast.LENGTH_SHORT).show();
                ((TextView)reg_spnr_Province.getChildAt(0)).setError("Message");

                reg_spnr_Province.setFocusable(true);
                reg_spnr_Province.setFocusableInTouchMode(true);
                reg_spnr_Province.requestFocus();
            }else if (brgy_choice.equals("-Select-")){
                Toast.makeText(this, "Select your Barangay",Toast.LENGTH_SHORT).show();
                ((TextView)reg_spnr_brgy.getChildAt(0)).setError("Message");

                reg_spnr_brgy.setFocusable(true);
                reg_spnr_brgy.setFocusableInTouchMode(true);
                reg_spnr_brgy.requestFocus();
            }else{
               sendData();
            }


        });



    }//end of onCreate

    //sending and fetching to data
    private void sendData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, registrationUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String success = jsonObject.getString("success");
                if (success.equals("1")) {

                //    fn.setText("");

                    Toast.makeText(registration.this,"Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(registration.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(registration.this,"Registration Failed" +  "\n" + response, Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                Toast.makeText(registration.this, "Error Occurred  : " + e, Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(registration.this, "error on response Volley Error " + error.getMessage(), Toast.LENGTH_LONG).show()){
            public Map<String , String> getParams(){
                Map<String, String> params = new HashMap<>();

                String fn = reg_edt_Fname.getText().toString() + " " + reg_edt_Mname.getText().toString()  + " " + reg_edt_Lname.getText().toString();
                params.put("fn", fn);
                params.put("reg_edt_Email", reg_edt_Email.getText().toString());
                params.put("reg_edt_Bdate", reg_edt_Bdate.getText().toString());
                params.put("reg_edt_usr", reg_edt_usr.getText().toString()) ;
                params.put("reg_txtin_pass",reg_txtin_pass.getEditText().getText().toString());
                params.put("gender_choice", gender_choice);
                params.put("region_choice", region_choice);
                params.put("province_choice", province_choice);
                params.put("municipality_choice", municipality_choice);
                params.put("brgy_choice", brgy_choice);
                params.put("reg_edt_ContactNum", reg_edt_ContactNum.getText().toString());
                Log.d(TAG,"sd "+params.toString());
                return  params;
            }
        };
        requestQueue = Volley.newRequestQueue(registration.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,1,1.0f));
        requestQueue.add(stringRequest);
    }
}