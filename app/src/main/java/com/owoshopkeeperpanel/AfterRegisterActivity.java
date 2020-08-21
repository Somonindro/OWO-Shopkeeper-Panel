package com.example.owoshopkeeperpanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AfterRegisterActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_register);

        button=(Button)findViewById(R.id.create_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AfterRegisterActivity.this,DummyPendingShopActivity.class);
                startActivity(intent);
            }
        });
    }
}
