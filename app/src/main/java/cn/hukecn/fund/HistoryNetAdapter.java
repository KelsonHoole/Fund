package cn.hukecn.fund;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by 22916 on 2016.02.01.
 */
public class HistoryNetAdapter extends BaseAdapter{
    Context context = null;
    List<HistoryNetBean> list = null;
    public HistoryNetAdapter(Context context, List<HistoryNetBean> list){
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = View.inflate(context,R.layout.history_net_item,null);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.percent = (TextView) convertView.findViewById(R.id.percent);
            holder.value = (TextView) convertView.findViewById(R.id.value);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        bindView(position,holder);
        return convertView;
    }

    private void bindView(int position, ViewHolder holder) {
        HistoryNetBean bean = (HistoryNetBean) getItem(position);
        holder.date.setText(bean.date);
        holder.value.setText(bean.value);
        if(bean.percent >= 0.00f) {
            holder.percent.setText("+" + bean.percent + "%");
            holder.percent.setTextColor(Color.RED);
        }
        else
        {
            holder.percent.setText(bean.percent+"%");
            holder.percent.setTextColor(Color.GREEN);
        }

    }

    private class ViewHolder{
        TextView date,value,percent;
    }
}
