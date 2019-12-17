package com.example.sandetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class DBUtil{

    private String loginIsSuccess;

    //定义SoapAction与命名空间,作为常量
    private static final String nameSpace = "http://tempuri.org/";
    //url相关参数
//    private static final String url = "http://192.168.6.186:44338/WebService1.asmx";
    private static final String url = "http://165872u96i.imwork.net:16497/WebService1.asmx";
    /**
     * webservice 调用方法各种参数
     */
    //增加一条用户信息
    private static final String addUserMethod = "insertUserInfo";
    private static final String addUserSoapAction = "http://tempuri.org/insertUserInfo";
    //判断用户信息进行登录
    private static final String loginUserMethod = "loginUserInfo";
    private static final String loginUserSoapAction = "http://tempuri.org/loginUserInfo";
    //增加用户诊断结果的信息
    private static final String addDiagnoseMethod = "insertDiagnoseInfo";
    private static final String addDiagnoseSoapAction = "http://tempuri.org/insertDiagnoseInfo";
    //查询最近一次用户诊断信息
    private static final String selectRecentMethod = "selectRecentUserInfo";
    private static final String selectRecentSoapAction = "http://tempuri.org/selectRecentUserInfo";
    //搜索一位病人的诊断信息
    private static final String selectOneMethod = "selectOneUserInfo";
    private static final String selectOneSoapAction = "http://tempuri.org/selectOneUserInfo";
    //判断输入用户是否存在
    private static final String userExistMethod = "userNameIsExist";
    private static final String userExistSoapAction = "http://tempuri.org/userNameIsExist";

    /**
     * 判断输入用户是否存在
     * @param Name
     * @return
     */
    public String userIsExist(String Name){
        SoapObject request = new SoapObject(nameSpace, userExistMethod);
        //设置参数
        request.addProperty("Uname", Name);

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request ;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(url);
        System.out.println("服务设置完毕,准备开启服务");

        try {
            httpTransportSE.call(userExistSoapAction, envelope);
            System.out.println("调用WebService服务成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用WebService服务失败");
        }
        //获得服务返回的数据,并且开始解析
        SoapObject object = (SoapObject) envelope.bodyIn;
        System.out.println("获得服务数据");
        return object.getProperty(0).toString();
    }

    /**
     * 搜索一位病人的诊断信息
     * @param Name 搜索病人姓名
     * @return 返回搜索病人的诊断信息
     */
    public String[] selectOneUserInfo(String Name){
        SoapObject request = new SoapObject(nameSpace, selectOneMethod);

        // 有参数要设置参数！！！！！！！！！！！！！
        request.addProperty("Name", Name);

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request ;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(url);
        System.out.println("服务设置完毕,准备开启服务");

        try {
            httpTransportSE.call(selectOneSoapAction, envelope);
            System.out.println("调用WebService服务成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用WebService服务失败");
        }
        //获得服务返回的数据，并开始解析
        SoapObject object = (SoapObject) envelope.bodyIn;
        SoapObject soapChild = (SoapObject) object.getProperty(0);
        System.out.println("获得服务数据");
        String[] message = new String[30];//初始化定义 长度为20

        for(int i = 0; i < soapChild.getPropertyCount(); i ++)
        {
            String soapChilds = soapChild.getProperty(i).toString();
            message[i] = soapChilds;
        }

//        for(int i = 0; i < message.length; i ++)
//        {
//            System.out.println(message[i] + "+++++++++++++");
//        }
        return message;
    }

    /**
     * 查询最近一次的用户诊断信息
     * @return 返回最近一次用户的诊断信息
     */
    public String[] selectRecentUserInfo(){
        SoapObject request = new SoapObject(nameSpace, selectRecentMethod);

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request ;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(url);
        System.out.println("服务设置完毕,准备开启服务");

        try {
            httpTransportSE.call(selectRecentSoapAction, envelope);
            System.out.println("调用WebService服务成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用WebService服务失败");
        }
        //获得服务返回的数据，并开始解析
        SoapObject object = (SoapObject) envelope.bodyIn;
        SoapObject soapChild = (SoapObject) object.getProperty(0);
        System.out.println("获得服务数据");
        String[] message = new String[20];//初始化定义 长度为20

        for(int i = 0; i < soapChild.getPropertyCount(); i ++)
        {
            String soapChilds = soapChild.getProperty(i).toString();
            message[i] = soapChilds;
        }

//        for(int i = 0; i < message.length; i ++)
//        {
//            System.out.println(message[i]);
//        }

        return message;
    }


    /**
     * 增加用户诊断结果的信息
     * @param Name 姓名
     * @param Sex 性别
     * @param Age 年龄
     * @param Date 诊断日期
     * @param Color1 肺区颜色
     * @param Color2 心区颜色
     * @param Color3 肝胆区颜色
     * @param Color4 脾胃区颜色
     * @param Color5 肾区颜色
     * @param Color6 生殖区颜色
     * @param Color7 其他部位颜色
     * @param OtherParts 其他部位
     * @param Analyze 罐印分析
     * @param Result 诊断结果
     */
    public void addDiagnose(String Name, String Sex, String Age, String Date, String Color1,
                            String Color2, String Color3,String Color4, String Color5, String Color6,
                            String Color7, String OtherParts, String Analyze, String Result)
    {
        SoapObject request = new SoapObject(nameSpace, addDiagnoseMethod);
        //设置参数
        request.addProperty("Name", Name);request.addProperty("Sex", Sex);
        request.addProperty("Age", Age);request.addProperty("Date", Date);
        request.addProperty("Color1", Color1);request.addProperty("Color2", Color2);
        request.addProperty("Color3", Color3);request.addProperty("Color4", Color4);
        request.addProperty("Color5", Color5);request.addProperty("Color6", Color6);
        request.addProperty("Color7", Color7);request.addProperty("OtherParts", OtherParts);
        request.addProperty("Analyze", Analyze);request.addProperty("Result", Result);

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request ;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(url);
        System.out.println("服务设置完毕,准备开启服务");

        try {
            httpTransportSE.call(addDiagnoseSoapAction, envelope);
            System.out.println("调用WebService服务成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用WebService服务失败");
        }
    }

    //插入诊断信息
    public void insertDiagnoseInfo(final String Name, final String Sex, final String Age, final String Date, final String Color1,
                                   final String Color2, final String Color3, final String Color4, final String Color5,
                                   final String Color6, final String Color7, final String OtherParts, final String Analyze, final String Result){
        new Thread(){
            @Override
            public void run() {
                addDiagnose(Name, Sex, Age, Date, Color1, Color2, Color3, Color4, Color5, Color6,
                        Color7, OtherParts, Analyze, Result);
            }
        }.start();
    }


    /**
     * 根据用户名和密码进行登录
     * @param sdname 用户名
     * @param sdpwd 用户密码
     * @return 是否可以登录
     */
    public String loginUser(String sdname, String sdpwd){
        SoapObject request = new SoapObject(nameSpace, loginUserMethod);
        //设置参数
        request.addProperty("Uname", sdname);
        request.addProperty("Upwd", sdpwd);

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request ;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(url);
        System.out.println("服务设置完毕,准备开启服务");

        try {
            httpTransportSE.call(loginUserSoapAction, envelope);
            System.out.println("调用WebService服务成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用WebService服务失败");
        }
        //获得服务返回的数据,并且开始解析
        SoapObject object = (SoapObject) envelope.bodyIn;
        System.out.println("获得服务数据");
//        System.out.println(object.getProperty(0));
//        System.out.println(object.getProperty(0).equals(true) + "11111111111");
//        System.out.println(object.getProperty(0).toString().equals("true") + "222222222222222222");
//        return object.getProperty(0).toString();
        loginIsSuccess = object.getProperty(0).toString();
        Message message = new Message();
        message.obj = loginIsSuccess;
        return loginIsSuccess;
    }


    /**
     * 增加一条用户信息
     * @param sdname 用户名
     * @param sdpwd 用户密码
     */
    public void insertUserInfo(final String sdname, final String sdpwd)
    {
        new Thread(){
            @Override
            public void run() {
                addUser(sdname,sdpwd);
            }
        }.start();
    }

    /**
     * 增加一条用户信息
     * @param sdname 用户姓名
     * @param sdpwd 用户密码
     */
    public void addUser(String sdname, String sdpwd) {
        SoapObject request = new SoapObject(nameSpace, addUserMethod);
        //设置参数
        request.addProperty("Uname", sdname);
        request.addProperty("Upwd", sdpwd);



        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request ;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(url);
        System.out.println("服务设置完毕,准备开启服务");

        try {
            httpTransportSE.call(addUserSoapAction, envelope);
            System.out.println("调用WebService服务成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用WebService服务失败");
        }
    }




    //把webservice服务封装成一个统一调用的方法

    /**
     *
     * @param nameSpace 命名空间
     * @param method 调用的方法
     * @return 返回一个SoapObject类型的 envelope
     */
//    public SoapObject useWebservice(String nameSpace, String method){
//        SoapObject request = new SoapObject(nameSpace, method);
//        //设置参数
//        request.add
//
//    }






}
