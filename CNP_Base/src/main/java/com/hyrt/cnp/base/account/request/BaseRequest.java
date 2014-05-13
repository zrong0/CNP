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
 * Created by yepeng on 14-1-3.
 */
public abstract class BaseRequest extends SpringAndroidSpiceRequest{

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

    public BaseRequest(Class clazz,Context context) {
        super(clazz);
        this.context = context;
        RoboGuice.getInjector(context).injectMembers(this);
    }

    @Override
    public Base loadDataFromNetwork() throws Exception {
        Base base = null;
        try{
            final AccountManager manager = AccountManager.get(activity);
            final Account account;
            try {
                account = AccountUtils.getAccount(manager, activity);
            } catch (IOException e) {
                return null;
            } catch (AccountsException e) {
                return null;
            }
            accountScope.enterWith(account, manager);
            try {
                contextScope.enter(activity);
                try {
                    base = run();
                } catch (Exception e) {
                    // Retry task if authentication failure occurs and account is
                    // successfully updated
                    if (AccountUtils.isUnauthorized(e)
                            && AccountUtils.updateAccount(account, activity))
                        return run();
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
        return base;
    }

    public abstract Base run();

}
