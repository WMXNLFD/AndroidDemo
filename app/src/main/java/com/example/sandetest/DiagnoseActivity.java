package com.example.sandetest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiagnoseActivity extends Activity {

    private TextView tv_p1,tv_p2,tv_p3,tv_p4,tv_p5,tv_p6,tv_p7;
    //private EditText et_1,et_2,et_3,et_4,et_5,et_6,et_7; //****************************
    private TextView tv_c1, tv_c2, tv_c3, tv_c4, tv_c5, tv_c6, tv_c7;
    private TextView tv_c1_1, tv_c2_1, tv_c3_1, tv_c4_1, tv_c5_1, tv_c6_1, tv_c7_1;
    private Button btn_decide_color;
    private ImageView iv_user_photo, iv_user_takephoto;
    Uri userPhotoUri, mImageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private File mFilePath;
    private String photoPath;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    boolean userPhotoIsChoose = false; //是否选择患者照片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose);
        init();
        setListeners();
    }

    //点击事件 的设置在这里
    private void setListeners() {
        OnClick onClick = new OnClick();
        //tv_p1.setOnClickListener(onClick);
        //tv_p2.setOnClickListener(onClick);
        //tv_p3.setOnClickListener(onClick);
        //tv_p4.setOnClickListener(onClick);
        //tv_p5.setOnClickListener(onClick);
        //tv_p6.setOnClickListener(onClick);
        tv_p7.setOnClickListener(onClick);
        tv_c1.setOnClickListener(onClick);
        tv_c2.setOnClickListener(onClick);
        tv_c3.setOnClickListener(onClick);
        tv_c4.setOnClickListener(onClick);
        tv_c5.setOnClickListener(onClick);
        tv_c6.setOnClickListener(onClick);
        tv_c7.setOnClickListener(onClick);
        tv_c1_1.setOnClickListener(onClick);
        tv_c2_1.setOnClickListener(onClick);
        tv_c3_1.setOnClickListener(onClick);
        tv_c4_1.setOnClickListener(onClick);
        tv_c5_1.setOnClickListener(onClick);
        tv_c6_1.setOnClickListener(onClick);
        tv_c7_1.setOnClickListener(onClick);
        //确定按钮事件
        btn_decide_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果没选照片，提示选照片
                if(userPhotoIsChoose == false)
                    Toast.makeText(DiagnoseActivity.this, "请选择患者照片!", Toast.LENGTH_SHORT).show();
                else {
                    //获取当前页面的填写信息，传送到报告页面显示笑信息
                    Intent intent = new Intent(DiagnoseActivity.this, ReportActivity.class);
                    //获取名字信息并传送
//                    intent.putExtra("name", et_name.getText().toString());
                    //获取照片uri并传送
                    System.out.println(userPhotoUri + " +++++++++++");
                    intent.putExtra("userPhotoUri", userPhotoUri.toString());
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
                    intent.putExtra("diagnoseColor1", tv_c1.getText().toString());
                    intent.putExtra("diagnoseColor2", tv_c2.getText().toString());
                    intent.putExtra("diagnoseColor3", tv_c3.getText().toString());
                    intent.putExtra("diagnoseColor4", tv_c4.getText().toString());
                    intent.putExtra("diagnoseColor5", tv_c5.getText().toString());
                    intent.putExtra("diagnoseColor6", tv_c6.getText().toString());
                    intent.putExtra("diagnoseColor7", tv_c7.getText().toString());
                    //获取颜色2的信息并传送
                    intent.putExtra("diagnoseColor1_1", tv_c1_1.getText().toString());
                    intent.putExtra("diagnoseColor2_1", tv_c2_1.getText().toString());
                    intent.putExtra("diagnoseColor3_1", tv_c3_1.getText().toString());
                    intent.putExtra("diagnoseColor4_1", tv_c4_1.getText().toString());
                    intent.putExtra("diagnoseColor5_1", tv_c5_1.getText().toString());
                    intent.putExtra("diagnoseColor6_1", tv_c6_1.getText().toString());
                    intent.putExtra("diagnoseColor7_1", tv_c7_1.getText().toString());
                    //从Diagnose 跳到 Report 页面 发送一个信号标志 防止直接点击报告获取不到信息
                    intent.putExtra("diagnoseToReport", 1);
                    startActivity(intent);
                    Toast.makeText(DiagnoseActivity.this, "提交选择的颜色...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //选择本地相册点击事件
        iv_user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开本地相册选择照片
                upLoadPhoto();
                //打开相机进行照相的功能
//                takePhoto();
            }
        });
        //照相功能的点击事件
        iv_user_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开相机进行照相的功能
                takePhoto();
            }
        });

    }

    //打开相机进行照相的功能
    private void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        mFilePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg"); // 指定路径
        mFilePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + timeStamp + ".jpg"); // 指定路径
//        System.out.println(System.currentTimeMillis() + "=====");
//        System.out.println(timeStamp + "=====");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //第二个参数为 包名.fileprovider
            userPhotoUri = FileProvider.getUriForFile(this, "com.example.sandetest.DiagnoseActivity.fileprovider", mFilePath);
        }
        else{
            userPhotoUri = Uri.fromFile(mFilePath);
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, userPhotoUri);
        System.out.println("=====拍照返回图片路径=========:" + userPhotoUri);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            File imageFile = createImageFile();// 创建用来保存照片的文件
//            if(imageFile != null){
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//                    /*7.0以上要通过FileProvider将File转化为Uri*/
////                    mImageUri = FileProvider.getUriForFile(this,FILE_PROVIDER_AUTHORITY,imageFile);
////                    System.out.println("ahahhahahaahah");
//                    mImageUri = Uri.fromFile(imageFile);
//                }
//                else {
//                    mImageUri = Uri.fromFile(imageFile);
//                }
//            }
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,mImageUri);//将用于输出的文件Uri传递给相机
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     * @return 创建的图片文件
     */
    private File createImageFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }


    //打开本地相册选择照片
    private void upLoadPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 10);

    }

    //处理相册和相机 的消息处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK){
            iv_user_photo.setImageURI(data.getData());
            //保存图片uri
            userPhotoUri = data.getData();
            userPhotoIsChoose = true; // 表示已选择照片
            System.out.println(userPhotoUri);
        }
        //获取拍摄图片的结果
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
//            try {
//                /*如果拍照成功，将Uri用BitmapFactory的decodeStream方法转为Bitmap*/
//                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
//                iv_user_photo.setImageBitmap(bitmap);//显示到ImageView上
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoPath = String.valueOf(mFilePath);
            }else{
                photoPath = userPhotoUri.getEncodedPath();
            }
            Log.d("拍照返回图片路径:", photoPath);
            System.out.println("++++++拍照返回图片路径:" + photoPath);
            Uri photoUri = Uri.parse(photoPath);
            iv_user_photo.setImageURI(photoUri);
            userPhotoIsChoose = true; // 表示已选择照片
        }
    }

    private void init() {
        tv_p1 = findViewById(R.id.tv_p1);
        tv_p2 = findViewById(R.id.tv_p2);
        tv_p3 = findViewById(R.id.tv_p3);
        tv_p4 = findViewById(R.id.tv_p4);
        tv_p5 = findViewById(R.id.tv_p5);
        tv_p6 = findViewById(R.id.tv_p6);
        tv_p7 = findViewById(R.id.tv_p7);
        tv_c1 = findViewById(R.id.tv_color1);
        tv_c2 = findViewById(R.id.tv_color2);
        tv_c3 = findViewById(R.id.tv_color3);
        tv_c4 = findViewById(R.id.tv_color4);
        tv_c5 = findViewById(R.id.tv_color5);
        tv_c6 = findViewById(R.id.tv_color6);
        tv_c7 = findViewById(R.id.tv_color7);
        tv_c1_1 = findViewById(R.id.tv_color1_1);
        tv_c2_1 = findViewById(R.id.tv_color2_1);
        tv_c3_1 = findViewById(R.id.tv_color3_1);
        tv_c4_1 = findViewById(R.id.tv_color4_1);
        tv_c5_1 = findViewById(R.id.tv_color5_1);
        tv_c6_1 = findViewById(R.id.tv_color6_1);
        tv_c7_1 = findViewById(R.id.tv_color7_1);
        btn_decide_color = findViewById(R.id.btn_decide_color);
//        et_name = findViewById(R.id.et_name);
        iv_user_photo = findViewById(R.id.iv_user_photo);
        iv_user_takephoto = findViewById(R.id.iv_user_takephoto);
    }

    //选择罐印点击事件
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.tv_p7)
                selectSite();
            else setColor(v.getId());
//            switch (v.getId()) {
//                case R.id.tv_color1:
//                    Toast.makeText(DiagnoseActivity.this, "肺区", Toast.LENGTH_SHORT).show();
//                    setColor(R.id.tv_color1); //选择颜色并显示
//                    break;
//                case R.id.tv_color2:
//                    Toast.makeText(DiagnoseActivity.this, "心区", Toast.LENGTH_SHORT).show();
//                    setColor(R.id.tv_p2);
//                    break;
//                case R.id.tv_p3:
//                    Toast.makeText(DiagnoseActivity.this, "肝胆区", Toast.LENGTH_SHORT).show();
//                    setColor(R.id.tv_p3);
//                    break;
//                case R.id.tv_p4:
//                    Toast.makeText(DiagnoseActivity.this, "脾胃区", Toast.LENGTH_SHORT).show();
//                    setColor(R.id.tv_p4);
//                    break;
//                case R.id.tv_p5:
//                    Toast.makeText(DiagnoseActivity.this, "肾区", Toast.LENGTH_SHORT).show();
//                    setColor(R.id.tv_p5);
//                    break;
//                case R.id.tv_p6:
//                    Toast.makeText(DiagnoseActivity.this, "生殖区", Toast.LENGTH_SHORT).show();
//                    setColor(R.id.tv_p6);
//                    break;
//                case R.id.tv_p7:
//                    Toast.makeText(DiagnoseActivity.this, "其他部位", Toast.LENGTH_SHORT).show();
//                    selectSite(); //选择其他部位
//                    //setColor(R.id.tv_p7);
//                    break;
//            }
        }
    }

    //其他部位点击弹出对话框
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
                //setColor(R.id.tv_p7);
            }
        });
        builder.create().show();
    }

    //选择颜色 弹出对话框
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
                if(tv_pnum == R.id.tv_color1)
                    tv_c1.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color2)
                    tv_c2.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color3)
                    tv_c3.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color4)
                    tv_c4.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color5)
                    tv_c5.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color6)
                    tv_c6.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color7)
                    tv_c7.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color1_1)  //第二颜色
                    tv_c1_1.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color2_1)  //第二颜色
                    tv_c2_1.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color3_1)  //第二颜色
                    tv_c3_1.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color4_1)  //第二颜色
                    tv_c4_1.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color5_1)  //第二颜色
                    tv_c5_1.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color6_1)  //第二颜色
                    tv_c6_1.setText(items[colorNum[0]]);
                else if(tv_pnum == R.id.tv_color7_1)  //第二颜色
                    tv_c7_1.setText(items[colorNum[0]]);
            }
        }); //确定按钮显示及其按键监听器
        builder.create().show();//创建并显示对话框
    }
}
