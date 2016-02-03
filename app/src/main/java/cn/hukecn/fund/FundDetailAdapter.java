package cn.hukecn.fund;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelson on 2015/11/11.
 */
public class FundDetailAdapter extends BaseAdapter{
    private final Context context;
    private List<FundBean> list = null;
    View parentView = null;
    public FundDetailAdapter(Context context,View parentView){
        this.context = context;
        this.parentView = parentView;
        list = new ArrayList<>();
    }

    public List<FundBean> getList(){
        return list;
    }
    public void append(FundBean bean){


        list.add(bean);
        notifyDataSetChanged();
    }

    public void clear()
    {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.fund_detail_item,parent,false);
            holder.tv_fundname = (TextView) convertView.findViewById(R.id.tv_fundname);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.tv_fundpz = (TextView) convertView.findViewById(R.id.tv_fundpz);
            holder.tv_income = (TextView) convertView.findViewById(R.id.tv_income);

            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //popWindow(position);
                Intent intent = new Intent(context,HistoryNetActivity.class);
                intent.putExtra("fundid",((FundBean)getItem(position)).id);
                intent.putExtra("fundname",((FundBean)getItem(position)).name);
                context.startActivity(intent);
                return;
            }
        });

//        convertView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//            }
//        });

        bindView(position,holder);
        return convertView;
    }

    private void bindView(int position, ViewHolder holder) {
        FundBean bean = (FundBean) getItem(position);

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

        MyDataBase db = new MyDataBase(context);
        float money = db.quary(bean.id);
        float this_income = this_percent * money;
        this_income = Math.round(this_income*100);
        this_income = this_income/100.0f;

        if(this_income < 0.00)
        {
            holder.tv_income.setTextColor(Color.GREEN);
            holder.tv_fundpz.setTextColor(Color.GREEN);
        }else
        {
            holder.tv_income.setTextColor(Color.RED);
            holder.tv_fundpz.setTextColor(Color.RED);
        }

        holder.tv_income.setText(this_income+"");
        holder.tv_fundpz.setText(bean.pecent_value);
        holder.tv_fundname.setText(bean.name);
        //holder.tv_money.setText(money+"");
        holder.tv_money.setText("预计今日收益：");
    }

    private class ViewHolder{
        TextView tv_fundname;
        TextView tv_money;
        TextView tv_income;
        TextView tv_fundpz;
    }

    public void popWindow(int position) {
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra("fundid",((FundBean)getItem(position)).id);
        intent.putExtra("fundname",((FundBean)getItem(position)).name);
        context.startActivity(intent);
    }
}
