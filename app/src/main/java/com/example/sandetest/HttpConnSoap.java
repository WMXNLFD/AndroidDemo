package com.example.sandetest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class HttpConnSoap {
    public ArrayList<String> GetWebServer(String methodName, ArrayList<String> Parameters, ArrayList<String> ParValues)
    {
        ArrayList<String> Values = new ArrayList<String>();

        //ServerUrl是指webservice的url
        //10.0.2.2是让android模拟器访问本地（PC）服务器，不能写成127.0.0.1
        //11125是指端口号，即挂载到IIS上的时候开启的端口
        //Service1.asmx是指提供服务的页面
        String ServerUrl = "https://10.0.2.2:44339/WebService1.asmx";

        String soapAction = "http://tempuri.org/" + methodName;
        String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Body />";

        String tps, vps, ts;
        String mreakString = "";

        mreakString = "<" + methodName + " xmlns=\"http://tempuri.org/\">";
        for (int i = 0; i < Parameters.size(); i++) {
            tps = Parameters.get(i).toString();
            //设置该方法的参数为.net webService中的参数名称
            vps = ParValues.get(i).toString();
            ts = "<" + tps + ">" + vps + "</" + tps + ">";
            mreakString = mreakString + ts;
        }
        mreakString = mreakString + "</" + methodName + ">";
        String soap2 = "</soap:Envelope>";
        String requestData = soap + mreakString + soap2;

        try {
            URL url = new URL(ServerUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            byte[] bytes = requestData.getBytes("utf-8");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(6000);// 设置超时时间
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
            con.setRequestProperty("SOAPAction", soapAction);
            con.setRequestProperty("Content-Length", "" + bytes.length);
            OutputStream outStream = con.getOutputStream();
            outStream.write(bytes);
            outStream.flush();
            outStream.close();
            InputStream inStream = con.getInputStream();

            //data=parser(inStream);
            //System.out.print("11");
            Values = inputStreamtovaluelist(inStream, methodName);
            //System.out.println(Values.size());
            return Values;

        } catch (Exception e) {
            System.out.print("2221");
            return null;
        }



    }

    private ArrayList<String> inputStreamtovaluelist(InputStream in, String MonthsName) throws IOException {
        StringBuffer out = new StringBuffer();
        String s1 = "";
        byte[] b = new byte[4096];
        ArrayList<String> Values = new ArrayList<String>();
        Values.clear();
        for (int n; (n = in.read(b)) != -1;) {
            s1 = new String(b, 0, n);
            out.append(s1);
        }

        System.out.println(out);
        //分割
        String[] s13 = s1.split("><");
        String ifString = MonthsName + "Result";
        String TS = "";
        String vs = "";

        Boolean getValueBoolean = false;
        for (int i = 0; i < s13.length; i++) {
            TS = s13[i];
            System.out.println(TS);
            int j, k, l;
            j = TS.indexOf(ifString); //第一索引位置
            k = TS.lastIndexOf(ifString); //最后索引位置

            if (j >= 0) {
                System.out.println(j);
                if (getValueBoolean == false) {
                    getValueBoolean = true;
                } else {

                }

                if ((j >= 0) && (k > j)) {
                    System.out.println("FFF" + TS.lastIndexOf("/" + ifString));
                    //System.out.println(TS);
                    l = ifString.length() + 1; //长度增加1
                    vs = TS.substring(j + l, k - 2);
                    //System.out.println("fff"+vs);
                    Values.add(vs);
                    System.out.println("退出" + vs);
                    getValueBoolean = false;
                    return Values;
                }
            }
            //查询操作取值结束
            if (TS.lastIndexOf("/" + ifString) >= 0) {
                getValueBoolean = false;
                return Values;
            }
            if ((getValueBoolean) && (TS.lastIndexOf("/" + ifString) < 0) && (j < 0)) {
                k = TS.length();
                //System.out.println(TS);
                vs = TS.substring(7, k - 8);
                //System.out.println("f"+vs);
                Values.add(vs);
            }
        }
        return Values;
    }
}
