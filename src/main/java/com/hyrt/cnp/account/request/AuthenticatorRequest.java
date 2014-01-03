package com.hyrt.cnp.account.request;

import android.content.Context;

import com.google.inject.Inject;
import com.hyrt.cnp.account.model.Base;
import com.hyrt.cnp.account.model.User;
import com.hyrt.cnp.account.service.UserService;

/**
 * Created by yepeng on 13-12-11.
 */
public class AuthenticatorRequest extends BaseRequest{

    private User user;

    public AuthenticatorRequest(User user,Context context) {
        super(User.UserModel.class,context);
        this.user = user;
    }

    @Override
    public Base run() {
        return getRestTemplate().getForObject("http://api.chinaxueqian.com/account/login?"+"username="+user.getUsername()
                +"&password="+ user.getPassword(),User.UserModel.class);
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
