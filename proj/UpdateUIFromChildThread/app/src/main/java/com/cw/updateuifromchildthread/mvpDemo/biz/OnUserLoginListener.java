package com.cw.updateuifromchildthread.mvpDemo.biz;

import com.cw.updateuifromchildthread.mvpDemo.bean.User;

/**
 * Create by robin On 21-3-24
 */
public interface OnUserLoginListener {
    void loginSuccess(User user);
    void loginFail();
}
