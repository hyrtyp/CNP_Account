package com.hyrt.cnp.account.request;

import android.content.Context;

import com.google.inject.Inject;
import com.hyrt.cnp.account.model.Base;
import com.hyrt.cnp.account.model.UserDetail;
import com.hyrt.cnp.account.service.UserService;

/**
 * Created by yepeng on 14-1-3.
 */
public class UserDetailRequest extends BaseRequest{

    @Inject
    private UserService userService;

    public UserDetailRequest(Context context) {
        super(UserDetail.UserDetailModel.class,context);
    }

    @Override
    public UserDetail.UserDetailModel run() {
<<<<<<< HEAD
        return userService.getUser(getRestTemplate());
=======
        return userService.getUser();
>>>>>>> ececa967d9aa303697f1c7e383215658fa063e5b
    }

    /**
     * This method generates a unique cache key for this request. In this case
     * our cache key depends just on the keyword.
     * @return
     */
    public String createCacheKey() {
        return "user.detail";
    }
}
