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

public class UserInfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        List<Map<String,String>> values = new ArrayList<Map<String,String>>();
        Map<String,String> item1 = new HashMap<String, String>();
        item1.put("title","宝宝姓名");
        item1.put("content","宝宝");
        values.add(item1);
        Map<String,String> item2 = new HashMap<String, String>();
        item2.put("title","出生年月");
        item2.put("content","2000-19-10");
        values.add(item2);
        Map<String,String> item3 = new HashMap<String, String>();
        item3.put("title","所在班级");
        item3.put("content","向日葵小班");
        values.add(item3);
        Map<String,String> item4 = new HashMap<String, String>();
        item4.put("title","所在幼儿园");
        item4.put("content","宝宝幼儿园");
        values.add(item4);
        Map<String,String> item5 = new HashMap<String, String>();
        item5.put("title","性别");
        item5.put("content","女");
        values.add(item5);
        Map<String,String> item6 = new HashMap<String, String>();
        item6.put("title","国籍");
        item6.put("content","中国");
        values.add(item6);
        Map<String,String> item7 = new HashMap<String, String>();
        item7.put("title","血型");
        item7.put("content","AB");
        values.add(item7);
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
