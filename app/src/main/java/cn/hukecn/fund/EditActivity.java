package cn.hukecn.fund;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class EditActivity extends AppCompatActivity {

    ListView lv;
    List<InsertBDBean> list = null;
    EditAdapter adapter = null;
    Button btn_add = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        lv = (ListView) findViewById(R.id.edit_list);


        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        //lv.removeAllViews();
        adapter = null;
        MyDataBase db = new MyDataBase(EditActivity.this);
        list = db.query();
        if(list !=null)
        {
            adapter = new EditAdapter(list, EditActivity.this);
            lv.setAdapter(adapter);
            Toast.makeText(getApplicationContext(),"列表长按可以删除基金...",Toast.LENGTH_LONG).show();

            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog alert = new AlertDialog.Builder(EditActivity.this).create();
                    alert.setIcon(R.drawable.icon);
                    alert.setTitle("删除？");
                    alert.setMessage("确定要删除该基金？");
                    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.deleteItem(position);
                        }
                    });

                    alert.show();
                    return true;
                }
            });
        }else
        {
            Toast.makeText(getApplicationContext(),"当前基金列表为空，请添加", Toast.LENGTH_LONG).show();
        }
        super.onResume();
    }
}
