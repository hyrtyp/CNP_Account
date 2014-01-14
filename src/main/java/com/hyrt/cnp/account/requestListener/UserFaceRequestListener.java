package com.hyrt.cnp.account.requestListener;

import android.app.Activity;
import android.content.Intent;

import com.hyrt.cnp.R;
import com.hyrt.cnp.account.manager.UserMainActivity;
import com.hyrt.cnp.account.model.BaseTest;
import com.hyrt.cnp.account.model.UserDetail;
import com.octo.android.robospice.persistence.exception.SpiceException;

/**
 * Created by yepeng on 14-1-9.
 */
public class UserFaceRequestListener extends BaseRequestListener{
    /**
     * @param context
     */
    public UserFaceRequestListener(Activity context) {
        super(context);
    }

    @Override
    public void onRequestFailure(SpiceException e) {
        super.onRequestFailure(e);
        showMessage(R.string.face_msg_title,R.string.face_msgerror_content);
    }


    @Override
    public void onRequestSuccess(Object baseTest) {
        super.onRequestSuccess(baseTest);
        if(context != null && context.get()!=null){
            if(!((BaseTest)baseTest).getCode().equals("200")){
                showMessage(R.string.face_msg_title,R.string.face_msg_content);
            }else{
                showMessage(R.string.face_msg_title,R.string.face_msgerror_content);
            } 
        }
    }

    @Override
    public UserFaceRequestListener start() {
        showIndeterminate(R.string.user_face_pg);
        return this;
    }
}
