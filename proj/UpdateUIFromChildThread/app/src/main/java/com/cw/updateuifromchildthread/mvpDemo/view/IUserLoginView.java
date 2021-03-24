package com.cw.updateuifromchildthread.mvpDemo.view;

/**
 * Create by robin On 21-3-24
 */
public interface IUserLoginView {
    String getUserName();
    String getPassword();

    void clearUserName();
    void clearPassword();

    void showLoading();
    void hideLoading();

    void toMainActivity();
    void showFailedError();
}
