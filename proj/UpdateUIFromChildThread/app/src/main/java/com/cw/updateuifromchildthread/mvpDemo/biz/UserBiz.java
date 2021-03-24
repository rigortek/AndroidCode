package com.cw.updateuifromchildthread.mvpDemo.biz;

import com.cw.updateuifromchildthread.mvpDemo.bean.User;

import java.util.concurrent.TimeUnit;

/**
 * Create by robin On 21-3-24
 */
public class UserBiz implements IUserBiz {
    @Override
    public void login(String userName, String password, OnUserLoginListener loginListener) {
        // mock login action
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                }

                if ("fakename".equals(userName) && "fakepwd".equals(password)) {
                    User user = new User();
                    user.setUserName(userName);
                    user.setPassword(password);
                    loginListener.loginSuccess(user);
                } else {
                    loginListener.loginFail();
                }
            }
        }).start();

    }
}
