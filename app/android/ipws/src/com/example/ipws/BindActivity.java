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
        //去除标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bind);

        //获取实例对象
        sp = this.getSharedPreferences("seidInfo", Context.MODE_WORLD_READABLE);
        seid = (EditText) findViewById(R.id.ipws_seid_et);
        //设置光标不显示,但不能设置光标颜色
        seid.setCursorVisible(false);
        rem_seid = (CheckBox) findViewById(R.id.cb_seid);
        auto_bind = (CheckBox) findViewById(R.id.cb_auto);
        btn_bind = (Button) findViewById(R.id.btn_bind);

      //btnQuit = (ImageButton)findViewById(R.id.img_btn);

        if(sp.getBoolean("ISCHECK", false))
        {
            //设置默认是记录设备号
            rem_seid.setChecked(true);
            seid.setText(sp.getString("SEID", ""));
            //判断自动登陆多选框状态
            if(sp.getBoolean("AUTO_ISCHECK", false))
            {
                //设置默认是自动绑定状态
                auto_bind.setChecked(true);
                //跳转界面
                if (!isNetworkAvailable(BindActivity.this))
               {
                   Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
                }else{
                    //跳转界面
                    seidValue = seid.getText().toString();
                    Intent intent = new Intent(BindActivity.this, MainActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("seid", seidValue);
                    intent.putExtra("bundle", bundle);
                    BindActivity.this.startActivity(intent);}
            }
        }



        // 登录监听事件
        btn_bind.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                seidValue = seid.getText().toString();
                if (!isNetworkAvailable(BindActivity.this))
                {
                    Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
                }
                else{
               // Toast.makeText(BindActivity.this, seidValue, Toast.LENGTH_LONG).show();
                new AsyncTask<String,Integer,String>() {
                    protected String doInBackground(String... params) {
                        try {
                        result = GetData.getHtmlCode(URL + "?seid=" + seidValue + "&show_se=提交");
                         //   Toast.makeText(BindActivity.this, result, Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                        return result;
                    }

                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        //result = GetData.getHtmlCode(URL+"?seid="+seidValue+"&show_se=提交");
                        result = result.substring(37,43);
                       // Toast.makeText(BindActivity.this, result, Toast.LENGTH_LONG).show();
                       // System.out.println(result);
                        if (!result.equals("'无数据！'")) {
                            Toast.makeText(BindActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                            //绑定成功和记住设备号框为选中状态才保存设备号信息
                            if (rem_seid.isChecked()) {
                                //记住设备号
                                Editor editor = sp.edit();
                                editor.putString("SEID", seidValue);
                                editor.commit();
                            }
                            //跳转界面
                            Intent intent = new Intent(BindActivity.this, MainActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("seid", seidValue);
                            intent.putExtra("bundle", bundle);
                            BindActivity.this.startActivity(intent);
                            //finish();

                        } else {

                            Toast.makeText(BindActivity.this, "设备号不存在，请重新绑定", Toast.LENGTH_LONG).show();
                        }

                    }
                }.execute();
            }
            }
        });

        //监听记住设备号多选框按钮事件
        rem_seid.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (rem_seid.isChecked()) {

                    System.out.println("记住设备号已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                }else {

                    System.out.println("记住设备号没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });

        //监听自动绑定多选框事件
        auto_bind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (auto_bind.isChecked()) {
                    System.out.println("自动绑定已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    System.out.println("自动绑定没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });
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
