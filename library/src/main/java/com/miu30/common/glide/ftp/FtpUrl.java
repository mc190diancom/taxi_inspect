package com.miu30.common.glide.ftp;

import android.support.annotation.NonNull;

import com.bumptech.glide.load.Key;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

/**
 * 作者：wanglei on 2019/6/24.
 * 邮箱：forwlwork@gmail.com
 */
public class FtpUrl implements Key {
    private static final String FTP_SCHEME = "ftp";

    private URL url;
    private String path;
    private String username;
    private String password;
    private String ftpUrlStr;

    private volatile byte[] cacheKeyBytes;

    /**
     * create FtpUrl by host、port、path、username、password
     *
     * @param host     主机名
     * @param port     端口
     * @param path     格式：/PicPath/photo.jpg
     * @param username 用户名
     * @param password 密码
     * @throws MalformedURLException 主机名或端口号不合法
     */
    public FtpUrl(String host, int port, String path, String username, String password) throws MalformedURLException {
        this.url = new URL(FTP_SCHEME, host, port, "");
        this.path = path;
        this.username = username;
        this.password = password;

        getFtpUrlStr();
    }

    /**
     * create FtpUrl by spec、username、password
     *
     * @param spec     格式 : ftp://10.212.160.152:12021/PicPath/2019-06-19/0004057274f6319e45549cef78f72ef31f08.jpg
     * @param username 用户名
     * @param password 密码
     */
    public FtpUrl(String spec, String username, String password) throws MalformedURLException {
        this.url = new URL(spec);
        this.path = this.url.getPath();
        this.username = username;
        this.password = password;

        if (!FTP_SCHEME.equals(this.url.getProtocol())) {
            throw new MalformedURLException("FtpUrl protocol must be ftp.");
        }

        getFtpUrlStr();
    }

    private void getFtpUrlStr() {
        this.ftpUrlStr = "%s://%s:%s@%s:%d/%s";
        this.ftpUrlStr = String.format(this.ftpUrlStr, url.getProtocol(), username, password, url.getHost(), url.getPort(), path);
    }

    public String getHost() {
        return this.url.getHost();
    }

    public int getPort() {
        return this.url.getPort();
    }

    public String getPath() {
        return this.path;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    private String getCacheKey() {
        return this.ftpUrlStr;
    }

    public String getProtocol() {
        return this.url.getProtocol();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        if (cacheKeyBytes == null) {
            cacheKeyBytes = getCacheKey().getBytes(CHARSET);
        }

        messageDigest.update(cacheKeyBytes);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FtpUrl) {
            return getCacheKey().equals(((FtpUrl) obj).getCacheKey());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getCacheKey().hashCode();
    }
}
