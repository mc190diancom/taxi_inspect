package com.miu30.common.glide.exception;

import java.io.IOException;

/**
 * 作者：wanglei on 2019/6/24.
 * 邮箱：forwlwork@gmail.com
 */
public class FtpConnectException extends IOException {

    public FtpConnectException(String host, int port, int replyCode) {
        super("ftp connect exception , server message is " + host + ":" + port + " , replyCode is " + replyCode);
    }

}
