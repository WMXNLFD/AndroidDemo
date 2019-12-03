package com.example.sandetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedbackActivity extends Activity {

    private Button btn_feedback;
    //记录当前系统时间
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btn_feedback = findViewById(R.id.btn_feedback);
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(System.currentTimeMillis());
                System.out.println(date + "=====");
                System.out.println(simpleDateFormat.format(date) + "=====");
                Toast.makeText(FeedbackActivity.this, "提交反馈信息...", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
