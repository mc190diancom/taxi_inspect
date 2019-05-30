package com.miu30.common.connect.entity;

/**
 * 作者：wanglei on 2019/5/23.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 登录注销请求
 */
public class LogoutRequest implements IMesage {
    private int msgType = LOGOUT;
    private String zfzh;

    public LogoutRequest(String zfzh) {
        this.zfzh = zfzh;
    }
}
