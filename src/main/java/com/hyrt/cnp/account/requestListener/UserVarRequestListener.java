package com.hyrt.cnp.account.requestListener;

import android.app.Activity;

import com.hyrt.cnp.account.R;
import com.hyrt.cnp.account.manager.UserInfoActivity;
import com.hyrt.cnp.base.account.model.UtilVar;
import com.hyrt.cnp.base.account.requestListener.BaseRequestListener;
import com.octo.android.robospice.persistence.exception.SpiceException;

import roboguice.RoboGuice;

/**
 * Created by yepeng on 14-1-9.
 */
public class UserVarRequestListener extends BaseRequestListener {


    /**
     * @param context
     */

    private String var;
    public UserVarRequestListener(Activity context,String var) {
        super(context);
        RoboGuice.getInjector(context).injectMembers(this);
        this.var=var;
    }

    @Override
    public void onRequestFailure(SpiceException e) {
        super.onRequestFailure(e);
    }

    @Override
    public void onRequestSuccess(Object userDetail) {
        super.onRequestSuccess(userDetail);
        UserInfoActivity activity = (UserInfoActivity)context.get();
        UtilVar result= (UtilVar)userDetail;
        if(userDetail!=null){
            if(var.equals("nationality")){
                activity.SetVar_nationality(result.getData());
            }else if(var.equals("ethnic")){
                activity.SetVar_ethnic(result.getData());
            }else if(var.equals("bloodType")){
                activity.SetVar_bloodType(result.getData());
            }
        }
    }

    @Override
    public UserVarRequestListener start() {
        showIndeterminate(R.string.user_info_pg);
        return this;
    }
}
