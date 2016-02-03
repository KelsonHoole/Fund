package cn.hukecn.fund;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HistoryNetActivity extends Activity {
    //TextView info = null;
    ListView listView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_net);


        String url = null;
        Intent intent = getIntent();
        if(intent != null)
        {
            String fundid = intent.getStringExtra("fundid");
            String fundname = intent.getStringExtra("fundname");
            if(fundid == null || fundname == null)
            {
                Toast.makeText(getApplicationContext(),"System Error...",Toast.LENGTH_LONG).show();
                return;
            }

            url = "http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code="+fundid+"&page=1&per=20&sdate=&edate=&rt=0.4070765394717455";
        }

        //info = (TextView) findViewById(R.id.info);
        listView = (ListView) findViewById(R.id.lv_info);

        MyHttp.get(this, url,new AsyncHttp.HttpListener() {
            @Override
            public void onHttpCallBack(int statusCode, String content) {
                //info.setText(statusCode+"\n"+content);
                if(statusCode == 200)
                {
                    List<HistoryNetBean> list =praseHtml(content);
                    if (list.size() > 0)
                    {
                        listView.setAdapter(new HistoryNetAdapter(HistoryNetActivity.this,list));
                    }
                }
            }
        });
    }

    private List<HistoryNetBean> praseHtml(String content) {
        List<HistoryNetBean> list = new ArrayList<>();
        String tbody = null;
        int start = -1;
        int end = -1;

        start = content.indexOf("<tbody>");
        end = content.indexOf("</tbody>");
        if(start == -1 || end == -1)
            return list;
        tbody = content.substring(start,end);

        String tr = null;

        int tr_start = 0;
        int tr_end  = 0;
        while(true)
        {
            HistoryNetBean bean = new HistoryNetBean();
            tr_start = tbody.indexOf("<tr>",tr_end);
            tr_end = tbody.indexOf("</tr>",tr_start);
            if(tr_start <= 0 || tr_end <= 0)
                break;

            tr = tbody.substring(tr_start,tr_end);

            start = tr.indexOf("<td>") + 4;
            end = tr.indexOf("</td>");
            if (start ==-1||end == -1)
                break;
            bean.date = tr.substring(start,end);

            start = tr.indexOf("bold")+6;
            end = tr.indexOf("</td>",start);
            if (start ==-1||end == -1)
                break;
            bean.value = tr.substring(start,end);

            start = tr.indexOf("bold",end)+6;
            end = tr.indexOf("</td>",start);

            start = tr.indexOf("bold",end);
            start = tr.indexOf(">",start)+1;
            end = tr.indexOf("</td>",start)-1;
            String strFloat = tr.substring(start,end);
            bean.percent = Float.parseFloat(strFloat);
            list.add(bean);
        }
        return list;
    }


}
