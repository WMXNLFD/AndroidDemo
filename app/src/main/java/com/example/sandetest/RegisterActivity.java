package com.example.sandetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends Activity {

    private EditText register_name, register_phone, register_password, register_id, register_clinic_name,
    regiser_address, register_host_id;
    private Button register_btn;
    private RadioGroup register_radioGroup;
    private String choose_man = "男";
    private DBUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init(); //初始化
        dbUtil = new DBUtil(); //创建数据库类

        register_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.register_radio_man)
                {
                    choose_man = "男";
                    //System.out.println(choose_man + "radio1");
                }
                else
                {
                    choose_man = "女";
                    //System.out.println(choose_man+ "radio2");
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() { //注册点击事件
            @Override
            public void onClick(View v) {
                if(register_name.getText().toString().isEmpty())
                    Toast.makeText(RegisterActivity.this, R.string.hint_username, Toast.LENGTH_SHORT).show();
                else if(register_password.getText().toString().isEmpty())
                    Toast.makeText(RegisterActivity.this, R.string.hint_password, Toast.LENGTH_SHORT).show();
                else if(register_phone.getText().toString().isEmpty())
                    Toast.makeText(RegisterActivity.this, R.string.hint_phone, Toast.LENGTH_SHORT).show();
                else if(register_id.getText().toString().isEmpty())
                    Toast.makeText(RegisterActivity.this, R.string.hint_id, Toast.LENGTH_SHORT).show();
                else if(register_clinic_name.getText().toString().isEmpty())
                    Toast.makeText(RegisterActivity.this, R.string.hint_clinic_name, Toast.LENGTH_SHORT).show();
                else if(regiser_address.getText().toString().isEmpty())
                    Toast.makeText(RegisterActivity.this, R.string.hint_address, Toast.LENGTH_SHORT).show();
                else if(register_host_id.getText().toString().isEmpty())
                    Toast.makeText(RegisterActivity.this, R.string.hint_host_id, Toast.LENGTH_SHORT).show();
                else registerNewUser(); //System.out.println(choose_man + "====");

            }
        });
    }

    private void registerNewUser() {
        new Thread()
        {
            @Override
            public void run() {
                dbUtil.registerUser(register_phone.getText().toString(), register_password.getText().toString(),
                        register_name.getText().toString(), register_id.getText().toString(), register_clinic_name.getText().toString(),
                        regiser_address.getText().toString(), register_host_id.getText().toString(), choose_man);
            }
        }.start(); //这里要开始啊!
        Toast.makeText(this, "注册成功!", Toast.LENGTH_SHORT).show();
    }

    private void init() {
        register_name = findViewById(R.id.register_name);
        register_phone = findViewById(R.id.register_phone);
        register_password = findViewById(R.id.register_password);
        register_id = findViewById(R.id.register_id);
        register_clinic_name = findViewById(R.id.register_clinic_name);
        regiser_address = findViewById(R.id.register_address);
        register_host_id = findViewById(R.id.register_host_id);
        register_btn = findViewById(R.id.register_btn);
        register_radioGroup = findViewById(R.id.register_radiogroup);
    }
}
