package com.example.ipws;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.ImageButton;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.view.KeyEvent;

import android.os.AsyncTask;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.example.http.GetData;
import android.view.View.OnClickListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;



/**
 * Created by Marquis on 2015/4/10.
 */
public class MainActivity extends Activity {

    private TextView seid_name, hum,temp,beam,wp,time;
    private String humdata,tempdata,beamdata,wpdata,timedata;
    private Button btn_set;
    private Button btn_updata;
    public static final String URL = "http://ipws.sinaapp.com/getdata/show.php";
    private String seiddata;
    private String result = null;
    private AsyncGet asyncGet;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题
       // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("家居植物自动浇灌系统");
        // TODO Auto-generated method stub
        setContentView(R.layout.main);
        seid_name = (TextView)findViewById(R.id.seid);
        hum = (TextView)findViewById(R.id.hum_data);
        temp = (TextView)findViewById(R.id.temp_data);
        beam = (TextView)findViewById(R.id.beam_data);
        wp = (TextView)findViewById(R.id.wp_data);
        time = (TextView)findViewById(R.id.time_data);
        btn_set = (Button)findViewById(R.id.btn_set);
        btn_updata = (Button)findViewById(R.id.btn_updata);


        //得到跳转到该Activity的Intent对象
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        seiddata = bundle.getString("seid");
        seid_name.setText(seiddata);
        asyncGet = new AsyncGet();
        asyncGet.execute();

        // 更新监听事件
        btn_updata.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (!isNetworkAvailable(MainActivity.this))
                {
                    Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
                } else{
                    asyncGet = new AsyncGet();
                    asyncGet.execute();
                }
            }
    });

        // 设置监听事件
        btn_set.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //跳转界面
                Intent intent = new Intent(MainActivity.this, SetActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("seid", seiddata);
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, RESULT_OK);
                //finish();
            }
        });


    }

    class AsyncGet extends AsyncTask<String,Integer,String>{
        //执行时调用该方法
            protected String doInBackground(String... params) {
                try {
                    result = GetData.getHtmlCode(URL + "?seid=" + seiddata + "&show_se=提交");
                    //   Toast.makeText(BindActivity.this, result, Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                return result;
            }
        //任务结束时执行该方法
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            //提取有用信息
            //Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            humdata = Data("湿度","<br >温度");
            tempdata = Data("温度","<br >光照");
            beamdata = Data("光照","<br >水泵状态");
            wpdata = Data("水泵状态","<br >");
            timedata = Data("更新时间","</p>");
            if (humdata != null&&tempdata != null&&beamdata != null&&wpdata != null&&timedata != null) {
                //数据插入
                hum.setText(humdata);
                temp.setText(tempdata);
                beam.setText(beamdata);
                wp.setText(wpdata);
                time.setText(timedata);
            } else{
                Toast.makeText(MainActivity.this, "无数据！", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected String Data (String first, String end) {
        Pattern p = Pattern.compile(first + "：(.*)" + end);
        Matcher m = p.matcher(result);
        while (m.find()) {
            return m.group(1);
        }
        return null;
    }

    //检查当前网络是否可用
    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
