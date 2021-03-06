package com.hyrt.cnp.account.requestListener;

import android.app.Activity;

import com.hyrt.cnp.R;
import com.hyrt.cnp.account.model.BaseTest;
import com.octo.android.robospice.persistence.exception.SpiceException;

/**
 * Created by yepeng on 14-1-9.
 */
public class UserPwdRequestListener extends BaseRequestListener{
    /**
     * @param context
     */
    public UserPwdRequestListener(Activity context) {
        super(context);
    }

    @Override
    public void onRequestFailure(SpiceException e) {
        super.onRequestFailure(e);
        showMessage(R.string.pwd_msg_title,R.string.pwd_msgerror_content);
    }


    @Override
    public void onRequestSuccess(Object baseTest) {
        super.onRequestSuccess(baseTest);
        if(context != null && context.get()!=null){
            if(((BaseTest)baseTest).getCode().equals("200")){
                showMessage(R.string.pwd_msg_title,R.string.pwd_msg_content);
            }else{
                showMessage(R.string.pwd_msg_title,R.string.pwd_msgerror_content);
            } 
        }
    }

    @Override
    public UserPwdRequestListener start() {
        showIndeterminate(R.string.user_pwd_pg);
        return this;
    }
}
