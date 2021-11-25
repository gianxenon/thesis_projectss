package com.example.contact_tracing_two;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText edt_usr, edt_pass;
    Button log_btn_login;
    TextView log_registerBtn,log_txtV_forgotpass;
    SpannableString text_signup, text_forgetpass;
    private ProgressDialog pd;
    String chkstats ;
    private final String loginUrl = "https://bscs2018db.000webhostapp.com/android_gpstrace/login.php";
    private RequestQueue requestQueue;
    private static final String TAG= MainActivity.class.getSimpleName();

    String usr_id,usr_fullname,usr_emailadd,usr_birthdate,usr_gender,usr_region,usr_province,usr_municipality
            ,usr_brgy,usr_username,usr_password,usr_cpnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //black icon status Bar
        Utils.blackiconStatusBar(MainActivity.this,R.color.light_Background);

        log_btn_login = findViewById(R.id.log_logBtn);
        edt_usr = findViewById(R.id.edt_usr);
        edt_pass = findViewById(R.id.edt_pass);

        log_registerBtn = findViewById(R.id.log_registerBtn);
        log_txtV_forgotpass = findViewById(R.id.log_txtV_forgotpass);

        //login xml => sign up and forget password Underline design
        text_signup = new SpannableString( "Sign Up HERE") ;
        text_signup.setSpan( new UnderlineSpan() , 0 , text_signup.length() , 0 ) ;
        log_registerBtn.setText(text_signup);

        text_forgetpass = new SpannableString("Forget Password?") ;
        text_forgetpass.setSpan( new UnderlineSpan() , 0 , text_forgetpass.length() , 0 ) ;
        log_txtV_forgotpass.setText(text_forgetpass);
        pd = new ProgressDialog(this);

        log_txtV_forgotpass.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, forgetpass_confirmation.class);
            startActivity(intent);

        });

        //login xml  Button
        log_btn_login.setOnClickListener(v -> {
            pd.setTitle("Logging In");
            pd.setMessage("Please wait...");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            sendUrl();
        });

        //login xml sign_up textview
        log_registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, registration.class);
                startActivity(intent);
            }
        });

    } //end of on create

    private void sendUrl( ){

        StringRequest stringRequest  = new StringRequest(Request.Method.POST, loginUrl, response -> {
            try {
                JSONObject jobj = new JSONObject(response);
                String success = jobj.getString("success");
                JSONArray sad = jobj.getJSONArray("user_info");
                Log.d("resposens" , response);
                if (success.equals("1")) {
                    chkstats= "success";
                    for (int i =0;  i < jobj.length()-1; i++) {
                        JSONObject sads = sad.getJSONObject(i);
                        usr_id = sads.getString("usr_id");
                        usr_fullname = sads.getString("usr_fullname");
                        usr_emailadd = sads.getString("usr_emailadd");
                        usr_birthdate = sads.getString("usr_birthdate");
                        usr_gender = sads.getString("usr_gender");
                        usr_region = sads.getString("usr_region");
                        usr_province = sads.getString("usr_province");
                        usr_municipality = sads.getString("usr_municipality");
                        usr_brgy = sads.getString("usr_brgy");
                        usr_username = sads.getString("usr_username");
                        usr_password = sads.getString("usr_password");
                        usr_cpnumber = sads.getString("usr_cpnumber");
                    }
                    pd.setMessage("Logged In successfully");
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, dashboard.class);
                    intent.putExtra("usr_id",usr_id);
                    intent.putExtra("usr_fullname",usr_fullname);
                    intent.putExtra("usr_emailadd",usr_emailadd);
                    intent.putExtra("usr_birthdate",usr_birthdate);
                    intent.putExtra("usr_gender",usr_gender);
                    intent.putExtra("usr_region",usr_region);
                    intent.putExtra("usr_province",usr_province);
                    intent.putExtra("usr_municipality",usr_municipality);
                    intent.putExtra("usr_brgy",usr_brgy);
                    intent.putExtra("usr_username",usr_username);
                    intent.putExtra("usr_password",usr_password);
                    intent.putExtra("usr_cpnumber",usr_cpnumber);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(MainActivity.this, "Login Failed  ",  Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "000WEBHOST Error Occured" + e, Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        }, error -> {
            Toast.makeText(MainActivity.this, " 5 000WEBHOST Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
            pd.dismiss();
        }){
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String,String>();
                params.put("usr_username",edt_usr.getText().toString());
                params.put("usr_password",edt_pass.getText().toString());
                return params;
            }
        };
        requestQueue= Volley.newRequestQueue(MainActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,1,1.0f));
        requestQueue.add(stringRequest);
    }

}
//end of Main Activity class