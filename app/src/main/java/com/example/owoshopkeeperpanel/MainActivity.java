package com.example.owoshopkeeperpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owoshopkeeperpanel.Model.User_shopkeeper;
import com.example.owoshopkeeperpanel.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    //this is our login activity

    private Button loginButton;
    private EditText mobile, pin;
    private ImageView visibility;
    private Boolean isShowPin = false;
    private ProgressBar progressBar;
    private TextView forgetPin,signUp;
    private CheckBox rememberMe;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Paper.init(this);


        loginButton=(Button)findViewById(R.id.login_btn);
        mobile = (EditText)findViewById(R.id.shopkeeper_mobile);
        pin = (EditText)findViewById(R.id.shopkeeper_pin);
        visibility = findViewById(R.id.show_pin);
        rememberMe=(CheckBox)findViewById(R.id.remember_me);
        forgetPin=(TextView)findViewById(R.id.forget_pin);
        signUp=(TextView)findViewById(R.id.sign_up);
        loadingbar = new ProgressDialog(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterWithOTPActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShowPin) {
                    pin.setTransformationMethod(new PasswordTransformationMethod());
                    visibility.setImageResource(R.drawable.ic_visibility_off);
                    isShowPin = false;

                }else{
                    pin.setTransformationMethod(null);
                    visibility.setImageResource(R.drawable.ic_visibility);
                    isShowPin = true;
                }
            }
        });
    }

    private void LoginUser() {
        String Phone = mobile.getText().toString();
        String Pin = pin.getText().toString();

        if(Phone.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter a phone number", Toast.LENGTH_SHORT).show();
        }
        else if(Pin.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please write your pin", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingbar.setTitle("Login Account");
            loadingbar.setMessage("Please wait while we are checking the credentials....");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            AllowAccessToAccount(Phone, Pin);
        }
    }

    private void AllowAccessToAccount(final String phone, final String pin) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Shopkeeper").child(phone).exists())
                {
                    User_shopkeeper users = dataSnapshot.child("Shopkeeper").child(phone).getValue(User_shopkeeper.class);

                    if(users.getPhone().equals(phone))
                    {
                        if(users.getPin().equals(pin))
                        {

                            if(rememberMe.isChecked())//Writing user data on android storage
                            {
                                Paper.book().write(Prevalent.UserPhoneKey, phone);
                                Paper.book().write(Prevalent.UserPinKey, pin);
                            }

                            Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), AfterRegisterActivity.class);
                            Prevalent.currentOnlineUser = users;
                            startActivity(intent);
                        }

                        else
                        {
                            Toast.makeText(MainActivity.this, "Please enter correct pin", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Account with this phone number do not exists", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
