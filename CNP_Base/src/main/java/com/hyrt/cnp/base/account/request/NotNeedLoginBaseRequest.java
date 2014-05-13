package com.hyrt.cnp.base.account.request;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.app.Activity;
import android.content.Context;

import com.google.inject.Inject;
import com.hyrt.cnp.base.account.AccountScope;
import com.hyrt.cnp.base.account.AccountUtils;
import com.hyrt.cnp.base.account.model.Base;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.io.IOException;

import roboguice.RoboGuice;
import roboguice.inject.ContextScope;

/**
 * Created by Zoe on 2014-05-05.
 */
public abstract class NotNeedLoginBaseRequest extends SpringAndroidSpiceRequest {
    private Context context;

    @Inject
    private AccountScope accountScope;
    @Inject
    private Activity activity;
    @Inject
    private ContextScope contextScope;

    public Context getContext() {
        return context;
    }

    public NotNeedLoginBaseRequest(Class clazz,Context context) {
        super(clazz);
        this.context = context;
        RoboGuice.getInjector(context).injectMembers(this);
    }

    @Override
    public Base loadDataFromNetwork() throws Exception {
        Base base = run();
        return base;
    }

    public abstract Base run();
}
