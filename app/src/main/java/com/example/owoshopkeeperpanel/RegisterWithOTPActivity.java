package com.example.owoshopkeeperpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterWithOTPActivity extends AppCompatActivity {

    private EditText regMobile;
    private Button sendOTP;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_with_o_t_p);

        regMobile = (EditText)findViewById(R.id.shopkeeper_register_mobile);
        sendOTP = (Button)findViewById(R.id.send_otp_btn);
        progress = findViewById(R.id.progress);

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(RegisterWithOTPActivity.this, "Wait for the verification code...", Toast.LENGTH_SHORT).show();
                final String number = regMobile.getText().toString().trim();

                if (number.isEmpty() || number.length() < 11) {
                    regMobile.setError("Please enter a valid number");
                    regMobile.requestFocus();
                    return;
                }

                //For checking existing user in firebase

                progress.setVisibility(View.VISIBLE);

                String phoneNumber = "+" + "88" + number;

                Intent intent = new Intent(RegisterWithOTPActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phonenumber", phoneNumber);
                intent.putExtra("mobilenumber", number);//without +88
                progress.setVisibility(View.INVISIBLE);
                startActivity(intent);

            }
        });

    }
}
