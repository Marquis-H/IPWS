package com.example.http;

/**
 * Created by Marquis on 2015/4/10.
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

/**
 * Created by Marquis on 2014/10/28.
 */
public class GetData {
    //public static final String URL = "http://ipws.sinaapp.com/getdata/index.php";
    private static final int REQUEST_TIMEOUT = 15*1000;   //设置请求超时15s
    private static final int SO_TIMEOUT = 15*1000;       //设置等待数据超时时间15s

    //该方法用于设置超时
    public static HttpClient getHttpClient(){
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams,REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams,SO_TIMEOUT);
        HttpClient client = new DefaultHttpClient(httpParams);
        return client;
    }

    //获取网页源码
    public static String getHtmlCode(String path){
        String result = null;
        try {
            HttpClient httpClient = getHttpClient();
            HttpGet get = new HttpGet(path);
            HttpResponse response =  httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
                String line;
                result = "";
                while ((line = br.readLine())!= null){
                    result += line + "\n";
                }
            }
        }
        catch (ConnectTimeoutException e)
        {
            System.out.println("ConnectTimeoutException timeout");
            return null;
        }
        catch (SocketTimeoutException e)
        {
            System.out.println("SocketTimeoutException timeout");
            return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage() + ":" + e.toString());
            return null;
        }
        return result;

    }
}
