package com.hyrt.cnp.account.manager;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hyrt.cnp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPasswordActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_password);
        List<Map<String,String>> values = new ArrayList<Map<String,String>>();
        Map<String,String> item1 = new HashMap<String, String>();
        item1.put("title","当前账号");
        item1.put("content","13718868826");
        values.add(item1);
        Map<String,String> item2 = new HashMap<String, String>();
        item2.put("title","当前密码");
        item2.put("content","");
        values.add(item2);
        Map<String,String> item3 = new HashMap<String, String>();
        item3.put("title","新密码");
        item3.put("content","");
        values.add(item3);
        Map<String,String> item4 = new HashMap<String, String>();
        item4.put("title","确认密码");
        item4.put("content","");
        values.add(item4);
        ((ListView)findViewById(R.id.user_info_listview)).setAdapter( new SimpleAdapter(this,values,R.layout.user_info_item,new String[]{"title","content"},new int[]{R.id.title,R.id.content}));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

}
