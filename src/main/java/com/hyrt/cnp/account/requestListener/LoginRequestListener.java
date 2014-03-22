package com.hyrt.cnp.account.requestListener;

import android.accounts.Account;
import android.app.Activity;

import com.hyrt.cnp.account.R;
import com.hyrt.cnp.account.LoginActivity;
import com.hyrt.cnp.base.account.model.User;
import com.hyrt.cnp.base.account.requestListener.BaseRequestListener;
import com.octo.android.robospice.persistence.exception.SpiceException;

import static com.hyrt.cnp.base.account.AccountConstants.ACCOUNT_TYPE;

/**
 * Created by yepeng on 14-1-9.
 */
public class LoginRequestListener extends BaseRequestListener {

    /**
     * @param context
     */
    public LoginRequestListener(Activity context) {
        super(context);
    }

    @Override
    public void onRequestFailure(SpiceException e) {
        showMessage(R.string.login_notice, R.string.error_username);
        super.onRequestFailure(e);
    }

    @Override
    public void onRequestSuccess(Object user) {
        super.onRequestSuccess(user);
            Account account = new Account(((User.UserModel)user).getData().getUsername(), ACCOUNT_TYPE);
            if(context != null && context.get()!=null){
                LoginActivity loginActivity = (LoginActivity) context.get();
                if (loginActivity.isRequestNewAccount()) {
                    loginActivity.getAccountManager()
                            .addAccountExplicitly(account, loginActivity.getPassword(), null);
                } else
                    loginActivity.getAccountManager().setPassword(account, loginActivity.getPassword());
                loginActivity.finishLogin(account.name,loginActivity.getPassword());
            }
    }

    @Override
    public LoginRequestListener start() {
        showIndeterminate(R.string.user_login_pg);
        return this;
    }

}
