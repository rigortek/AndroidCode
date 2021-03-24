package com.cw.updateuifromchildthread.mvpDemo.biz;

/**
 * Create by robin On 21-3-24
 */
public interface IUserBiz {
    void login(String userName, String password, OnUserLoginListener loginListener);
}
