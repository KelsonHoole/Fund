package cn.hukecn.fund;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

/**
 * Created by Kelson on 2016/1/6.
 */
public class MyHttp {

    public static void get(Context context, String url, final AsyncHttp.HttpListener listener){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(1000);
        client.setThreadPool(AppThreadPool.getInstance().getPool());
        client.setResponseTimeout(1000);
        client.get(context, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if(listener != null && bytes != null)
                        listener.onHttpCallBack(i,new String(bytes,"gbk"));
                } catch (UnsupportedEncodingException e) {}
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                //
            }
        });
    }
}
