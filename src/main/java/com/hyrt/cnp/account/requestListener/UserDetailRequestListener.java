package com.hyrt.cnp.account.requestListener;

import android.app.Activity;
import android.content.Intent;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hyrt.cnp.R;
import com.hyrt.cnp.account.manager.UserMainActivity;
import com.hyrt.cnp.account.model.UserDetail;
import com.octo.android.robospice.persistence.exception.SpiceException;

import roboguice.RoboGuice;

/**
 * Created by yepeng on 14-1-9.
 */
public class UserDetailRequestListener extends BaseRequestListener{


    //@Inject
    //@Named("schoolIndexActivity")
    //private Class schoolIndexClass;


    /**
     * @param context
     */
    public UserDetailRequestListener(Activity context) {
        super(context);
        RoboGuice.getInjector(context).injectMembers(this);
    }

    @Override
    public void onRequestFailure(SpiceException e) {
        super.onRequestFailure(e);
    }

    @Override
    public void onRequestSuccess(Object userDetail) {
        super.onRequestSuccess(userDetail);
        if(context != null && context.get()!=null){
            if(context.get() instanceof  UserMainActivity){
                UserMainActivity userMainActivity = (UserMainActivity) context.get();
                userMainActivity.updateUI((UserDetail.UserDetailModel)userDetail);
            }else{
                //context.get().startActivity(new Intent(context.get(),schoolIndexClass));
            } 
        }
    }

    @Override
    public UserDetailRequestListener start() {
        showIndeterminate(R.string.user_info_pg);
        return this;
    }
}
