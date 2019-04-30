package cn.edu.swufe.cheng.hello;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MyListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        ListView listView = findViewById(R.id.mylist);
        String data[]= {"111","222"};

        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);//android.R.layout.simple_list_item_1:引用Android平台的简单布局
        listView.setAdapter(adapter);

    }
}
