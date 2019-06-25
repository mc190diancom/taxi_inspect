package com.miu30.common.glide.exception;

import java.io.IOException;

/**
 * 作者：wanglei on 2019/6/24.
 * 邮箱：forwlwork@gmail.com
 */
public class FtpLoginFailureException extends IOException {

    public FtpLoginFailureException(String host, int port) {
        super("login ftp server failure , server message is " + host + ":" + port);
    }

}
