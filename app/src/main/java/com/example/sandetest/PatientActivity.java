package com.example.sandetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.SearchView;
import android.widget.Toast;

public class PatientActivity extends Activity {

    private SearchView searchView;
    private DBUtil dbUtil = new DBUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        searchView = findViewById(R.id.searchView);

        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //handle 接受消息进行处理
            Handler handler = new Handler(){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    if(msg.what == 12){
                        String [] oneUserInfo = (String[]) msg.obj;
                        System.out.println(oneUserInfo[1]);
                        if(oneUserInfo[1] == null)
                            Toast.makeText(PatientActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                        else {
                            //发送信息到报告页面
                            Intent intent = new Intent(PatientActivity.this, ReportActivity.class);
                            //从Patient 页面 跳到 Report 页面 发送一个信号标志
                            intent.putExtra("patientToReport", 3);
                            intent.putExtra("oneUserInfo", oneUserInfo);
                            startActivity(intent);
                        }
                    }
                }
            };

            @Override
            public boolean onQueryTextSubmit(final String query) {
                //软键盘的搜索按钮点击事件的监听
                System.out.println(query + "==========");
                new Thread(){
                    @Override
                    public void run() {
                        String [] getOneUser = dbUtil.selectOneUserInfo(query);
                        Message message = new Message();
                        message.obj = getOneUser;
                        message.what = 12;
                        handler.sendMessage(message);
                    }
                }.start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //输入内容改变的方法监听
//                System.out.println(newText + "-------------");
                return false;
            }
        });


    }
}
