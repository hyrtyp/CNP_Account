package com.hyrt.cnp.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import static com.hyrt.cnp.account.AccountConstants.ACCOUNT_TYPE;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hyrt.cnp.account.model.User;
import com.hyrt.cnp.account.request.AuthenticatorRequest;
import com.hyrt.cnp.account.service.MyJacksonSpringAndroidSpiceService;
import com.hyrt.cnp.account.service.UserService;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.io.File;
import java.io.IOException;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContextScope;

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
    protected boolean requestNewAccount = false;
    private String username = "slerman@163.com";
    private String password = "123456";
    private String authTokenType;

    private AccountManager accountManager;
    private SpiceManager spiceManager = new SpiceManager(
            MyJacksonSpringAndroidSpiceService.class);

    @Inject
    private UserService userService;

    @Inject
    @Named("cacheDir")
    private File cacheDir;
    @Inject
    private AccountScope accountScope;
    @Inject
    private Activity activity;
    @Inject
    private ContextScope contextScope;

    public String getUsername() {
        return username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        checkForUpdates();
        accountManager = AccountManager.get(this);
        //final Intent intent = getIntent();
        //username = intent.getStringExtra(PARAM_USERNAME);
        //authTokenType = intent.getStringExtra(PARAM_AUTHTOKEN_TYPE);
        requestNewAccount = username == null;
        performRequest();
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
        getMenuInflater().inflate(R.menu.main, menu);
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

    private void performRequest() {
        LoginActivity.this.setProgressBarIndeterminateVisibility(true);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        AuthenticatorRequest request = new AuthenticatorRequest(user);
        String lastRequestCacheKey = request.createCacheKey();
        spiceManager.execute(request, lastRequestCacheKey,
                DurationInMillis.ONE_MINUTE, new RequestListener<User.UserModel>(){

            @Override
            public void onRequestFailure(SpiceException e) {
                   e.printStackTrace();
            }

            @Override
            public void onRequestSuccess(User.UserModel user) {
                Account account = new Account(user.getData().getUsername(), ACCOUNT_TYPE);
                if (requestNewAccount) {
                    accountManager
                            .addAccountExplicitly(account, password, null);
                } else
                    accountManager.setPassword(account, password);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                        final AccountManager manager = AccountManager.get(activity);
                        final Account account = AccountUtils.getAccount(manager, activity);

                        accountScope.enterWith(account, manager);
                        try {
                            contextScope.enter(activity);
                            try {
                                System.out.println("yepeng : "+userService.getUser().getUsername());
                            } catch (Exception e) {
                                // Retry task if authentication failure occurs and account is
                                // successfully updated
                                if (AccountUtils.isUnauthorized(e)
                                        && AccountUtils.updateAccount(account, LoginActivity.this))
                                   System.out.println(userService.getUser().getUsername());
                                else
                                    throw e;
                            } finally {
                                contextScope.exit(activity);
                            }
                        } finally {
                            accountScope.exit();
                        }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });

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
