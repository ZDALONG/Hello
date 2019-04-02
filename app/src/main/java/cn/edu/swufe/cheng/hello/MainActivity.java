package cn.edu.swufe.cheng.hello;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView res;
    EditText inp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //out = (TextView)findViewById(R.id.showText);

        inp = (EditText)findViewById(R.id.inpText);
        Button btn = (Button)findViewById(R.id.btn1);
        res = (TextView)findViewById(R.id.textView);

        //btn.setOnClickListener(this); 此时已注释掉最下面的监听器，并不会调用
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("mail","onClick called....");
                String str = inp.getText().toString();
                //int num = Integer.parseInt(str);
                float num = Float.parseFloat(str);
                float r = (float) (num*5/9.0+32.0);
                res.setText((int) r);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Log.i("click","onClick......");
        //TextView tv = (TextView)findViewById(R.id.showText);
        //EditText inp = (EditText)findViewById(R.id.inpText);

        String str = inp.getText().toString();
        res.setText("Hello"+ str);
    }
}
