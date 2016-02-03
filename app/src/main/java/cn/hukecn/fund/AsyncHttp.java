package cn.hukecn.fund;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kelson on 2015/12/5.
 */
public class AsyncHttp implements Runnable {

    public int COUNT = 0;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(listener != null)
                listener.onHttpCallBack(msg.getData().getInt("code")
                        ,msg.getData().getString("content"));
        }
    };
    String strUrl = null;
    HttpListener listener = null;
    public AsyncHttp(String url,HttpListener listener){
        this.strUrl = url;
        this.listener = listener;
    }

    private void HttpGet() {
        try {
            COUNT++;
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1000);
            conn.setDoInput(true);

            InputStreamReader in = new InputStreamReader(conn.getInputStream(),"gbk");
            BufferedReader buffer = new BufferedReader(in);
            String inputLine = null;
            String result = "";

            while ((inputLine = buffer.readLine()) != null)
            {
                result += inputLine;
            }

            if(result.length() >0)
            {
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("content",result);
                bundle.putInt("code",conn.getResponseCode());
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            in.close();
            conn.disconnect();

            if(result.length() <= 0 && COUNT < 3)
                HttpGet();

        } catch (MalformedURLException e) {
            if(COUNT < 3)
                HttpGet();
        } catch (IOException e) {
            if(COUNT < 3)
               HttpGet();
        }
    }

    @Override
    public void run() {
        HttpGet();
    }

    public interface HttpListener{
        public void onHttpCallBack(int statusCode, String content);
    }
}
