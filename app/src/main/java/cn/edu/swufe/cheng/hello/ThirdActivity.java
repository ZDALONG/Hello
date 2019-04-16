package cn.edu.swufe.cheng.hello;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ThirdActivity extends AppCompatActivity implements Runnable{
    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;

    EditText rmb;
    TextView show;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        rmb = (EditText)findViewById(R.id.rmb);
        show = (TextView)findViewById(R.id.showOut);

        //获取sp里保存的数据
        SharedPreferences SharedPreferences=getSharedPreferences("myrate",Activity.MODE_PRIVATE);
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);//这个方法只能获取一个配置文件，用于保存关键信息
        dollarRate=SharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate=SharedPreferences.getFloat("euro_rate",0.0f);
        wonRate=SharedPreferences.getFloat("won_rate",0.0f);

        Log.i(TAG,"onCreate:sp dollarRate="+dollarRate);
        Log.i(TAG,"onCreate:sp euroRate="+euroRate);
        Log.i(TAG,"onCreate:sp wonRate="+wonRate);

        //开启子线程 Runnable
        Thread t = new Thread(this);//this调用当前run方法
        t.start();//开始运行

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 5){
                    String str = (String) msg.obj;
                    Log.i(TAG,"handleMessage:getMessage msg= "+str);
                    show.setText(str);
                }
                super.handleMessage(msg);//处理传过来的消息
            }
        };


    }
    public  void onClick(View btn){
        //获取用户输入内容
        String str = rmb.getText().toString();
        float r=0;
        if (str.length()>0){
            r=Float.parseFloat(str);
        }else {
            //提示用户输入内容
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG,"onClick: r="+r);

        if (btn.getId()==R.id.btn_dollar){
            show.setText(String.format("%.2f",r*dollarRate));
        }else if (btn.getId()==R.id.btn_euro){
            show.setText(String.format("%.2f",r*euroRate));
        }else if (btn.getId()==R.id.btn_won){
            show.setText(String.format("%.2f",r*wonRate));
        }
    }
    public void openOne(View btn){
        //打开一个页面Actively
        Log.i("open","openOne: ");
        openConfig();
    }

    private void openConfig() {
        Intent config= new Intent(this,ConfigActivity.class);//打开另一个Activity
        /*Intent web= new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));//打开网页
        Intent intent= new Intent(Intent.ACTION_DIAL, Uri.parse("tel:87092173"));//拨号*/
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        Log.i(TAG,"openOne: dollarRate="+ dollarRate);
        Log.i(TAG,"openOne: euroRate="+ euroRate);
        Log.i(TAG,"openOne: wonRate="+ wonRate);

        //startActivity(config);
        startActivityForResult(config,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_set){
            Intent config= new Intent(this,ConfigActivity.class);//打开另一个Activity
        /*Intent web= new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));//打开网页
        Intent intent= new Intent(Intent.ACTION_DIAL, Uri.parse("tel:87092173"));//拨号*/
            config.putExtra("dollar_rate_key",dollarRate);
            config.putExtra("euro_rate_key",euroRate);
            config.putExtra("won_rate_key",wonRate);

            Log.i(TAG,"openOne: dollarRate="+ dollarRate);
            Log.i(TAG,"openOne: euroRate="+ euroRate);
            Log.i(TAG,"openOne: wonRate="+ wonRate);

            //startActivity(config);
            startActivityForResult(config,1);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==1 && resultCode==2){
            //Bundle bundle = data.getBundleExtra();
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate =  bundle.getFloat("key_euro",0.1f);
            wonRate =  bundle.getFloat("key_won",0.1f);
            Log.i(TAG,"onActivityResult: dollarRate="+dollarRate);
            Log.i(TAG,"onActivityResult: euroRate="+euroRate);
            Log.i(TAG,"onActivityResult: wonRate="+wonRate);

            //将新设置的汇率写道sp里
            SharedPreferences SharedPreferences=getSharedPreferences("myrate",Activity.MODE_PRIVATE);
            android.content.SharedPreferences.Editor editor = SharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i(TAG,"onActivityResult:数据以保存到sharedPreferences");

        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void run() {
        Log.i(TAG,"run:run()......");
        for (int i=1;i<3;i++){
            Log.i(TAG,"run:i="+i);
            try {
                Thread.sleep(2000); //当前线程停止两秒，不让他跑这么快
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //获取Msg对象，用于返回
        Message msg = handler.obtainMessage(5);
        //msg.what = 5;   //arg1,arg2:一个参数，两个参数int   what：用于标记当前msg的属性  obj ：所有对象的父类
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);


        //获取网络数据
        URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();//获得一个输入流

            String html = inputStream2String(in);//调用方法，将输入流转化为字符串
            Log.i(TAG,"run:html="+html);

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //把数据流转化为字符串输出
    private String inputStream2String(InputStream inputStream) throws IOException {
        final int butfferSize = 1024;
        final char[] buffer = new  char[butfferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        for(; ;){
            int rsz = in.read(buffer,0,buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }

}

