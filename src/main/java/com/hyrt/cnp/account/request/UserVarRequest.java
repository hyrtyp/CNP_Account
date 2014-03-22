package com.hyrt.cnp.account.request;

import android.content.Context;

import com.google.inject.Inject;
import com.hyrt.cnp.base.account.model.UserDetail;
import com.hyrt.cnp.base.account.model.UtilVar;
import com.hyrt.cnp.base.account.request.BaseRequest;
import com.hyrt.cnp.base.account.service.UserService;

/**
 * Created by yepeng on 14-1-3.
 */
public class UserVarRequest extends BaseRequest {

    @Inject
    private UserService userService;
    private String name;
    public UserVarRequest(Context context,String name) {
        super(UserDetail.UserDetailModel.class,context);
        this.name=name;
    }

    @Override
    public UtilVar run() {
        return userService.getUtilvar(name);
    }

    /**
     * This method generates a unique cache key for this request. In this case
     * our cache key depends just on the keyword.
     * @return
     */
    public String createCacheKey() {
        return "uservar"+name;
    }
}
