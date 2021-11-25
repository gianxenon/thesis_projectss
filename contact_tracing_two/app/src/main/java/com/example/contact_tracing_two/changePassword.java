package com.example.contact_tracing_two;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class changePassword extends AppCompatActivity {

        TextInputLayout cpass_one,cpass_two;
        Button cpass_btn_Newpass;
    FirebaseAuth firebaseAuth;
        String sad = "sad";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //black icon status Bar
        Utils.blackiconStatusBar(changePassword.this,R.color.light_Background);


        cpass_btn_Newpass = findViewById(R.id.cpass_btnSubmit);
        cpass_one = findViewById(R.id.cpass_txtInlay_passOne);
        cpass_two = findViewById(R.id.cpass_txtInlay_passTwo);
        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();


        cpass_btn_Newpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                checkUserStatus();
            }
        });

    }



    private void checkUserStatus() {
        //get Current User
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String  cpassOne = cpass_one.getEditText().getText().toString().trim();
        String cpassTwo = cpass_two.getEditText().getText().toString().trim();
        if(firebaseUser != null){
            //user is Loggin in
                String phone = firebaseUser.getPhoneNumber();
            Toast.makeText(getApplicationContext(),"sadwasdad = "+phone ,Toast.LENGTH_LONG).show();;
        }else{
                //user is logged out
                   startActivity(new Intent(changePassword.this,forgetpass_confirmation.class));
                   finish();
        }
    }
}