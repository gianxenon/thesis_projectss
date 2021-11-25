package com.example.contact_tracing_two;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class forgetpass_confirmation extends AppCompatActivity {
    EditText fpass_edt_usr,fpass_edt_cntrnum,fpass_code;
    private  View fpass_lay1,fpass_laytwo;
    Button fpass_btn_Continue,fpass_btn_submit;
    TextView resendCodeTv;
    private static final String KEY_VERIFICATION_ID = "key_verification_id";
    //if code  failed, will resend code otp
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;
        String sad = "sads";
    private static  final String TAG ="forgetpass_confirmation";

    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass_confirmation);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //black icon status Bar
        Utils.blackiconStatusBar(forgetpass_confirmation.this,R.color.light_Background);

        fpass_lay1 = findViewById(R.id.fpass_lay1);
        fpass_laytwo = findViewById(R.id.fpass_laytwo);
        fpass_edt_usr = findViewById(R.id.fpass_edt_usr);
        fpass_edt_cntrnum = findViewById(R.id.fpass_edt_cntrnum);
        fpass_btn_Continue = findViewById(R.id.fpass_btn_Continue);
        fpass_btn_submit = findViewById(R.id.fpass_btn_submit);
        resendCodeTv =  findViewById(R.id.resendCodeTv);
        fpass_code = findViewById(R.id.fpass_code);

        TextView fpass_contentDes = findViewById(R.id.fpass_contentDes);
        fpass_lay1.setVisibility(View.VISIBLE);
        fpass_laytwo.setVisibility(View.GONE);
        //end of casting

        firebaseAuth = FirebaseAuth.getInstance();
        if (mVerificationId == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        //init progress dialog
        pd = new ProgressDialog(this);
        pd.setTitle("Please Wait..");
        pd.setCanceledOnTouchOutside(false);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //this callback will be invoked in two situations:
                //1 = Instant Verification. In some cases the phone number can be instanly
                //                          Verified without needing to send or enter a verification code.
                //2 = Auto-retrival. On some devices Google play services can automatically
                //                  detect the incoming verification SMS and perform verification without
                //                  user action.
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                    //this callback is invoked in an invalid request for verification is made,
                    //  for instance if the phone number format is not Valid.
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"onVerificationFailed = "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, token);
                // the sms Verification code has been sent to the povider phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG,"onCodeSent " + verificationId);
                mVerificationId =verificationId;
                forceResendingToken = token;

                pd.dismiss();
                //hide phone layout,show code layout
                fpass_lay1.setVisibility(View.GONE);
                fpass_laytwo.setVisibility(View.VISIBLE);

                Toast.makeText(forgetpass_confirmation.this, "Verification code Sent...", Toast.LENGTH_SHORT).show();
                String desContent = "Please type the verification code we sent \nto " +fpass_edt_cntrnum.getText().toString().trim();
                fpass_contentDes.setText(desContent);
            }
        };

        fpass_btn_Continue.setOnClickListener(v -> {

            String fpass_usr =  fpass_edt_usr.getText().toString();
            String fpass_cntrnum = fpass_edt_cntrnum.getText().toString();
            if(TextUtils.isEmpty(fpass_usr) ){
                Toast.makeText(getApplicationContext(),"Please Enter Username....", Toast.LENGTH_SHORT).show();

            }else if(TextUtils.isEmpty(fpass_cntrnum)){
                Toast.makeText(getApplicationContext(),"Please Enter Contact Number....", Toast.LENGTH_SHORT).show();

            }else{
                startPhoneNumberVerification(fpass_cntrnum);
            }
        });

        resendCodeTv.setOnClickListener(v -> {

            String fpass_cntrnum = fpass_edt_cntrnum.getText().toString();
           if(TextUtils.isEmpty(fpass_cntrnum)){
                Toast.makeText(getApplicationContext(),"Please Enter Contact Number....", Toast.LENGTH_SHORT).show();

            }else{
                startPhoneNumberVerification(fpass_cntrnum);
            }
           resendVerificationCode(fpass_cntrnum,forceResendingToken);
        });

        fpass_btn_submit.setOnClickListener(v -> {
            String fpass_codes = fpass_code.getText().toString();
            if(TextUtils.isEmpty(fpass_codes)){
                Toast.makeText(getApplicationContext(),"Please Enter Verification Code....", Toast.LENGTH_SHORT).show();

            }else{
                verifyPhoneNumberWithCode(mVerificationId,fpass_codes);
            }
        });




    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_VERIFICATION_ID,mVerificationId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationId = savedInstanceState.getString(KEY_VERIFICATION_ID);
    }




    private void startPhoneNumberVerification(String fpass_cntrnum) {
        pd.setMessage("Verifying Phone Number");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(fpass_cntrnum)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerificationCode(String fpass_cntrnum, PhoneAuthProvider.ForceResendingToken token) {
pd.setMessage("Resending Code..");

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(fpass_cntrnum)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String fpass_codes) {
        pd.setMessage("Verifiying Code...");
        pd.show();

    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,fpass_codes);
    signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        pd.setMessage("Logging In");
            firebaseAuth.signInWithCredential(credential)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //Successfully signed In
                            pd.dismiss();
                            String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                            Toast.makeText(getApplicationContext(),"Logged In as" + phone, Toast.LENGTH_SHORT).show();
                            //start change pass Activity
                            startActivity(new Intent(forgetpass_confirmation.this,changePassword.class));
                           // finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // failed signing in
                            Toast.makeText(forgetpass_confirmation.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

    }
}