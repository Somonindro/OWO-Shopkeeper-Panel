package com.shopKPR.login.forgetPin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.shopKPR.R;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneWithOTP extends AppCompatActivity {

    private EditText inputOtp;
    private String mobileNumber;
    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView countDownTimer, resetOtpHeader;
    private Button resendOtp;

    private int counter;

    private ProgressDialog progressDialog;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mAuth = FirebaseAuth.getInstance();

        inputOtp = findViewById(R.id.otp);
        Button continueBtn = findViewById(R.id.nextButton);
        progressBar = findViewById(R.id.progressbar);

        countDownTimer = findViewById(R.id.countDownTimer);
        resetOtpHeader = findViewById(R.id.reset_otp_header);
        resendOtp = findViewById(R.id.resend_otp);

        mobileNumber = getIntent().getStringExtra("mobileNumber");
        verificationId = getIntent().getStringExtra("verificationId");
        forceResendingToken = getIntent().getParcelableExtra("force_resend_token");

        resendOtp.setVisibility(View.GONE);

        new CountDownTimer(65000, 1000){
            public void onTick(long millisUntilFinished)
            {
                countDownTimer.setText(String.valueOf(65 - counter));
                counter++;
            }
            public  void onFinish(){
                resendOtp.setVisibility(View.VISIBLE);
                resetOtpHeader.setVisibility(View.GONE);
                countDownTimer.setVisibility(View.GONE);
                counter = 0;
            }
        }.start();

        continueBtn.setOnClickListener(v ->
        {
            progressBar.setVisibility(View.VISIBLE);
            String code = inputOtp.getText().toString().trim();

            if (code.isEmpty() || code.length() < 6) {
                progressBar.setVisibility(View.GONE);
                inputOtp.setError("Please Enter Code...");
                inputOtp.requestFocus();
                return;
            }
            verifyCode(code);
        });

        resendOtp.setOnClickListener(v -> sendOtpToUser("+88" + mobileNumber));
    }

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task ->
        {
            Intent intent = new Intent(VerifyPhoneWithOTP.this, EnterNewPasswordActivity.class);

            intent.putExtra("mobileNumber", mobileNumber);
            intent.putExtra("verificationId", verificationId);
            intent.putExtra("force_resend_token", forceResendingToken);

            startActivity(intent);
            finish();
        });
    }


    private void sendOtpToUser(String phoneNumberWithCountryCode)
    {
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Send OTP");
        progressDialog.setMessage("Please wait while we are sending you OTP");
        progressDialog.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumberWithCountryCode)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.e("Verify Phone Activity", e.getMessage());
                                Toast.makeText(VerifyPhoneWithOTP.this, "Verification code sending failed, Please try again", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
                            {
                                progressDialog.dismiss();

                                Intent intent = new Intent(VerifyPhoneWithOTP.this, VerifyPhoneWithOTP.class);

                                intent.putExtra("mobileNumber", mobileNumber);
                                intent.putExtra("verificationId", verificationId);
                                intent.putExtra("force_resend_token", forceResendingToken);

                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setForceResendingToken(forceResendingToken)     // ForceResendingToken from callbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}
