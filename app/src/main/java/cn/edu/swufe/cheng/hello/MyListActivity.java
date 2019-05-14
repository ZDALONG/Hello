package cn.edu.swufe.cheng.hello;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    List<String> data = new ArrayList<String>();
    private String TAG = "MyList";
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        ListView listView = findViewById(R.id.mylist);
        //init data
        for (int i=0;i<10;i++){
            data.add("item" + i);
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);//android.R.layout.simple_list_item_1:引用Android平台的简单布局
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.nodata));//定义列表没有数据时候显示TextView内容
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> listv, View view, int position, long id) {
        Log.i(TAG,"onItemClick: position= "+ position);
        Log.i(TAG,"onItemClick: parent= "+ listv);//parent表示list view这个控件=listv
        adapter.remove(listv.getItemAtPosition(position));
        //adapter.notifyDataSetChanged();//界面刷新
    }
}
