package com.example.sandetest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DiagnoseActivity extends Activity {

    private TextView tv_p1,tv_p2,tv_p3,tv_p4,tv_p5,tv_p6,tv_p7;
    private EditText et_name,et_1,et_2,et_3,et_4,et_5,et_6,et_7;
    private Button btn_decide_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose);
        init();
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        tv_p1.setOnClickListener(onClick);
        tv_p2.setOnClickListener(onClick);
        tv_p3.setOnClickListener(onClick);
        tv_p4.setOnClickListener(onClick);
        tv_p5.setOnClickListener(onClick);
        tv_p6.setOnClickListener(onClick);
        tv_p7.setOnClickListener(onClick);
        btn_decide_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前页面的填写信息，传送到报告页面显示笑信息
                Intent intent = new Intent(DiagnoseActivity.this, ReportActivity.class);
                //获取名字信息并传送
                intent.putExtra("name", et_name.getText().toString());
                //获取六大区域部位信息并传送
                intent.putExtra("diagnose1", tv_p1.getText().toString());
                intent.putExtra("diagnose2", tv_p2.getText().toString());
                intent.putExtra("diagnose3", tv_p3.getText().toString());
                intent.putExtra("diagnose4", tv_p4.getText().toString());
                intent.putExtra("diagnose5", tv_p5.getText().toString());
                intent.putExtra("diagnose6", tv_p6.getText().toString());
                //获取其他部位的选择信息并传送
                intent.putExtra("diagnoseOther", tv_p7.getText().toString());
                //获取颜色信息并传送
                intent.putExtra("diagnoseColor1", et_1.getText().toString());
                intent.putExtra("diagnoseColor2", et_2.getText().toString());
                intent.putExtra("diagnoseColor3", et_3.getText().toString());
                intent.putExtra("diagnoseColor4", et_4.getText().toString());
                intent.putExtra("diagnoseColor5", et_5.getText().toString());
                intent.putExtra("diagnoseColor6", et_6.getText().toString());
                intent.putExtra("diagnoseColor7", et_7.getText().toString());
                //从Diagnose 跳到 Report 页面 发送一个信号标志 防止直接点击报告获取不到信息
                intent.putExtra("diagnoseToReport", 1);
                startActivity(intent);
                Toast.makeText(DiagnoseActivity.this, "提交选择的颜色...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        tv_p1 = findViewById(R.id.tv_p1);
        tv_p2 = findViewById(R.id.tv_p2);
        tv_p3 = findViewById(R.id.tv_p3);
        tv_p4 = findViewById(R.id.tv_p4);
        tv_p5 = findViewById(R.id.tv_p5);
        tv_p6 = findViewById(R.id.tv_p6);
        tv_p7 = findViewById(R.id.tv_p7);
        et_1 = findViewById(R.id.et_1);
        et_2 = findViewById(R.id.et_2);
        et_3 = findViewById(R.id.et_3);
        et_4 = findViewById(R.id.et_4);
        et_5 = findViewById(R.id.et_5);
        et_6 = findViewById(R.id.et_6);
        et_7 = findViewById(R.id.et_7);
        btn_decide_color = findViewById(R.id.btn_decide_color);
        et_name = findViewById(R.id.et_name);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_p1:
                    Toast.makeText(DiagnoseActivity.this, "肺区", Toast.LENGTH_SHORT).show();
                    setColor(R.id.tv_p1); //选择颜色并显示
                    break;
                case R.id.tv_p2:
                    Toast.makeText(DiagnoseActivity.this, "心区", Toast.LENGTH_SHORT).show();
                    setColor(R.id.tv_p2);
                    break;
                case R.id.tv_p3:
                    Toast.makeText(DiagnoseActivity.this, "肝胆区", Toast.LENGTH_SHORT).show();
                    setColor(R.id.tv_p3);
                    break;
                case R.id.tv_p4:
                    Toast.makeText(DiagnoseActivity.this, "脾胃区", Toast.LENGTH_SHORT).show();
                    setColor(R.id.tv_p4);
                    break;
                case R.id.tv_p5:
                    Toast.makeText(DiagnoseActivity.this, "肾区", Toast.LENGTH_SHORT).show();
                    setColor(R.id.tv_p5);
                    break;
                case R.id.tv_p6:
                    Toast.makeText(DiagnoseActivity.this, "生殖区", Toast.LENGTH_SHORT).show();
                    setColor(R.id.tv_p6);
                    break;
                case R.id.tv_p7:
                    Toast.makeText(DiagnoseActivity.this, "其他部位", Toast.LENGTH_SHORT).show();
                    selectSite(); //选择其他部位
                    //setColor(R.id.tv_p7);
                    break;
            }
        }
    }

    private void selectSite() {
        final String[] items = new String[] {"颈部","肩部","腰部","胸肋部","肩关节部","肘关节部","膝关节部","踝关节部","腹部"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DiagnoseActivity.this);
        final int[] selectNum = {0};
        builder.setTitle("请选择部位：");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectNum[0] = which;
                //Toast.makeText(DiagnoseActivity.this, items[which], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //根据选择的部位设置其他部位的名字，并设置颜色
                tv_p7.setText(items[selectNum[0]]);
                setColor(R.id.tv_p7);
            }
        });
        builder.create().show();
    }

    private void setColor(final int tv_pnum) {
        //单选列表对话框
        final String[] items = new String[] {"淡红色","鲜红色","白    色","青    色","紫    色","紫黑色"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DiagnoseActivity.this);
        final int[] colorNum = {0};
        builder.setTitle("请选择颜色：");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                colorNum[0] = which;
                //Toast.makeText(DiagnoseActivity.this, "[" + items[which] + "]", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //根据选择的id 来决定在哪一个et 上填写颜色
                if(tv_pnum == R.id.tv_p1)
                    et_1.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_p2)
                    et_2.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_p3)
                    et_3.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_p4)
                    et_4.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_p5)
                    et_5.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_p6)
                    et_6.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_p7)
                    et_7.setText(items[colorNum[0]]);
            }
        }); //确定按钮显示及其按键监听器
        builder.create().show();//创建并显示对话框
    }
}
