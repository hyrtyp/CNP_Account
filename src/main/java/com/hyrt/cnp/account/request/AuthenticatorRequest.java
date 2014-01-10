package com.hyrt.cnp.account.request;

import com.hyrt.cnp.account.model.User;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

/**
 * Created by yepeng on 13-12-11.
 */
public class AuthenticatorRequest extends SpringAndroidSpiceRequest {

    private User user;

    public AuthenticatorRequest(User user) {
        super(User.UserModel.class);
        this.user = user;
    }

    /**
     * This method generates a unique cache key for this request. In this case
     * our cache key depends just on the keyword.
     * @return
     */
    public String createCacheKey() {
        return "user.";
    }

    @Override
    public User.UserModel loadDataFromNetwork() throws Exception {
            return getRestTemplate().getForObject("http://api.chinaxueqian.com/account/login?"+"username="+user.getUsername()
                    +"&password="+ user.getPassword(),User.UserModel.class);
    }
}
