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
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Toast;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.http.GetData;
import android.os.AsyncTask;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class BindActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private SharedPreferences sp;
    private CheckBox rem_seid , auto_bind;
    private Button btn_bind;
    private EditText seid;
    //private ImageButton btnQuit;
    private String seidValue;
    private String result = null;
    public static final String URL = "http://ipws.sinaapp.com/getdata/show.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ȥ������
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bind);

        //��ȡʵ������
        sp = this.getSharedPreferences("seidInfo", Context.MODE_WORLD_READABLE);
        seid = (EditText) findViewById(R.id.ipws_seid_et);
        //���ù�겻��ʾ,���������ù����ɫ
        seid.setCursorVisible(false);
        rem_seid = (CheckBox) findViewById(R.id.cb_seid);
        auto_bind = (CheckBox) findViewById(R.id.cb_auto);
        btn_bind = (Button) findViewById(R.id.btn_bind);

      //btnQuit = (ImageButton)findViewById(R.id.img_btn);

        if(sp.getBoolean("ISCHECK", false))
        {
            //����Ĭ���Ǽ�¼�豸��
            rem_seid.setChecked(true);
            seid.setText(sp.getString("SEID", ""));
            //�ж��Զ���½��ѡ��״̬
            if(sp.getBoolean("AUTO_ISCHECK", false))
            {
                //����Ĭ�����Զ���״̬
                auto_bind.setChecked(true);
                //��ת����
                if (!isNetworkAvailable(BindActivity.this))
               {
                   Toast.makeText(getApplicationContext(), "��ǰû�п������磡", Toast.LENGTH_LONG).show();
                }else{
                    //��ת����
                    seidValue = seid.getText().toString();
                    Intent intent = new Intent(BindActivity.this, MainActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("seid", seidValue);
                    intent.putExtra("bundle", bundle);
                    BindActivity.this.startActivity(intent);}
            }
        }



        // ��¼�����¼�
        btn_bind.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                seidValue = seid.getText().toString();
                if (!isNetworkAvailable(BindActivity.this))
                {
                    Toast.makeText(getApplicationContext(), "��ǰû�п������磡", Toast.LENGTH_LONG).show();
                }
                else{
               // Toast.makeText(BindActivity.this, seidValue, Toast.LENGTH_LONG).show();
                new AsyncTask<String,Integer,String>() {
                    protected String doInBackground(String... params) {
                        try {
                        result = GetData.getHtmlCode(URL + "?seid=" + seidValue + "&show_se=�ύ");
                         //   Toast.makeText(BindActivity.this, result, Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                        return result;
                    }

                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        //result = GetData.getHtmlCode(URL+"?seid="+seidValue+"&show_se=�ύ");
                        result = result.substring(37,43);
                       // Toast.makeText(BindActivity.this, result, Toast.LENGTH_LONG).show();
                       // System.out.println(result);
                        if (!result.equals("'�����ݣ�'")) {
                            Toast.makeText(BindActivity.this, "�󶨳ɹ�", Toast.LENGTH_SHORT).show();
                            //�󶨳ɹ��ͼ�ס�豸�ſ�Ϊѡ��״̬�ű����豸����Ϣ
                            if (rem_seid.isChecked()) {
                                //��ס�豸��
                                Editor editor = sp.edit();
                                editor.putString("SEID", seidValue);
                                editor.commit();
                            }
                            //��ת����
                            Intent intent = new Intent(BindActivity.this, MainActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("seid", seidValue);
                            intent.putExtra("bundle", bundle);
                            BindActivity.this.startActivity(intent);
                            //finish();

                        } else {

                            Toast.makeText(BindActivity.this, "�豸�Ų����ڣ������°�", Toast.LENGTH_LONG).show();
                        }

                    }
                }.execute();
            }
            }
        });

        //������ס�豸�Ŷ�ѡ��ť�¼�
        rem_seid.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (rem_seid.isChecked()) {

                    System.out.println("��ס�豸����ѡ��");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                }else {

                    System.out.println("��ס�豸��û��ѡ��");
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });

        //�����Զ��󶨶�ѡ���¼�
        auto_bind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (auto_bind.isChecked()) {
                    System.out.println("�Զ�����ѡ��");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    System.out.println("�Զ���û��ѡ��");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });
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
