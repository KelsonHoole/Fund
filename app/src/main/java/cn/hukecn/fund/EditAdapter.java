package cn.hukecn.fund;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Kelson on 2015/11/10.
 */
public class EditAdapter extends BaseAdapter {
    List<InsertBDBean> list = null;
    Context context = null;

    public EditAdapter(List<InsertBDBean> list, Context context) {
        this.list = list;
        this.context = context;
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

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.edit_list_item, null);
            holder = new ViewHolder();
            holder.tv_fundname = (TextView) convertView.findViewById(R.id.tv_fundname);
            holder.btn_delete = (Button) convertView.findViewById(R.id.btn_delete);
            holder.tv_fundid = (TextView) convertView.findViewById(R.id.tv_fundid);
            holder.tv_fundmoney = (TextView) convertView.findViewById(R.id.tv_fundmoney);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        bindView(position, holder);
        return convertView;
    }

    public void bindView(final int position, ViewHolder holder) {
        InsertBDBean bean = (InsertBDBean) getItem(position);
        holder.tv_fundname.setText(bean.fundname);
        holder.tv_fundid.setText(bean.fundid+".OF");
        holder.tv_fundmoney.setText("确认金额："+bean.money);
    }

    private class ViewHolder {
        public TextView tv_fundname;
        public Button btn_delete;
        public TextView tv_fundid,tv_fundmoney;
    }

    public void deleteItem(int position){
        InsertBDBean bean = (InsertBDBean) getItem(position);
        MyDataBase db = new MyDataBase(context);
        db.delete(bean.fundid);
        list.remove(position);
        notifyDataSetChanged();
    }
}
