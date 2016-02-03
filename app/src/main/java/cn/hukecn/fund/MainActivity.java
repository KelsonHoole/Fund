package cn.hukecn.fund;
    import android.content.Intent;
    import android.graphics.Color;
    import android.os.Message;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CompoundButton;
    import android.widget.ListView;
    import android.widget.Switch;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.loopj.android.http.AsyncHttpClient;
    import com.loopj.android.http.AsyncHttpResponseHandler;
    import com.loopj.android.http.ResponseHandlerInterface;

    import java.util.Calendar;
    import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncHttp.HttpListener{

    TextView tv_income = null,tv_info1,tv_info2,
            tv_info1_1,tv_info1_2,tv_info1_3;
    TextView tv_info2_1,tv_info2_2,tv_info2_3;
    //BaseNetWork baseNetWork = null;
    List<InsertBDBean> list = null;
    boolean notify = true;

    Switch autoFresh = null;
    float income = 0f;
    long current = 0;
    ListView lv_info;
    FundDetailAdapter adapter;
    Calendar calendar = null;
    boolean auto = false;
    Button btn_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance() ;

        //baseNetWork = BaseNetWork.getInstance(this);

        btn_setting = (Button) findViewById(R.id.btn_setting);
        tv_income = (TextView) findViewById(R.id.tv_income);
        lv_info = (ListView) findViewById(R.id.lv_info);
        tv_info1 = (TextView) findViewById(R.id.tv_info1);
        tv_info2 = (TextView) findViewById(R.id.tv_info2);

        tv_info1_1 = (TextView) findViewById(R.id.tv_info1_1);
        tv_info1_2 = (TextView) findViewById(R.id.tv_info1_2);
        tv_info1_3 = (TextView) findViewById(R.id.tv_info1_3);

        tv_info2_1 = (TextView) findViewById(R.id.tv_info2_1);
        tv_info2_2 = (TextView) findViewById(R.id.tv_info2_2);
        tv_info2_3 = (TextView) findViewById(R.id.tv_info2_3);

        autoFresh = (Switch) findViewById(R.id.autofresh);
        autoFresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(getApplicationContext(),isChecked+"",Toast.LENGTH_SHORT).show();
                auto = isChecked;
                if(isChecked)
                    handler.sendEmptyMessage(1);
            }
        });

        adapter = new FundDetailAdapter(this,tv_income);
        lv_info.setAdapter(adapter);

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        //refresh();
    }

    public void ChangeView(FundBean bean) {

        String str_num;
        float this_percent;
        if(bean.pecent_value.charAt(0) == '-')
        {
            int start = 1;
            int end = bean.pecent_value.indexOf('%');
            str_num = bean.pecent_value.substring(start,end);
            this_percent = Float.valueOf(str_num) * -0.01f;
        }else
        {
            int start = 0;
            int end = bean.pecent_value.indexOf('%');
            str_num = bean.pecent_value.substring(start,end);
            this_percent = Float.valueOf(str_num) * 0.01f;
        }

        this_percent = Math.round(this_percent * 10000);
        this_percent = this_percent/10000.0f;
        MyDataBase db = new MyDataBase(MainActivity.this);
        float money = db.quary(bean.id);
        float this_income = this_percent * money;
        this_income = Math.round(this_income*100);
        this_income = this_income/100.0f;
        income += this_income;

        income = Math.round(income*100);
        income = income / 100.0f;
        tv_income.setText(income + "");
        if(income < 0.00)
        {
            tv_income.setTextColor(Color.GREEN);
        }else
        {
            tv_income.setTextColor(Color.RED);
        }

        int location = isRepeat(bean);
        if(location == -1)
        {
            //原列表中没有
            adapter.append(bean);
        }else
        {
            //原列表中有
            adapter.getList().set(location,bean);
            adapter.notifyDataSetChanged();
        }


    }

    private void refresh(){
       // if(baseNetWork == null)
       //     return;
        getLatestInfo();
        income = 0f;
        MyDataBase db = new MyDataBase(MainActivity.this);
        list = db.query();

        if ((list != null && list.size() != 0)) {
            handler.sendEmptyMessageDelayed(0, 6000);
            for (InsertBDBean bean : list)
            {
                //baseNetWork.getFundInfo(bean.fundid, this);
                MyHttp.get(MainActivity.this,AppConfig.BASEURL+bean.fundid+".js?rt="+System.currentTimeMillis(),this);
            }
        }else{
            if(notify){
                Toast.makeText(getApplicationContext(),"打开顶部开关可以定时刷新，右边设置按钮可以添加基金",Toast.LENGTH_LONG).show();
                notify = false;
            }else if(auto)
            {
                Toast.makeText(getApplicationContext(),"请先添加基金，然后将为您自动刷新",Toast.LENGTH_LONG).show();
                autoFresh.setChecked(false);
                auto = false;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - current > 2000)
        {
            current = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
        }else
        {
            //baseNetWork.poolShutDown();
            //baseNetWork = null;
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    protected void onResume() {
        if(!auto)
        {
            refresh();
        }
        super.onResume();
    }

    private  android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1 && auto)
            {
                refresh();
                this.sendEmptyMessageDelayed(1,5000);
            }
        }
    };

    @Override
    public void onHttpCallBack(int statusCode, String content) {
        if(statusCode == 200)
        {
            FundBean bean = BaseTools.praseJsonToFundBean(content);
            if(bean != null)
                ChangeView(bean);
        }
    }

    public void getLatestInfo(){
//        baseNetWork.getLatestIndex(new AsyncHttp.HttpListener() {
//            @Override
//            public void onHttpCallBack(int statusCode, String content) {
//                if(statusCode == 200)
//                {
//                    setIndexTextView(content);
//                }
//            }
//        });
        MyHttp.get(MainActivity.this, AppConfig.LATESTINDEXURL, new AsyncHttp.HttpListener() {
            @Override
            public void onHttpCallBack(int statusCode, String content) {
                if(statusCode == 200)
                {
                    setIndexTextView(content);
                }
            }
        });
    }

    private void setIndexTextView(String content) {

        String indexStr;
        int start = content.indexOf(',')+1;
        int end = content.indexOf(',',start+1);
        indexStr = content.substring(start,end);//3539.04

        double temp = Double.parseDouble(indexStr);
        tv_info1_1.setText(String.format("%.2f",temp));

        start = end+1;
        end = content.indexOf(',',start);
        indexStr = content.substring(start,end);//14.051
        temp = Double.parseDouble(indexStr);
        tv_info1_2.setText(String.format("%.2f",temp));

        start = end+1;
        end = content.indexOf(',',start);
        indexStr = content.substring(start,end)+'%';//0.40%
        tv_info1_3.setText(indexStr);

        if(indexStr.indexOf('-') == -1)
        {
            tv_info1_1.setTextColor(Color.RED);
            tv_info1_2.setTextColor(Color.RED);
            tv_info1_3.setTextColor(Color.RED);
        }
        else
        {
            tv_info1_1.setTextColor(Color.GREEN);
            tv_info1_2.setTextColor(Color.GREEN);
            tv_info1_3.setTextColor(Color.GREEN);
        }

        start = content.indexOf("创业板指") + 5;
        end = content.indexOf(',',start);
        indexStr = content.substring(start,end);//2746.49
        temp = Double.parseDouble(indexStr);
        tv_info2_1.setText(String.format("%.2f",temp));

        start = end+1;
        end = content.indexOf(',',start);
        indexStr = content.substring(start,end);//54.333
        temp = Double.parseDouble(indexStr);
        tv_info2_2.setText(String.format("%.2f",temp));

        start = end+1;
        end = content.indexOf(',',start);
        indexStr = content.substring(start,end)+'%';//2.02%
        tv_info2_3.setText(indexStr);

        if(indexStr.indexOf('-') == -1)
        {
            tv_info2_1.setTextColor(Color.RED);
            tv_info2_2.setTextColor(Color.RED);
            tv_info2_3.setTextColor(Color.RED);
        }
        else
        {
            tv_info2_1.setTextColor(Color.GREEN);
            tv_info2_2.setTextColor(Color.GREEN);
            tv_info2_3.setTextColor(Color.GREEN);
        }
    }

    public int isRepeat(FundBean bean){
        List<FundBean> list = adapter.getList();
        for(int i = 0;i < list.size();i++)
        {
            if(list.get(i).id.equals(bean.id))
                return i;
        }
        return -1;
    }
}
