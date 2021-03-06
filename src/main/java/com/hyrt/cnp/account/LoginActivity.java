package com.hyrt.cnp.account;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.hyrt.cnp.R;
import com.hyrt.cnp.account.model.User;
import com.hyrt.cnp.account.request.AuthenticatorRequest;
import com.hyrt.cnp.account.requestListener.LoginRequestListener;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import roboguice.activity.RoboActivity;

public class LoginActivity extends RoboActivity{

    /**
     * Auth token type parameter
     */
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

    /**
     * Initial user name
     */
    public static final String PARAM_USERNAME = "username";

    /**
     * hockeyapp plugin APP_ID for this project
     */
    private static final String APP_ID = "411f163ce21e2352706900bdccb37c92";

    /**
     * Was the original caller asking for an entirely new account?
     */
    protected boolean requestNewAccount = true;
    private String username = "slerman@163.com";
    private String password = "123";
    private String authTokenType;

    private AccountManager accountManager;

    private EditText usernameEt;
    private EditText passwordEt;
    private Button loginBtn;
    public SpiceManager spiceManager = new SpiceManager(
            JacksonSpringAndroidSpiceService.class);

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRequestNewAccount() {
        return requestNewAccount;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        accountManager = AccountManager.get(this);
        usernameEt = (EditText)findViewById(R.id.login_name_et);
        passwordEt = (EditText)findViewById(R.id.login_password_et);
        loginBtn = (Button)findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!usernameEt.getText().toString().equals("")){
                    username = usernameEt.getText().toString();
                    password = passwordEt.getText().toString();
                }
                handlerLogin();
            }
        });
        checkForUpdates();
        //final Intent intent = getIntent();
        //username = intent.getStringExtra(PARAM_USERNAME);
        //authTokenType = intent.getStringExtra(PARAM_AUTHTOKEN_TYPE);
        //requestNewAccount = username == null;
    }


    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
    }

    private void checkForCrashes() {
        CrashManager.register(this, APP_ID);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this, APP_ID);
    }

    private void handlerLogin() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        AuthenticatorRequest request = new AuthenticatorRequest(user);
        String lastRequestCacheKey = request.createCacheKey();
        LoginRequestListener loginRequestListener = new LoginRequestListener(this);
        spiceManager.execute(request, lastRequestCacheKey,DurationInMillis.ONE_MINUTE,loginRequestListener.start());

    }

}
