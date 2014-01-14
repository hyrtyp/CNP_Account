package com.hyrt.cnp.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import com.google.inject.Inject;
import com.hyrt.cnp.R;
import com.hyrt.cnp.account.model.User;
import com.hyrt.cnp.account.request.AuthenticatorRequest;
import com.hyrt.cnp.account.requestListener.LoginRequestListener;
import com.hyrt.cnp.account.service.MyService;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import roboguice.activity.RoboActivity;
import com.hyrt.cnp.R;


import static com.hyrt.cnp.account.AccountConstants.ACCOUNT_TYPE;

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
    private String password = "123456";
    private String authTokenType;

    @Inject
    private AccountManager accountManager;

    private SpiceManager spiceManager = new SpiceManager(
            JacksonSpringAndroidSpiceService.class);
    //@Inject
    //@Named("schoolIndexActivity")
    //private Class SchoolIndexActivity;

    private EditText usernameEt;
    private EditText passwordEt;
    private Button loginBtn;

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
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_login);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            return rootView;
        }
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

}
