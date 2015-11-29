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
        //ȥ������
       // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("�Ҿ�ֲ���Զ�����ϵͳ");
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


        //�õ���ת����Activity��Intent����
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        seiddata = bundle.getString("seid");
        seid_name.setText(seiddata);
        asyncGet = new AsyncGet();
        asyncGet.execute();

        // ���¼����¼�
        btn_updata.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (!isNetworkAvailable(MainActivity.this))
                {
                    Toast.makeText(getApplicationContext(), "��ǰû�п������磡", Toast.LENGTH_LONG).show();
                } else{
                    asyncGet = new AsyncGet();
                    asyncGet.execute();
                }
            }
    });

        // ���ü����¼�
        btn_set.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //��ת����
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
        //ִ��ʱ���ø÷���
            protected String doInBackground(String... params) {
                try {
                    result = GetData.getHtmlCode(URL + "?seid=" + seiddata + "&show_se=�ύ");
                    //   Toast.makeText(BindActivity.this, result, Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                return result;
            }
        //�������ʱִ�и÷���
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            //��ȡ������Ϣ
            //Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            humdata = Data("ʪ��","<br >�¶�");
            tempdata = Data("�¶�","<br >����");
            beamdata = Data("����","<br >ˮ��״̬");
            wpdata = Data("ˮ��״̬","<br >");
            timedata = Data("����ʱ��","</p>");
            if (humdata != null&&tempdata != null&&beamdata != null&&wpdata != null&&timedata != null) {
                //���ݲ���
                hum.setText(humdata);
                temp.setText(tempdata);
                beam.setText(beamdata);
                wp.setText(wpdata);
                time.setText(timedata);
            } else{
                Toast.makeText(MainActivity.this, "�����ݣ�", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected String Data (String first, String end) {
        Pattern p = Pattern.compile(first + "��(.*)" + end);
        Matcher m = p.matcher(result);
        while (m.find()) {
            return m.group(1);
        }
        return null;
    }

    //��鵱ǰ�����Ƿ����
    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,net�����ӵĹ���
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // ��ȡNetworkInfo����
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===״̬===" + networkInfo[i].getState());
                    System.out.println(i + "===����===" + networkInfo[i].getTypeName());
                    // �жϵ�ǰ����״̬�Ƿ�Ϊ����״̬
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
