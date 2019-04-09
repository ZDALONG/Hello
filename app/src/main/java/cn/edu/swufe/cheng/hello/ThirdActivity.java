package cn.edu.swufe.cheng.hello;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity {
    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;

    EditText rmb;
    TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        rmb = (EditText)findViewById(R.id.rmb);
        show = (TextView)findViewById(R.id.showOut);
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

        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}
