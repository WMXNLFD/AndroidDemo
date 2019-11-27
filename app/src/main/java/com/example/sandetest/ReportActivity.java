package com.example.sandetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.Vector;

public class ReportActivity extends Activity {

    private ImageView iv_pic1;
    private Button btn_report_savedb, btn_report_pdf, btn_report_edit;
    private TextView tv_report7, tv_report_analyze, tv_report_diagnose;
    private EditText et_report1, et_report2, et_report3, et_report4, et_report5, et_report6, et_report7,
            et_report_name, et_report_sex, et_report_age, et_report_date;
    private LinearLayout linearLayout;
    private DBUtil dbUtil;
//    DBUtil dbUtil = new DBUtil();//创建数据库类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        init();
        dbUtil = new DBUtil();//创建数据库类
        setListeners();
        //这里有个bug 直接点击报告获取不到信息会出错
        //可以从Diagnose页面过来的发送一个信号，没有信号则不执行下面的getInfo()方法
        int diagnoseToReport = getIntent().getIntExtra("diagnoseToReport", 0);
        //获取从信息页面进入报告的发送消息
        int messageToReport = getIntent().getIntExtra("messageToReport", 0);
        //获取从Patient 页面进入报告的发送消息
        int patientToReport = getIntent().getIntExtra("patientToReport", 0);
        //从诊断信息进来的，获取信息
        if(diagnoseToReport == 1)
            getInfo();
        //从message页面进入报告，从数据库获取用户诊断数据
        if(messageToReport == 2)
            getRecentUserInfo();
        //获取patient 发送的消息 并在报告显示 该病人的诊断信息
        if(patientToReport == 3)
            getOneUserInfo();
    }

    private void init() {
        linearLayout = findViewById(R.id.linearlayout);
        iv_pic1 = findViewById(R.id.iv_pic1);
        et_report_name = findViewById(R.id.et_report_name);
        et_report_sex = findViewById(R.id.et_report_sex);
        et_report_age = findViewById(R.id.et_report_age);
        et_report_date = findViewById(R.id.et_report_date);

        et_report1 = findViewById(R.id.et_report1);
        et_report2 = findViewById(R.id.et_report2);
        et_report3 = findViewById(R.id.et_report3);
        et_report4 = findViewById(R.id.et_report4);
        et_report5 = findViewById(R.id.et_report5);
        et_report6 = findViewById(R.id.et_report6);
        et_report7 = findViewById(R.id.et_report7);
        tv_report7 = findViewById(R.id.tv_report7);
        tv_report_analyze = findViewById(R.id.tv_report_analyze);
        tv_report_diagnose = findViewById(R.id.tv_report_diagnose);

        btn_report_savedb = findViewById(R.id.btn_report_savedb);
        btn_report_pdf = findViewById(R.id.btn_report_pdf);
        btn_report_edit = findViewById(R.id.btn_report_edit);

    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        btn_report_savedb.setOnClickListener(onClick);
        btn_report_pdf.setOnClickListener(onClick);
        btn_report_edit.setOnClickListener(onClick);
    }

    public void getOneUserInfo(){
        String [] oneUserInfo = getIntent().getStringArrayExtra("oneUserInfo");
        //对查询的病人 获取信息后 进行报告的填写
        et_report_name.setText(oneUserInfo[1]);
        et_report_sex.setText(oneUserInfo[2]);
        et_report_age.setText(oneUserInfo[3]);
        et_report_date.setText(oneUserInfo[4]);
        et_report1.setText(oneUserInfo[5]);
        et_report2.setText(oneUserInfo[6]);
        et_report3.setText(oneUserInfo[7]);
        et_report4.setText(oneUserInfo[8]);
        et_report5.setText(oneUserInfo[9]);
        et_report6.setText(oneUserInfo[10]);
        et_report7.setText(oneUserInfo[11]);
        tv_report7.setText(oneUserInfo[12]);
        tv_report_analyze.setText(oneUserInfo[13]);
        tv_report_diagnose.setText(oneUserInfo[14]);
//        for(int i = 0; i < oneUserInfo.length; i ++)
//            System.out.println(oneUserInfo[i] + "=-=-=-=-=");
    }

    //handle 接收消息进行处理
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 11){
                String[] recentUserInfo = (String[]) msg.obj;
                //对获取的最近一次的用户诊断信息进行报告的填写
                et_report_name.setText(recentUserInfo[1]);
                et_report_sex.setText(recentUserInfo[2]);
                et_report_age.setText(recentUserInfo[3]);
                et_report_date.setText(recentUserInfo[4]);
                et_report1.setText(recentUserInfo[5]);
                et_report2.setText(recentUserInfo[6]);
                et_report3.setText(recentUserInfo[7]);
                et_report4.setText(recentUserInfo[8]);
                et_report5.setText(recentUserInfo[9]);
                et_report6.setText(recentUserInfo[10]);
                et_report7.setText(recentUserInfo[11]);
                tv_report7.setText(recentUserInfo[12]);
                tv_report_analyze.setText(recentUserInfo[13]);
                tv_report_diagnose.setText(recentUserInfo[14]);

//                for(int i = 0; i < recentUserInfo.length;  i++)
//                    System.out.println(recentUserInfo[i] + "==========");

            }
        }
    };

    private void getRecentUserInfo() {
        new Thread(){
            @Override
            public void run() {
                String [] getRecentUser = dbUtil.selectRecentUserInfo();
                Message message = new Message();
                message.obj = getRecentUser;
                message.what = 11;
                handler.sendMessage(message);
            }
        }.start();

    }

    private void getInfo() {
        //获取名字
        String name = getIntent().getStringExtra("name");
        //获取yans
        String reportColor1 = getIntent().getStringExtra("diagnoseColor1");
        String reportColor2 = getIntent().getStringExtra("diagnoseColor2");
        String reportColor3 = getIntent().getStringExtra("diagnoseColor3");
        String reportColor4 = getIntent().getStringExtra("diagnoseColor4");
        String reportColor5 = getIntent().getStringExtra("diagnoseColor5");
        String reportColor6 = getIntent().getStringExtra("diagnoseColor6");
        String reportColor7 = getIntent().getStringExtra("diagnoseColor7");
        //获取六大部位
        String reportPart1 = getIntent().getStringExtra("diagnose1");
        String reportPart2 = getIntent().getStringExtra("diagnose2");
        String reportPart3 = getIntent().getStringExtra("diagnose3");
        String reportPart4 = getIntent().getStringExtra("diagnose4");
        String reportPart5 = getIntent().getStringExtra("diagnose5");
        String reportPart6 = getIntent().getStringExtra("diagnose6");
        //获取其他部位
        String reportOther = getIntent().getStringExtra("diagnoseOther");
        //填写名字信息栏
        et_report_name.setText(name);
        //填写其他部位信息栏
        tv_report7.setText(reportOther);
        //填写颜色信息栏
        et_report1.setText(reportColor1);
        et_report2.setText(reportColor2);
        et_report3.setText(reportColor3);
        et_report4.setText(reportColor4);
        et_report5.setText(reportColor5);
        et_report6.setText(reportColor6);
        et_report7.setText(reportColor7);     
        //定义罐印分析结果
        String analyzeResult = "罐印分析：";
        //if(reportColor1.replace(" ","").equals("白色"))
        //根据罐印颜色判断病症
        analyzeResult += "肺区:" + getAnalyze(reportColor1.replace(" ","")) +
                "心区:" + getAnalyze(reportColor2.replace(" ","")) +
                "肝胆区:" + getAnalyze(reportColor3.replace(" ","")) +
                "脾胃区:" + getAnalyze(reportColor4.replace(" ","")) +
                "肾区:" + getAnalyze(reportColor5.replace(" ","")) +
                "生殖区:" + getAnalyze(reportColor6.replace(" ","")) +
                tv_report7.getText().toString() + ":" + getAnalyze(reportColor7.replace(" ",""));
        tv_report_analyze.setText(analyzeResult);

        //定义诊断结果
        String diagnoseResult = "诊断结果：";
        //根据罐印颜色判断病症
        diagnoseResult += "肺区:" + getResult(reportPart1, reportColor1.replace(" ","")) +
                "心区:" + getResult(reportPart2, reportColor2.replace(" ","")) +
                "肝胆区:" + getResult(reportPart3, reportColor3.replace(" ","")) +
                "脾胃区:" + getResult(reportPart4, reportColor4.replace(" ","")) +
                "肾区:" + getResult(reportPart5, reportColor5.replace(" ","")) +
                "生殖区:" + getResult(reportPart6, reportColor6.replace(" ","")) +
                tv_report7.getText().toString() + ":" + getAnalyze(reportColor7.replace(" ",""));
        tv_report_diagnose.setText(diagnoseResult);
    }

    //罐印分析
    private String getAnalyze(String color) {
        String str = "";
        if(color.equals("淡红色"))
            str = "正常。";
        else if(color.equals("鲜红色"))
            str = "阴虚火旺。";
        else if(color.equals("白色"))
            str = "虚症、气血双亏、功能低下。";
        else if(color.equals("青色"))
            str = "寒症、湿症。";
        else if(color.equals("紫色"))
            str = "伴有斑块、表明为瘀症、旧病。";
        else if(color.equals("紫黑色"))
            str = "深黯、表明血瘀、病程已久。";

        return str;
    }

    //诊断结果
    private String getResult(String part,String color) {
        String str = "";
        if(part.equals("肺区")) {
            if (color.equals("淡红色"))
                str = "正常。";
            else if (color.equals("鲜红色"))
                str = "风热犯肺、阴虚火旺。";
            else if (color.equals("白色"))
                str = "肺气虚、肺阴亏耗、肺卫不固。";
            else if (color.equals("青色"))
                str = "痰湿蕴肺、痰浊阻肺、寒饮伏肺。";
            else if (color.equals("紫色"))
                str = "痰热郁肺、风燥伤肺。";
            else if (color.equals("紫黑色"))
                str = "肝火犯肺、肺气郁痹。";
        }
        else if(part.equals("心区")) {
            if (color.equals("淡红色"))
                str = "正常。";
            else if (color.equals("鲜红色"))
                str = "阴虚火旺、心火上炎。";
            else if (color.equals("白色"))
                str = "心虚胆怯、心血不足、心阳不振。";
            else if (color.equals("青色"))
                str = "阴寒凝滞。";
            else if (color.equals("紫色"))
                str = "肝郁化火、心血瘀阻。";
            else if (color.equals("紫黑色"))
                str = "痰热内扰、痰浊壅塞。";
        }
        else if(part.equals("肝胆区")) {
            if (color.equals("淡红色"))
                str = "正常。";
            else if (color.equals("鲜红色"))
                str = "气郁化火、阴虚火旺。";
            else if (color.equals("白色"))
                str = "肝血虚、肝阴不足、心肝阴虚。";
            else if (color.equals("青色"))
                str = "寒湿凝滞。";
            else if (color.equals("紫色"))
                str = "肝气郁结、瘀血停着。";
            else if (color.equals("紫黑色"))
                str = "气滞痰郁、肝胆湿热。";
        }
        else if(part.equals("脾胃区")) {
            if (color.equals("淡红色"))
                str = "正常。";
            else if (color.equals("鲜红色"))
                str = "饮食停滞。";
            else if (color.equals("白色"))
                str = "脾气虚、脾胃阳虚、脾胃虚弱。";
            else if (color.equals("青色"))
                str = "寒邪客胃、脾胃虚寒、痰饮内阻。";
            else if (color.equals("紫色"))
                str = "肝胃郁热、肝气犯胃、气滞血瘀。";
            else if (color.equals("紫黑色"))
                str = "瘀血停滞、津亏热结、湿热壅滞。";
        }
        else if(part.equals("肾区")) {
            if (color.equals("淡红色"))
                str = "正常。";
            else if (color.equals("鲜红色"))
                str = "阴虚火旺。";
            else if (color.equals("白色"))
                str = "肾阳虚、肾阴虚、心肾不交。";
            else if (color.equals("青色"))
                str = "寒湿凝滞。";
            else if (color.equals("紫色"))
                str = "湿热下注、肝郁气滞。";
            else if (color.equals("紫黑色"))
                str = "湿热壅盛 气滞血瘀。";
        }
        else if(part.equals("生殖区")) {
            if (color.equals("淡红色"))
                str = "正常。";
            else if (color.equals("鲜红色"))
                str = "阴虚火旺。";
            else if (color.equals("白色"))
                str = "中气不足、肾阳衰惫。";
            else if (color.equals("青色"))
                str = "寒湿凝滞。";
            else if (color.equals("紫色"))
                str = "肝郁气滞。";
            else if (color.equals("紫黑色"))
                str = "膀胱湿热、气滞血瘀。";
        }

        return str;
    }


    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_report_savedb:
                    insertDiagnoseInfo();
                    Toast.makeText(ReportActivity.this, "保存诊断结果...", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_report_pdf:
                    generatePdf();
                    Toast.makeText(ReportActivity.this, "生成pdf...", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_report_edit:
                    intent = new Intent(ReportActivity.this, DiagnoseActivity.class);
                    Toast.makeText(ReportActivity.this, "编辑...", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    break;
            }
        }
    }

    private void insertDiagnoseInfo() {
        //如果姓名，性别，年龄，时间为空，提示输入相关信息
        if(et_report_name.length() < 1)
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
        else if(et_report_sex.length() < 1)
            Toast.makeText(this, "请输入性别", Toast.LENGTH_SHORT).show();
        else if(et_report_sex.length() < 1)
            Toast.makeText(this, "请输入年龄", Toast.LENGTH_SHORT).show();
        else if(et_report_sex.length() < 1)
            Toast.makeText(this, "请输入诊断时间", Toast.LENGTH_SHORT).show();
        else{
//            dbUtil.insertDiagnoseInfo(et_report_name.getText().toString(), et_report_sex.getText().toString(),
//                            et_report_age.getText().toString(), et_report_date.getText().toString(),
//                            et_report1.getText().toString(), et_report2.getText().toString(),
//                            et_report3.getText().toString(), et_report4.getText().toString(),
//                            et_report5.getText().toString(), et_report6.getText().toString(),
//                            et_report7.getText().toString(), tv_report7.getText().toString(),
//                            tv_report_analyze.getText().toString(), tv_report_diagnose.getText().toString());
//            Toast.makeText(ReportActivity.this, "保存诊断数据成功", Toast.LENGTH_SHORT).show();

            new Thread(){
                @Override
                public void run() {
                    dbUtil.addDiagnose(et_report_name.getText().toString(), et_report_sex.getText().toString(),
                            et_report_age.getText().toString(), et_report_date.getText().toString(),
                            et_report1.getText().toString(), et_report2.getText().toString(),
                            et_report3.getText().toString(), et_report4.getText().toString(),
                            et_report5.getText().toString(), et_report6.getText().toString(),
                            et_report7.getText().toString(), tv_report7.getText().toString(),
                            tv_report_analyze.getText().toString(), tv_report_diagnose.getText().toString());
                }
            }.start();

        }
    }


    //获取主件高度和宽度  注意!!这个方法不能用在onCreate，onStart等启动activity的过程中，因为这个过程中，整个窗口的layout还没有布局到屏幕上。
    //View view = getLayoutInflater().inflate(R.layout.activity_report, null);
    //LinearLayout linearLayout = view.findViewById(R.id.linearlayout);

    /**
     * 生成pdf方法
     */
    private void generatePdf() {
        PdfDocument document = new PdfDocument();//1.建立PdfDocument
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(linearLayout.getWidth(),linearLayout.getHeight(),1).create();
        PdfDocument.Page page = document.startPage(pageInfo);//2.建立新的page

        //View content = this.findViewById(R.id.linearlayout);
        //content.draw(page.getCanvas());
        linearLayout.draw(page.getCanvas());//3.canvas把当前画面画出来
        document.finishPage(page);


        String path = getApplicationContext().getFilesDir().getAbsolutePath() + "/table1.pdf";
        System.out.println(path);
        File file = new File(path);
        if(!file.exists()){
            if(file.mkdirs()){
                System.out.println(1);
            }else
                System.out.println(0);
        }
        if(file.exists()){
            file.delete();
        }
        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
    }
}
