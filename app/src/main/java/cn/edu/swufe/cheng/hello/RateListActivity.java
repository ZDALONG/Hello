package cn.edu.swufe.cheng.hello;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{
    String data[] = {"wait..."};
    Handler handler;
    private final String TAG = "Rate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list); ListActivity已经包含有布局，需要注释掉
        List<String> list1 = new ArrayList<String>();
        for (int i =1 ;i<100;i++){
            list1.add("item" + i);
        }

        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);//android.R.layout.simple_list_item_1:引用Android平台的简单布局
        setListAdapter(adapter);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==7){
                   List<String> List2 = (List<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,List2);//android.R.layout.simple_list_item_1:引用Android平台的简单布局
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };

    }

    @Override
    public void run() {
        //获取网络数据，放入list，带回到主线程中
        List<String>retList = new ArrayList<String>();
        Document doc = null;
        try {
            Thread.sleep(3000);
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            Log.i(TAG,"run: "+doc.title());
            Elements tables = doc.getElementsByTag("table");

            Element table1 = tables.get(1);//第一个table为0，取第二个
            //获取TD中的数据
            Elements tds = table1.getElementsByTag("td");
            for (int i = 0;i<tds.size();i+=8){
                Element td1 = tds.get(i);//获取到第一列的数据:国家名字
                Element td2 = tds.get(i+5);//获取第六列的数据：汇率

                String str1= td1.text();
                String val = td2.text();

                Log.i(TAG,"run: "+ str1 +"==>"+ val);
                retList.add(str1 + "==>" +val);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //获取Msg对象，用于返回
        Message msg = handler.obtainMessage(7);
        //msg.what = 5;   //arg1,arg2:一个参数，两个参数int   what：用于标记当前msg的属性  obj ：所有对象的父类
        //msg.obj = "Hello from run()";
        msg.obj = retList;
        handler.sendMessage(msg);
    }
}
