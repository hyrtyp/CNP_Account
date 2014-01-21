package com.hyrt.cnp.account.request;

import android.content.Context;

import com.google.inject.Inject;
import com.hyrt.cnp.account.manager.UserInfoActivity;
import com.hyrt.cnp.account.model.BaseTest;
import com.hyrt.cnp.account.service.UserService;

/**
 * Created by yepeng on 14-1-3.
 */
public class UserInfoUpdateRequest extends BaseRequest{

    @Inject
    private UserService userService;

    private String renname;
    private String birthday;
    private String sex;
    private String national;
    private String bloodType;

    public UserInfoUpdateRequest(Context context, String renname, String birthday,String sex, String national, String bloodType) {
        super(BaseTest.class,context);
        this.renname = renname;
        this.birthday = birthday;
        this.sex = sex;
        this.national = national;
        this.bloodType = bloodType;
    }

    @Override
    public BaseTest run() {
        return userService.modifyUserInfo(renname, birthday, sex, national, bloodType);
    }

    /**
     * This method generates a unique cache key for this request. In this case
     * our cache key depends just on the keyword.
     * @return
     */
    public String createCacheKey() {
        return "user.userinfoupdate";
    }
}
