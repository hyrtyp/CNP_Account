package com.hyrt.cnp.account.request;

import com.hyrt.cnp.account.model.User;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

/**
 * Created by yepeng on 13-12-11.
 */
public class AuthenticatorRequest extends SpringAndroidSpiceRequest<User.UserModel>{

    private User user;

    public AuthenticatorRequest(User user) {
        super(User.UserModel.class);
        this.user = user;
    }


    /**
     * start thread load data
     * @return user base information
     * @throws Exception
     * TODO 加入scope inject
     */
    @Override
    public User.UserModel loadDataFromNetwork() throws Exception {
        String url = "http://api.chinaxueqian.com/account/login?username="+user.getUsername()+"&password="+user.getPassword();
        return getRestTemplate().getForObject(url, User.UserModel.class);
    }

    /**
     * This method generates a unique cache key for this request. In this case
     * our cache key depends just on the keyword.
     * @return
     */
    public String createCacheKey() {
        return "user.";
    }
}
