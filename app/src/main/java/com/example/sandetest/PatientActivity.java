package com.example.sandetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientActivity extends Activity {

    private SearchView searchView;
    private DBUtil dbUtil = new DBUtil();
    private ListView listView;
    private TextView it_id, it_name, it_phone, it_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        searchView = findViewById(R.id.searchView);
        //1. 找到listview对象
        listView = findViewById(R.id.lv_main);

        it_id = findViewById(R.id.it_id);
        it_name = findViewById(R.id.it_name);
        it_phone = findViewById(R.id.it_phone);
        it_date = findViewById(R.id.it_date);

        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //handle 接受消息进行处理
            Handler handler = new Handler(){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    if(msg.what == 12){
                        //2.数据源
                        String [] usersInfo = (String[]) msg.obj;
                        int size = usersInfo.length;
                        System.out.println(size + "数据源");
//                        System.out.println(usersInfo[0] + "数据源");
//                        System.out.println(usersInfo[1] + "数据源");
                        if(size == 0)
                            Toast.makeText(PatientActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                        else {
                            //发送信息到报告页面
//                            Intent intent = new Intent(PatientActivity.this, ReportActivity.class);
                            //从Patient 页面 跳到 Report 页面 发送一个信号标志
//                            intent.putExtra("patientToReport", 3);
//                            intent.putExtra("usersInfo", usersInfo);
//                            startActivity(intent);
                            //3.设置适配器
//                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                                    PatientActivity.this,
//                                    android.R.layout.simple_list_item_1,
////                                    R.layout.item_1,
//                                    usersInfo
//                            );

                            // SimpleAdapter简单适配器
                             final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                            for(int i = 0; i < usersInfo.length; i += 4)
                            {
                                HashMap<String, String>hashMap = new HashMap<String, String>();
                                hashMap.put("it_id", usersInfo[i]);
                                hashMap.put("it_name", usersInfo[i + 1]);
                                hashMap.put("it_phone", usersInfo[i + 2]);
                                hashMap.put("it_date", usersInfo[i + 3]);
                                list.add(hashMap);
                            }
                            SimpleAdapter adapter = new SimpleAdapter(
                                    PatientActivity.this,
                                    list,
                                    R.layout.item_1,
                                    new String[] {"it_id", "it_name", "it_phone", "it_date"},
                                    new int[] {R.id.it_id, R.id.it_name, R.id.it_phone, R.id.it_date}
                            );


                            //4.关联适配器
                            listView.setAdapter(adapter);
                            //listview点击事件
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                    Toast.makeText(PatientActivity.this, "点击了" + position + list.get(position).get("it_id"), Toast.LENGTH_SHORT).show();
                                    //为了更加鲁棒性，弹出对话框进行确定
                                    AlertDialog.Builder dialogUidInfo = new AlertDialog.Builder(PatientActivity.this);
                                    dialogUidInfo.setMessage("要查询当前用户的诊断报告吗?");
                                    dialogUidInfo.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            selectUidInfo(list.get(position).get("it_id")); //传入Uid进行搜索方法
                                        }
                                    });
                                    dialogUidInfo.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(PatientActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    dialogUidInfo.show();
                                }
                            });


//                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    String name = (String) parent.getItemAtPosition(position);
//                                    Toast.makeText(PatientActivity.this, "点击了" + name + "=" + position + "=" + id , Toast.LENGTH_SHORT).show();
//                                }
//                            });
                        }
                    }
                    else if(msg.what == 13){ //根据id开始查询病人信息,下面处理返回的信息
                        //id数据
                        String [] idUserInfo = (String[]) msg.obj;
                        Intent intent = new Intent(PatientActivity.this, ReportActivity.class);
                        intent.putExtra("patientToReport", 3);
                        intent.putExtra("idUserInfo", idUserInfo);
                        startActivity(intent);

                    }
                }
            };

            private void selectUidInfo(final String Uid) {
                System.out.println(Uid + "根据id开始查询病人信息啦");
                new Thread(){
                    @Override
                    public void run() {
                        String [] getUidInfo = dbUtil.selectUidUserInfo(Uid);
                        Message message = new Message();
                        message.obj = getUidInfo;
                        message.what = 13;
                        handler.sendMessage(message);
                    }
                }.start();
            }


            @Override
            public boolean onQueryTextSubmit(final String query) {
                if(isNumber(query)) {
                    System.out.println("是数字");
                    System.out.println(query + "=>=>=>=>=>=>=>=>根据手机开始搜索");
                    new Thread(){
                        @Override
                        public void run() {
                            String [] getOneUser = dbUtil.selectPhoneDate(query);
                            Message message = new Message();
                            message.obj = getOneUser;
                            message.what = 12;
                            handler.sendMessage(message);
                        }
                    }.start();
                }
                else {
                    System.out.println("不是数字");
                    //软键盘的搜索按钮点击事件的监听 下面根据姓名搜索
                    System.out.println(query + "=>=>=>=>=>=>=>=>根据名字开始搜索");
                    new Thread(){
                        @Override
                        public void run() {
                            String [] getOneUser = dbUtil.selectUserNamePhoneDate(query);
                            Message message = new Message();
                            message.obj = getOneUser;
                            message.what = 12;
                            handler.sendMessage(message);
                        }
                    }.start();
                }

                return false;
            }

            private boolean isNumber(String str) {
                for(int i = 0; i < str.length(); i ++)
                {
                    if(!Character.isDigit(str.charAt(i)))
                        return false;
                }
                return true;
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
