/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyrt.cnp.account;

import android.text.TextUtils;
import android.util.Log;

import com.google.inject.Provider;
import com.github.kevinsawicki.http.HttpRequest;

import static android.util.Log.DEBUG;

/**
 * {@link AccountClient} extensions that integrates with the Android account
 * manager to provide request credentials
 */
public class AccountClient extends CNPClient{

    private static final String TAG = "AccountClient";

    private final Provider<CNPAccount> accountProvider;


    /**
     * Create account-aware client
     *
     * @param accountProvider
     */
    public AccountClient(final Provider<CNPAccount> accountProvider) {
        super();
        this.accountProvider = accountProvider;
    }

    @Override
    public HttpRequest configureRequest(HttpRequest httpRequest) {
        CNPAccount account = accountProvider.get();

        if (Log.isLoggable(TAG, DEBUG))
            Log.d(TAG, "Authenticating using " + account);

        // Credentials setting must come before super call
        //TODO 让首师方提供点单登陆的功能，然后恢复以下四行
       /* String token = account.getAuthToken();
        if (!TextUtils.isEmpty(token))
            setOAuth2Token(token);
        else*/
            setCredentials(account.getUsername(), account.getPassword());
        return super.configureRequest(httpRequest);
    }

}
