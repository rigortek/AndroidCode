package com.cw.updateuifromchildthread.mvpDemo.presenter;

import android.os.Handler;

import com.cw.updateuifromchildthread.mvpDemo.bean.User;
import com.cw.updateuifromchildthread.mvpDemo.biz.IUserBiz;
import com.cw.updateuifromchildthread.mvpDemo.biz.OnUserLoginListener;
import com.cw.updateuifromchildthread.mvpDemo.biz.UserBiz;
import com.cw.updateuifromchildthread.mvpDemo.view.IUserLoginView;



/**
 * Create by robin On 21-3-24
 */
public class UserLoginPresenter {

    private IUserBiz mUserBiz;  // Module
    private IUserLoginView mUserLoginView; // View

    private Handler mUIHandler = new Handler();


    public UserLoginPresenter(IUserLoginView mUserLoginView) {
        this.mUserLoginView = mUserLoginView;
        mUserBiz = new UserBiz();
    }

    public void login() {
        mUserLoginView.showLoading();
        mUserBiz.login(mUserLoginView.getUserName(), mUserLoginView.getPassword(), new OnUserLoginListener() {
            @Override
            public void loginSuccess(User user) {
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mUserLoginView.toMainActivity();
                        mUserLoginView.hideLoading();
                    }
                });
            }

            @Override
            public void loginFail() {
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mUserLoginView.showFailedError();
                        mUserLoginView.hideLoading();
                    }
                });
            }
        });
    }

}
