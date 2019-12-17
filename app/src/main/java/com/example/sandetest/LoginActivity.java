package com.example.sandetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends Activity {

    private EditText et_user;
    private EditText et_pass;
    private Button btn_signup;
    private Button btn_login;
    private DBUtil dbUtil;
    private ListView listView;
    private SimpleAdapter adapter;
    private static boolean matchLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        dbUtil = new DBUtil(); //创建数据库类
        setListeners();

    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        btn_signup.setOnClickListener(onClick);
        btn_login.setOnClickListener(onClick);
    }

    private void init() {
        et_user = findViewById(R.id.et_user);
        et_pass = findViewById(R.id.et_passwd);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_signup:
                    //点击注册按钮触发事件
                    //注册用户信息
                    setAddUser();
                    break;
                case R.id.btn_login:
                    //点击登录按钮触发事件
                    //如果登录成功，跳转页面
                    //获取数据
                    setLoginUser();
                    break;
            }
        }
    }



    private void setLoginUser() {
        if(et_user.length() < 1)
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
        else if(et_pass.length() < 1)
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        else {
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    String match = (String) msg.obj;
                    if(match.equals("true")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(LoginActivity.this, "登录失败,用户名或密码错误。", Toast.LENGTH_SHORT).show();
                }
            };

            /**
             * 判断登录信息进行登录
             */
                new Thread(){
                    @Override
                    public void run() {
                        String  match= dbUtil.loginUser(et_user.getText().toString(), et_pass.getText().toString());
                        Message message = new Message();
                        message.obj = match;
                        handler.sendMessage(message);
                    }
                }.start();
            }
    }


    private void setAddUser() {
        if(et_user.length() < 1)
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
        else if(et_pass.length() < 1)
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        else {
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    String exist = (String) msg.obj;
                    if(exist.equals("true")){
                        Toast.makeText(LoginActivity.this, "用户名已存在!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        dbUtil.insertUserInfo(et_user.getText().toString(), et_pass.getText().toString());
                        Toast.makeText(LoginActivity.this, "用户注册成功!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            /**
             * 判断输入用户是否存在
             */
            new Thread(){
                @Override
                public void run() {
                    String exist = dbUtil.userIsExist(et_user.getText().toString());
                    Message message = new Message();
                    message.obj = exist;
                    handler.sendMessage(message);
                }
            }.start();


        }
    }

}


