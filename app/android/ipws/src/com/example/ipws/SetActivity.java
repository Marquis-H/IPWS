package com.example.ipws;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import java.sql.Time;

import android.os.AsyncTask;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.example.http.GetData;
import com.example.http.SetData;
import android.view.View.OnClickListener;
import android.view.KeyEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.CompoundButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by Marquis on 2015/4/11.
 */
public class SetActivity extends Activity implements View.OnTouchListener {
    private SharedPreferences sp;
    private TextView seid_name,up_timedata;
    private String seiddata,open_humdata, end_humdata, open_timedata, end_timedata,timedata;
    private Button btn_esc,btn_qd;
    private EditText open_hum, end_hum, open_time, end_time;
    private CheckBox hum_pri, time_pri;
    private AsyncGetSet asyncGetSet;
    private String pri="1",result,pri_data="1",resultset;
    public static final String URL = "http://ipws.sinaapp.com/senddata/set.php";
    public static final String SETURL = "http://ipws.sinaapp.com/senddata/send.php";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ȥ������
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("����");
        // TODO Auto-generated method stub
        setContentView(R.layout.set);

        seid_name = (TextView)findViewById(R.id.seid);
        up_timedata = (TextView)findViewById(R.id.up_timedata);
        btn_esc = (Button)findViewById(R.id.btn_esc);
        open_hum = (EditText)findViewById(R.id.open_hum_et);
        end_hum = (EditText)findViewById(R.id.end_hum_et);
        open_time = (EditText)findViewById(R.id.open_time_et);
        end_time = (EditText)findViewById(R.id.end_time_et);
        hum_pri = (CheckBox) findViewById(R.id.hum_pri);
        time_pri = (CheckBox) findViewById(R.id.time_pri);
        btn_qd = (Button)findViewById(R.id.btn_qd);

        //�õ���ת����Activity��Intent����
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        seiddata = bundle.getString("seid");
        seid_name.setText(seiddata);
        asyncGetSet = new AsyncGetSet();
        asyncGetSet.execute();
       // sp = this.getSharedPreferences(seiddata+"Info", Context.MODE_WORLD_READABLE);
       // open_hum.setText(sp.getString("open_hum", ""));
       // open_hum.setText(open_humdata);
       // end_hum.setText(sp.getString("end_hum", ""));
       // end_hum.setText(end_humdata);
      //  open_time.setText(sp.getString("open_time", ""));
      //  open_time.setText(open_timedata);
       // end_time.setText(sp.getString("end_time", ""));
       // end_time.setText(end_timedata);
      //  pri_data = sp.getString("pri", "");
     //   if(pri_data.equals("0")){
      //      hum_pri.setChecked(true);
     //   }else if(pri_data.equals("2")){
    //        time_pri.setChecked(true);
     //   }
       // Toast.makeText(SetActivity.this, sp.getString("pri", ""), Toast.LENGTH_LONG).show();
      //  up_timedata.setText(sp.getString("up_time_data", ""));
        open_time.setOnTouchListener(this);
        end_time.setOnTouchListener(this);

       // open_hum.setText(savedInstanceState.getString("temp"));

        hum_pri.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(time_pri.isChecked()) {
                    time_pri.setChecked(false);
                }
                if (hum_pri.isChecked()==true) {
                    pri = "0";
                } else {
                    pri = "1";
                }
            }
        });
        hum_pri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

            }
        });

        time_pri.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (hum_pri.isChecked()) {
                    hum_pri.setChecked(false);
                }
                if (time_pri.isChecked()==true) {
                    pri = "2";
                } else {
                    pri = "1";
                }
            }
        });
//        ck.setChecked(checked)
        time_pri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
            }
        });
// ���ؼ����¼�
        btn_esc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //��ת����
              //  Editor editor = sp.edit();
              //  editor.putString("open_hum",  open_hum.getText().toString());
              //  editor.putString("end_hum",  end_hum.getText().toString());
              //  editor.putString("open_time",  open_time.getText().toString());
              //  editor.putString("end_time",  end_time.getText().toString());
              //  editor.putString("pri",  pri);
              //  editor.putString("up_time_data", up_timedata.getText().toString());
             //   editor.commit();
                SetActivity.this.setResult(RESULT_OK, getIntent());
                SetActivity.this.finish();
            }
        });
        // ȷ�������¼�
        btn_qd.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (!isNetworkAvailable(SetActivity.this))
                {
                    Toast.makeText(getApplicationContext(), "��ǰû�п������磡", Toast.LENGTH_LONG).show();
                }
                else{
                    new AsyncTask<String,Integer,String>() {
                        protected String doInBackground(String... params) {
                            try {

                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date  curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��

                               String url = (URL + "?seid=" + seiddata + "&open_hum="+ open_hum.getText().toString() +
                                        "&end_hum="+ end_hum.getText().toString()+ "&open_time="+ open_time.getText().toString() +
                                        "&end_time="+ end_time.getText().toString()+"&pri="+ pri+"&time="+formatter.format(curDate));
                                url = url.replaceAll(" ", "%20");
                                result = SetData.getHtmlCode(url);
                               // System.out.println(result);
                                //   Toast.makeText(BindActivity.this, result, Toast.LENGTH_LONG).show();
                            }catch (Exception e){
                                System.out.println(e.getMessage());
                            }
                            return result;
                        }

                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);
                            //result = GetData.getHtmlCode(URL+"?seid="+seidValue+"&show_se=�ύ");
                          //  result = result.substring(23,30);
                            //Toast.makeText(SetActivity.this, result, Toast.LENGTH_LONG).show();
                            // System.out.println(result);
                            if (result.equals("�������óɹ���\n")) {
                               // up_timedata.setText("1");
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date  curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
                                up_timedata.setText(formatter.format(curDate));
                                Toast.makeText(SetActivity.this, "�������óɹ���", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SetActivity.this, result, Toast.LENGTH_LONG).show();
                            }

                        }
                    }.execute();
                }
            }
        });
    }

    class AsyncGetSet extends AsyncTask<String,Integer,String>{
        //ִ��ʱ���ø÷���
        protected String doInBackground(String... params) {
            try {
                resultset = GetData.getHtmlCode(SETURL + "?seid=" + seiddata + "&set=1");
                //   Toast.makeText(BindActivity.this, result, Toast.LENGTH_LONG).show();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            return resultset;
        }
        //�������ʱִ�и÷���
        protected void onPostExecute(String resultset){
            super.onPostExecute(resultset);
            //��ȡ������Ϣ
           // Toast.makeText(SetActivity.this, result, Toast.LENGTH_LONG).show();
            open_humdata = Data("open_hum","<br >end_hum");
            end_humdata = Data("end_hum","<br >open_time");
            open_timedata = Data("open_time","<br >end_time");
            end_timedata = Data("end_time","<br >pri");
            pri_data = Data("pri","<br >uptime");
            timedata = Data("uptime","<br >");
            //Toast.makeText(SetActivity.this, pri_data, Toast.LENGTH_LONG).show();
           if(pri_data.equals("0")){
                      hum_pri.setChecked(true);
                   }else if(pri_data.equals("2")){
                        time_pri.setChecked(true);
                   }


       //     if (open_humdata != null&&end_humdata != null&&open_timedata != null&&end_timedata != null&&pri_data != null) {
                //���ݲ���
           // Toast.makeText(SetActivity.this, resultset, Toast.LENGTH_LONG).show();
                open_hum.setText(open_humdata);
                // end_hum.setText(sp.getString("end_hum", ""));
                end_hum.setText(end_humdata);
                //  open_time.setText(sp.getString("open_time", ""));
                open_time.setText(open_timedata);
                // end_time.setText(sp.getString("end_time", ""));
                end_time.setText(end_timedata);
            up_timedata.setText(timedata);
         //   } else{
           //     Toast.makeText(SetActivity.this, "�����ݣ�", Toast.LENGTH_LONG).show();
          //  }
        }
    }

    protected String Data (String first, String end) {
        Pattern p = Pattern.compile(first + ":(.*)" + end);
        Matcher m = p.matcher(resultset);
        while (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.date_time_dialog, null);
            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
            builder.setView(view);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(Calendar.MINUTE);

            if (v.getId() == R.id.open_time_et) {
                final int inType = open_time.getInputType();
                open_time.setInputType(InputType.TYPE_NULL);
                open_time.onTouchEvent(event);
                open_time.setInputType(inType);
                open_time.setSelection(open_time.getText().length());

                builder.setTitle("ѡȡ��ʼʱ��");
                builder.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        sb.append(" ");
                        sb.append(timePicker.getCurrentHour())
                                .append(":").append(timePicker.getCurrentMinute()).append(":00");

                        open_time.setText(sb);
                        end_time.requestFocus();

                        dialog.cancel();
                    }
                });

            } else if (v.getId() == R.id.end_time_et) {
                int inType = end_time.getInputType();
                end_time.setInputType(InputType.TYPE_NULL);
                end_time.onTouchEvent(event);
                end_time.setInputType(inType);
                end_time.setSelection(end_time.getText().length());

                builder.setTitle("ѡȡ����ʱ��");
                builder.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        sb.append(" ");
                        sb.append(timePicker.getCurrentHour())
                                .append(":").append(timePicker.getCurrentMinute()).append(":00");
                        end_time.setText(sb);

                        dialog.cancel();
                    }
                });
            }

            Dialog dialog = builder.create();
            dialog.show();
        }

        return true;
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
  //  protected void onSaveInstanceState(Bundle outState) {
  //      super.onSaveInstanceState(outState);
   //     outState.putString("temp",open_hum.getText().toString());
  //  }

    //��дonKeyDown����
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                Editor editor = sp.edit();
                editor.putString("open_hum",  open_hum.getText().toString());
                editor.putString("end_hum",  end_hum.getText().toString());
                editor.putString("open_time",  open_time.getText().toString());
                editor.putString("end_time",  end_time.getText().toString());
                editor.putString("pri",  pri);
                editor.putString("up_time_data", up_timedata.getText().toString());
                editor.commit();
                SetActivity.this.setResult(RESULT_OK, getIntent());
                SetActivity.this.finish();
                break;
            default:
                break;
        }
        return false;
    }
}
