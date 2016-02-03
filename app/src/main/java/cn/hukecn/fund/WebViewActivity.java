package cn.hukecn.fund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    WebView view = null;
    ProgressBar bar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String fundid = intent.getStringExtra("fundid");
        String fundname = intent.getStringExtra("fundname");
        if(fundid == null || fundname == null)
        {
            Toast.makeText(getApplicationContext(),"System Error...",Toast.LENGTH_LONG).show();
            return;
        }
        TextView tv_title = (TextView) findViewById(R.id.title);
        tv_title.setText(fundname+"("+fundid+")");
        view = (WebView) findViewById(R.id.webview);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setProgress(0);
        view.loadUrl("http://m.1234567.com.cn/m/fund/funddetail/#fundcode="+fundid);

        view.getSettings().setJavaScriptEnabled(true);
        view.setWebViewClient(webViewClient);
        view.setWebChromeClient(webChromeClient);
    }

    public WebViewClient webViewClient = new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    public WebChromeClient webChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress != 100)
                bar.setProgress(newProgress);
            else
                bar.setVisibility(View.GONE);

            super.onProgressChanged(view, newProgress);
        }


    };
}
