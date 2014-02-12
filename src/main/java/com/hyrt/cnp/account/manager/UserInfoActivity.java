package com.hyrt.cnp.account.manager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hyrt.cnp.R;
import com.hyrt.cnp.account.model.UserDetail;
import com.hyrt.cnp.account.request.UserInfoUpdateRequest;
import com.hyrt.cnp.account.requestListener.UserInfoUpdateRequestListener;
import com.jingdong.common.frame.BaseActivity;
import com.octo.android.robospice.persistence.DurationInMillis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO  部分字段不明确，需要后期增加，还有字典
 */
public class UserInfoActivity extends BaseActivity {

    private StringBuilder renname = new StringBuilder();
    private StringBuilder birthday  = new StringBuilder();
    private StringBuilder className  = new StringBuilder();
    private StringBuilder nurseryName  = new StringBuilder();
    private StringBuilder sex  = new StringBuilder();
    private StringBuilder national  = new StringBuilder();
    private StringBuilder bloodType  = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        UserDetail.UserDetailModel userDetailModel =
                (UserDetail.UserDetailModel) getIntent().getSerializableExtra("vo");
        List<Map<String,String>> values = new ArrayList<Map<String,String>>();
        Map<String,String> item1 = new HashMap<String, String>();
        item1.put("title","宝宝姓名");
        item1.put("content",userDetailModel.getData().getRenname());
        renname.append(userDetailModel.getData().getRenname());
        values.add(item1);
        Map<String,String> item2 = new HashMap<String, String>();
        item2.put("title","出生年月");
        birthday.append(userDetailModel.getData().getBirthday());
        item2.put("content",userDetailModel.getData().getBirthday());
        values.add(item2);
        Map<String,String> item3 = new HashMap<String, String>();
        item3.put("title","所在班级");
        item3.put("content",userDetailModel.getData().getNurseryName());
        values.add(item3);
        Map<String,String> item4 = new HashMap<String, String>();
        item4.put("title","所在幼儿园");
        item4.put("content",userDetailModel.getData().getNurseryName());
        values.add(item4);
        Map<String,String> item5 = new HashMap<String, String>();
        item5.put("title","性别");
        sex.append(userDetailModel.getData().getSex());
        item5.put("content",userDetailModel.getData().getSex());
        values.add(item5);
        Map<String,String> item6 = new HashMap<String, String>();
        item6.put("title","国籍");
        item6.put("content",userDetailModel.getData().getNationality());
        values.add(item6);
        Map<String,String> item7 = new HashMap<String, String>();
        item7.put("title","血型");
        item7.put("content",userDetailModel.getData().getBloodType());
        values.add(item7);
        ((ListView)findViewById(R.id.user_info_listview)).setAdapter( new SimpleAdapter(this,values,R.layout.user_info_item,new String[]{"title","content"}
                ,new int[]{R.id.title,R.id.content}){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final LinearLayout linearLayout = (LinearLayout) super.getView(position, convertView, parent);
                if(linearLayout != null){
                    final EditText editText = (EditText) linearLayout.findViewById(R.id.content);
                    // TODO MODIFY USERINFO
                    switch (position){//0-4不可以修改，5-6可以修改
                        case 0:
                            editText.setEnabled(false);
                            editText.setFocusable(false);
                            break;
                        case 1:
                            editText.setEnabled(false);
                            editText.setFocusable(false);
                            break;
                        case 2:
                            editText.setEnabled(false);
                            editText.setFocusable(false);
                            break;
                        case 3:
                            editText.setEnabled(false);
                            editText.setFocusable(false);
                            break;
                        case 4:
                            editText.setEnabled(false);
                            editText.setFocusable(false);
                            break;
                        case 5:
                            editText.requestFocus();
                            break;
                        case 6:
                            editText.requestFocus();
                            break;
                    }
                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText.addTextChangedListener(new TextWatcher(){

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    if(s.toString().length() == 0)
                                        return;
                                    switch (position){
//                                        case 0:
//                                            renname.delete(0,renname.length());
//                                            renname.append(s.toString());
//                                            break;
//                                        case 1:
//                                            birthday.delete(0,birthday.length());
//                                            birthday.append(s.toString());
//                                            break;
//                                        case 2:
//                                            className.delete(0, className.length());
//                                            className.append(s.toString());
//                                            break;
//                                        case 3:
//                                            nurseryName.delete(0, nurseryName.length());
//                                            nurseryName.append(s.toString());
//                                            break;
//                                        case 4:
//                                            sex.delete(0,sex.length());
//                                            sex.append(s.toString());
//                                            break;
                                        case 5:
                                            national.delete(0,national.length());
                                            national.append(s.toString());
                                            break;
                                        case 6:
                                            bloodType.delete(0,bloodType.length());
                                            bloodType.append(s.toString());
                                            break;
                                    }

                                }
                            });
                        }
                    });
                }
                return linearLayout;
            }
        });
        findViewById(R.id.userinfo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER){
            initData();
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 获取数据
     */
    private void initData(){
        UserInfoUpdateRequest userInfoUpdateRequest = new UserInfoUpdateRequest(this,renname.toString(),
                birthday.toString(),sex.toString(),national.toString(),bloodType.toString());
        UserInfoUpdateRequestListener userPwdRequestListener = new UserInfoUpdateRequestListener(this);
        spiceManager.execute(userInfoUpdateRequest, userInfoUpdateRequest.createCacheKey(),
                DurationInMillis.ONE_SECOND, userPwdRequestListener.start());
    }

}
