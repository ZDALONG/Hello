package cn.edu.swufe.cheng.hello;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class MyList2Activity extends ListActivity implements Runnable,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private String TAG = "mylist2";
    Handler handler;
    private List<HashMap<String, String>> listTtems;//存放文字，图片信息
    private SimpleAdapter listItemAdapter;//适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListview();

        //MyAdapter myAdapter = new MyAdapter(this,R.id.list_item,listTtems);
        //this.setListAdapter(myAdapter);
        this.setListAdapter(listItemAdapter);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 7) {
                    listTtems = (List<HashMap<String, String>>) msg.obj;
                    listItemAdapter = new SimpleAdapter(MyList2Activity.this, listTtems,//ListItems 数据源
                            R.layout.list_item,//ListItem的XML布局实现
                            new String[]{"ItemTitle", "ItemDetail"},
                            new int[]{R.id.itemTitle, R.id.itemDetail}//一一匹配关系，ItemTitle的数据放入itemTitle布局里，同理
                    );
                    setListAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);

    }

    private void initListview() {
        listTtems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate: " + i);//标题文字
            map.put("ItemDetail", "detail" + i);//详情描述
            listTtems.add(map);//往list列表添加map对象
        }
        //生成适配器的 Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listTtems,//ListItems 数据源
                R.layout.list_item,//ListItem的XML布局实现
                new String[]{"ItemTitle", "ItemDetail"},
                new int[]{R.id.itemTitle, R.id.itemDetail}//实现一一匹配关系，ItemTitle的数据放入itemTitle布局里，同理itemDetail
        );
    }

    public void run() {
        //获取网络数据，放入list，带回到主线程中
        List<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();
        Document doc = null;
        try {
            Thread.sleep(3000);
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            Log.i(TAG, "run: " + doc.title());
            Elements tables = doc.getElementsByTag("table");

            Element table1 = tables.get(1);//第一个table为0，取第二个
            //获取TD中的数据
            Elements tds = table1.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i += 8) {
                Element td1 = tds.get(i);//获取到第一列的数据:国家名字
                Element td2 = tds.get(i + 5);//获取第六列的数据：汇率

                String str1 = td1.text();
                String val = td2.text();

                Log.i(TAG, "run: " + str1 + "==>" + val);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", str1);
                map.put("ItemDetail", val);
                retList.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //获取Msg对象，用于返回
        Message msg = handler.obtainMessage(7);
        msg.obj = retList;
        handler.sendMessage(msg);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.i(TAG,"onItemClick: parent="+parent);
        Log.i(TAG,"onItemClick: view="+view);
        Log.i(TAG,"onItemClick: position="+position);
        Log.i(TAG,"onItemClick: id="+id);

        HashMap<String,String>map = (HashMap<String, String>) getListView().getItemAtPosition(position);
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Log.i(TAG,"onItemClick: titleStr="+titleStr);
        Log.i(TAG,"onItemClick: detailStr="+detailStr);

        TextView title = (TextView)view.findViewById(R.id.itemTitle);
        TextView detail = (TextView)view.findViewById(R.id.itemDetail);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());
        Log.i(TAG,"onItemClick: title2= "+title2);
        Log.i(TAG,"onItemClick: detail2= "+detail2);

        //打开新的页面传入参数
        Intent rateCalc = new Intent(this,RateCalcActivity.class);
        rateCalc.putExtra("title",titleStr);
        rateCalc.putExtra("rate",Float.parseFloat(detailStr));
        startActivity(rateCalc);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG,"onItemLongClick: 长按列表项position= "+ position);
        //删除操作
        //listTtems.remove(position);
        //listItemAdapter.notifyDataSetChanged();

        //构造对话框进行确认操作
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请设置是否删除数据").setPositiveButton("是",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG,"onClick: 对话框事件处理");
                listTtems.remove(position);
                listItemAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("否",null);
        builder.create().show();


        Log.i(TAG,"onItemLongClick: size="+listTtems.size());
        return true;//false表示短按事件依然可以生效
    }
}
