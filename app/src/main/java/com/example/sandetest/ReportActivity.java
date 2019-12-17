package com.example.sandetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
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
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ReportActivity extends Activity {

    private ImageView iv_pic1;
    private Button btn_report_savedb, btn_report_pdf, btn_report_edit;
    private TextView tv_report7, tv_report_analyze, tv_report_diagnose, tv_report_treat, tv_report_cases,
            tv_report1,tv_report2,tv_report3,tv_report4,tv_report5,tv_report6;
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
        //从诊断页面进来报告页面，获取信息
        if(diagnoseToReport == 1)
            getInfo();
        //从message页面进入报告，从数据库获取用户诊断数据
        if(messageToReport == 2) {
            getRecentUserInfo();
            System.out.println(et_report1.getText() + "-=-=---=-=************-=-=---=");
            getTreatAndCasesInfo();
            System.out.println(et_report1.getText() + "--=-=-=---=");
            btn_report_savedb.setVisibility(View.INVISIBLE);
            btn_report_edit.setVisibility(View.INVISIBLE);
//            btn_report_edit.setVisibility(View.INVISIBLE);
        }
        //搜索页面进入报告 获取patient 发送的消息 并在报告显示 该病人的诊断信息
        if(patientToReport == 3) {
            getOneUserInfo();
            //中医治疗和调理方案就不存入数据库了，直接根据数据库存的颜色，调用方法采集数据，减少代码复杂度
            //我真是太聪明了=。=
            getTreatAndCasesInfo();
            btn_report_edit.setVisibility(View.INVISIBLE); //不显示，并且不保留所占空间
        }
        System.out.println(et_report1.getText() + "在外面-=-=---=-=************-=-=---=");
    }

    // 初始化方法 findViewById
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
        tv_report_treat = findViewById(R.id.tv_report_treat);
        tv_report_cases = findViewById(R.id.tv_report_cases);

        btn_report_savedb = findViewById(R.id.btn_report_savedb);
        btn_report_pdf = findViewById(R.id.btn_report_pdf);
        btn_report_edit = findViewById(R.id.btn_report_edit);

        tv_report1 = findViewById(R.id.tv_report1);
        tv_report2 = findViewById(R.id.tv_report2);
        tv_report3 = findViewById(R.id.tv_report3);
        tv_report4 = findViewById(R.id.tv_report4);
        tv_report5 = findViewById(R.id.tv_report5);
        tv_report6 = findViewById(R.id.tv_report6);
    }

    //设置监听 点击事件
    private void setListeners() {
        OnClick onClick = new OnClick();
        btn_report_savedb.setOnClickListener(onClick);
        btn_report_pdf.setOnClickListener(onClick);
        btn_report_edit.setOnClickListener(onClick);
    }

    // 根据查询页面 从数据库获取查询用户的诊断信息
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

    //获取最近一次用户诊断信息的 handle 接收消息进行处理
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
                System.out.println(et_report1.getText() + "在里面");

//                for(int i = 0; i < recentUserInfo.length;  i++)
//                    System.out.println(recentUserInfo[i] + "==========");

            }
        }
    };

    //从数据库颜色获取中医治疗原则和调理方案
    private void getTreatAndCasesInfo(){
        //把部位 转换为字符串
        String reportPart1 = tv_report1.getText().toString().substring(0,2);
//        System.out.println(tv_report1.getText().toString() + "-=-=---=-=-=-=---=");
//        System.out.println(tv_report1.getText().toString().substring(0,2) + "-=-=---=-=-=-=---=");
        String reportPart2 = tv_report2.getText().toString().substring(0,2);
        String reportPart3 = tv_report3.getText().toString().substring(0,3);
        String reportPart4 = tv_report4.getText().toString().substring(0,3);
        String reportPart5 = tv_report5.getText().toString().substring(0,2);
        String reportPart6 = tv_report6.getText().toString().substring(0,3);

        String reportColor1 = et_report1.getText().toString();
        String reportColor2 = et_report2.getText().toString();
        String reportColor3 = et_report3.getText().toString();
        String reportColor4 = et_report4.getText().toString();
        String reportColor5 = et_report5.getText().toString();
        String reportColor6 = et_report6.getText().toString();
        System.out.println(et_report1.getText() + "-=-=---=-=-=-=---=");

        //定义中医治疗原则
        String treat = "中医治疗原则：" ;
        treat += "肺:" + getTreat(reportPart1, reportColor1.replace(" ","")) +
                "心:" + getTreat(reportPart2, reportColor2.replace(" ","")) +
                "肝胆:" + getTreat(reportPart3, reportColor3.replace(" ","")) +
                "脾胃:" + getTreat(reportPart4, reportColor4.replace(" ","")) +
                "肾:" + getTreat(reportPart5, reportColor5.replace(" ","")) +
                "生殖:" + getTreat(reportPart6, reportColor6.replace(" ",""));
        tv_report_treat.setText(treat);

        //定义调理方案
        String cases = "调理方案：";
        cases += getCases(reportColor1.replace(" ", ""), reportColor2.replace(" ", ""),
                reportColor3.replace(" ", ""), reportColor4.replace(" ", ""),
                reportColor5.replace(" ", ""), reportColor6.replace(" ", ""));
        tv_report_cases.setText(cases);
    }

    //调用数据库dbUtil.selectRecentUserInfo()函数 获取最近一次用户诊断信息的方法
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

    //从诊断页面进来报告的方法，获取诊断页面填写信息
    private void getInfo() {
        //设置当前时间为诊断结果时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd HH:mm:ss 年月日 时分秒
        Date date = new Date(System.currentTimeMillis());
        et_report_date.setText(simpleDateFormat.format(date));
        //获取名字
        String name = getIntent().getStringExtra("name");
        //获取图片地址
        String userPhoto = getIntent().getStringExtra("userPhotoUri");
        System.out.println(userPhoto + "-----------------");
        Uri userPhotoUri = Uri.parse(userPhoto);
        System.out.println(userPhotoUri + "-----------------");
        //获取颜色
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
        //根据获取的图片设置图片
        iv_pic1.setImageURI(userPhotoUri);
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
        //根据罐印颜色判断病症  replace 替换中间的空格
        analyzeResult += "肺:" + getAnalyze(reportColor1.replace(" ","")) +
                "心:" + getAnalyze(reportColor2.replace(" ","")) +
                "肝胆:" + getAnalyze(reportColor3.replace(" ","")) +
                "脾胃:" + getAnalyze(reportColor4.replace(" ","")) +
                "肾:" + getAnalyze(reportColor5.replace(" ","")) +
                "生殖:" + getAnalyze(reportColor6.replace(" ","")) +
                tv_report7.getText().toString() + ":" + getAnalyze(reportColor7.replace(" ",""));
        tv_report_analyze.setText(analyzeResult);

        //定义诊断结果
        String diagnoseResult = "诊断结果：";
        //根据罐印颜色判断病症
        diagnoseResult += "肺:" + getResult(reportPart1, reportColor1.replace(" ","")) +
                "心:" + getResult(reportPart2, reportColor2.replace(" ","")) +
                "肝胆:" + getResult(reportPart3, reportColor3.replace(" ","")) +
                "脾胃:" + getResult(reportPart4, reportColor4.replace(" ","")) +
                "肾:" + getResult(reportPart5, reportColor5.replace(" ","")) +
                "生殖:" + getResult(reportPart6, reportColor6.replace(" ","")) +
                tv_report7.getText().toString() + ":" + getAnalyze(reportColor7.replace(" ",""));
        tv_report_diagnose.setText(diagnoseResult);

        //定义中医治疗原则
        String treat = "中医治疗原则：" ;
        treat += "肺:" + getTreat(reportPart1, reportColor1.replace(" ","")) +
                "心:" + getTreat(reportPart2, reportColor2.replace(" ","")) +
                "肝胆:" + getTreat(reportPart3, reportColor3.replace(" ","")) +
                "脾胃:" + getTreat(reportPart4, reportColor4.replace(" ","")) +
                "肾:" + getTreat(reportPart5, reportColor5.replace(" ","")) +
                "生殖:" + getTreat(reportPart6, reportColor6.replace(" ",""));
        tv_report_treat.setText(treat);

        //定义调理方案
        String cases = "调理方案：";
        cases += getCases(reportColor1.replace(" ", ""), reportColor2.replace(" ", ""),
                reportColor3.replace(" ", ""), reportColor4.replace(" ", ""),
                reportColor5.replace(" ", ""), reportColor6.replace(" ", ""));
        tv_report_cases.setText(cases);
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

    //中医治疗原则
    private String getTreat(String part, String color){
        String str = "";
        if(part.equals("肺区")) {
            if (color.equals("淡红色"))
                str = "正常。";
            else if (color.equals("鲜红色"))
                str = "滋阴清热、滋阴润肺。";
            else if (color.equals("白色"))
                str = "补益肺气、益气宣肺。";
            else if (color.equals("青色"))
                str = "温肺散寒、温肺化饮。";
            else if (color.equals("紫色"))
                str = "滋阴清肺、清热宣肺。";
            else if (color.equals("紫黑色"))
                str = "疏肝理气、益气宣肺。";
        }
        else if(part.equals("心区")) {
            if (color.equals("淡红色"))
                str = "正常。";
            else if (color.equals("鲜红色"))
                str = "滋阴清火、养阴润燥。";
            else if (color.equals("白色"))
                str = "温阳通络、补心血，温心阳。";
            else if (color.equals("青色"))
                str = "活血化瘀、温阳通脉。";
            else if (color.equals("紫色"))
                str = "活血化瘀、理气通络。";
            else if (color.equals("紫黑色"))
                str = "清热化痰、运化痰浊。";
        }
        else if(part.equals("肝胆区")) {
            if (color.equals("淡红色"))
                str = "正常。";
            else if (color.equals("鲜红色"))
                str = "滋阴清热、疏肝理气。";
            else if (color.equals("白色"))
                str = "养肝补血、疏肝理气。";
            else if (color.equals("青色"))
                str = "祛寒利湿、温经通络。";
            else if (color.equals("紫色"))
                str = "疏肝解郁、行气活血。";
            else if (color.equals("紫黑色"))
                str = "清热利湿、疏肝利胆。";
        }
        else if(part.equals("脾胃区")) {
            if (color.equals("淡红色"))
                str = "正常。";
            else if (color.equals("鲜红色"))
                str = "消食导滞、健脾和胃。";
            else if (color.equals("白色"))
                str = "益气补脾、健脾和胃。";
            else if (color.equals("青色"))
                str = "温中健脾、助运化湿。";
            else if (color.equals("紫色"))
                str = "疏肝理气、健脾和胃、活血化瘀。";
            else if (color.equals("紫黑色"))
                str = "行气健脾，助运化湿。";
        }
        else if(part.equals("肾区")) {
            if (color.equals("淡红色"))
                str = "正常。";
            else if (color.equals("鲜红色"))
                str = "滋阴降火、养阴润燥。";
            else if (color.equals("白色"))
                str = "补肾益气、补益心肾。";
            else if (color.equals("青色"))
                str = "温肾通脉、温阳化湿。";
            else if (color.equals("紫色"))
                str = "清利湿热、疏肝解郁。";
            else if (color.equals("紫黑色"))
                str = "活血化瘀、行气通络。";
        }
        else if(part.equals("生殖区")) {
            if (color.equals("淡红色"))
                str = "正常。";
            else if (color.equals("鲜红色"))
                str = "滋阴降火、养阴润燥。";
            else if (color.equals("白色"))
                str = "温补脾肾、益气和胃。";
            else if (color.equals("青色"))
                str = "温阳化饮、温通经脉。";
            else if (color.equals("紫色"))
                str = "活血化瘀、疏肝理气。";
            else if (color.equals("紫黑色"))
                str = "活血化瘀、清利湿热。";
        }
        return str;
    }

    //调理方案
    private String getCases(String color1, String color2, String color3, String color4, String color5, String color6){
        String str = "";
        String str1 = "1、用电磁吸附罐调理部位为督脉：从长强穴至大椎穴，能量传导由下至上布罐，调理时间为5分钟。" +
                "2、用电磁调理吸附罐调理部位为膀胱经对称顺序（肺俞、心俞、肝俞、脾俞、肾俞、大肠俞）穴位，能量传导由上至下，分别调理分两次时间各5分钟，共计时10分钟。" +
                "3、用电磁吸附罐调理部位为（中脘、气海、天枢、水道）穴位对称布罐，能量传导逆时针方向，调理时间为5分钟。" +
                "4、用电磁罐调理部位为正面下肢（血海、足三里、三阴交）穴位，能量传导由上向下布罐，时间为5分钟。";
        String str2 = "1、用电磁吸附罐调理部位为督脉：从大椎穴至长强穴，能量传导由上至下布罐，调理时间为5分钟。" +
                "2、用电磁调理吸附罐调理部位为膀胱经对称顺序（肺俞、心俞、肝俞、脾俞、肾俞、大肠俞）穴位，能量传导由下至上布罐，分别调理分两次时间各5分钟，共计时10分钟。" +
                "3、用电磁吸附罐调理部位为（中脘、气海、天枢、水道）穴位布罐，能量传导顺时针方向，调理时间为5分钟。" +
                "4、用电磁吸附罐调理部位为下肢膀胱经（殷门、委中、承山）穴位，能量传导由上至下对称顺序分配，时间为5分钟。";
        int case1 = 0, case2 = 0;
        if(color1.equals("白色") || color1.equals("青色"))
            case1 ++;
        else if(color1.equals("淡红色"))
            case1 += 0;
        else //红色 紫色 紫黑色
            case2 ++;
        if(case1 > case2)
            str = str1;
        else
            str = str2;
        return str;
    }

    // 报告页面 底部按钮的点击事件
    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_report_savedb:
                    //为了更加鲁棒性，弹出对话框进行确定
                    AlertDialog.Builder dialogSaveDb = new AlertDialog.Builder(ReportActivity.this);
                    dialogSaveDb.setMessage("要保存当前诊断用户信息吗?");
                    dialogSaveDb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            insertDiagnoseInfo();
                            Toast.makeText(ReportActivity.this, "保存诊断结果...", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialogSaveDb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ReportActivity.this, "取消", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //显示
                    dialogSaveDb.show();
                    break;
                case R.id.btn_report_pdf:
                    //同样 弹出对话框进行选择
                    AlertDialog.Builder dialogPdf = new AlertDialog.Builder(ReportActivity.this);
                    dialogPdf.setMessage("要生成pdf,文件吗?(默认保存到下载文件夹)");
                    dialogPdf.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            generatePdf();
                            Toast.makeText(ReportActivity.this, "生成pdf...", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialogPdf.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ReportActivity.this, "取消", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialogPdf.show();
                    break;
                case R.id.btn_report_edit:
                    intent = new Intent(ReportActivity.this, DiagnoseActivity.class);
                    Toast.makeText(ReportActivity.this, "编辑...", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    break;
            }
        }
    }

    //保存按钮 点击后把当前报告页面信息全部保存到数据库
    private void insertDiagnoseInfo() {
        //如果姓名，性别，年龄，时间为空，提示输入相关信息
        if(et_report_name.length() < 1)
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
        else if(et_report_sex.length() < 1)
            Toast.makeText(this, "请输入性别", Toast.LENGTH_SHORT).show();
        else if(et_report_age.length() < 1)
            Toast.makeText(this, "请输入年龄", Toast.LENGTH_SHORT).show();
        else if(et_report_date.length() < 1)
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

    // 点击生成pdf 生成pdf的方法
    private void generatePdf() {
//        verifyStoragePermissions(ReportActivity);
        PdfDocument document = new PdfDocument();//1.建立PdfDocument
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(linearLayout.getWidth(),linearLayout.getHeight(),1).create();
        PdfDocument.Page page = document.startPage(pageInfo);//2.建立新的page

        //View content = this.findViewById(R.id.linearlayout);
        //content.draw(page.getCanvas());
        linearLayout.draw(page.getCanvas());//3.canvas把当前画面画出来
        document.finishPage(page);


//        String path1 = getApplicationContext().getFilesDir().getAbsolutePath() + "/table11.pdf";
//        String path = getFilesDir().getAbsolutePath() + "/test1.pdf";
//        System.out.println(getApplicationContext());
//        System.out.println(getApplicationContext().getFilesDir());
//        System.out.println(getApplicationContext().getFilesDir().getAbsolutePath());
//        System.out.println(path);
//        System.out.println(path1);
//        String path3 = getFilesDir().getAbsolutePath() + "/Download/test2.pdf";

//        String name = "test3.pdf";
//        File fileDir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "test");
//        if(!fileDir.exists())
//            fileDir.mkdirs();
//        File file = new File(fileDir, name);
//        System.out.println(file + "==========");
        //文件名 当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String dateToFormat = simpleDateFormat.format(date);

        /**
         * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
         */
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }

        String path2 = "/sdcard/Download/" + dateToFormat + ".pdf";
//        String path2 = getApplicationContext().getFilesDir().getAbsolutePath() + dateToFormat + ".pdf";  //模拟器测试
        System.out.println(path2 + "===========");
        File file = new File(path2);

        if(!file.exists()){
//            file.mkdirs();
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
