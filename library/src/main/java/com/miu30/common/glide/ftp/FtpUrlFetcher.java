package com.miu30.common.glide.ftp;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.util.LogTime;
import com.miu30.common.glide.exception.FtpConnectException;
import com.miu30.common.glide.exception.FtpLoginFailureException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：wanglei on 2019/6/24.
 * 邮箱：forwlwork@gmail.com
 */
@SuppressLint("LogNotTimber")
public class FtpUrlFetcher implements DataFetcher<InputStream> {
    private static final String TAG = "FtpUrlFetcher";
    private FtpUrl ftpUrl;
    private int timeout;
    private FTPClient client;
    private InputStream inputStream;

    private boolean isCancelled;

    FtpUrlFetcher(FtpUrl ftpUrl, int timeout) {
        this.ftpUrl = ftpUrl;
        this.timeout = timeout;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        long startTime = LogTime.getLogTime();
        try {
            InputStream result = loadDataWithRedirects(this.ftpUrl);
            //数据加载成功
            callback.onDataReady(result);
        } catch (Exception e) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "Failed to load data for ftp url", e);
            }
            //数据加载失败
            callback.onLoadFailed(e);
        } finally {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Finished ftp url fetcher fetch in " + LogTime.getElapsedMillis(startTime));
            }
        }
    }

    private InputStream loadDataWithRedirects(FtpUrl url) throws IOException {
        client = new FTPClient();
        client.setDefaultTimeout(timeout);
        client.setConnectTimeout(timeout);
        client.setDataTimeout(timeout);
        client.connect(url.getHost(), url.getPort());

        int replyCode = client.getReplyCode();
        if (FTPReply.isPositiveCompletion(replyCode)) {
            boolean isSuccess = client.login(url.getUsername(), url.getPassword());
            if (isSuccess) {
                //use passive mode to pass firewalls.
                client.enterLocalPassiveMode();
                //set data transfer mode.
                client.setFileType(FTP.BINARY_FILE_TYPE);
                FTPFile[] files = client.listFiles(url.getPath());

                if (files != null && files.length > 0) {
                    inputStream = client.retrieveFileStream(url.getPath());
                    return isCancelled ? null : inputStream;
                } else {
                    throw new FileNotFoundException("file not found from ftp server , the path : " + url.getPath());
                }
            } else {
                throw new FtpLoginFailureException(url.getHost(), url.getPort());
            }
        } else {
            throw new FtpConnectException(url.getHost(), url.getPort(), replyCode);
        }

    }

    //释放资源
    @Override
    public void cleanup() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inputStream = null;
            }
        }

        disConnect();
    }

    private void disConnect() {
        if (client != null && client.isConnected()) {
            try {
                client.logout();
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                client = null;
            }
        }
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }

}
