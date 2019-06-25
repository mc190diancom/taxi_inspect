package com.miu30.common.glide.ftp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

/**
 * 作者：wanglei on 2019/6/24.
 * 邮箱：forwlwork@gmail.com
 */
public class FtpUrlLoader implements ModelLoader<FtpUrl, InputStream> {
    private static final String FTP_SCHEME = "ftp";

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull FtpUrl url, int width, int height, @NonNull Options options) {
        return new LoadData<>(url, new FtpUrlFetcher(url, 5_000));
    }

    @Override
    public boolean handles(@NonNull FtpUrl url) {
        return FTP_SCHEME.equals(url.getProtocol());
    }

    public static class Factory implements ModelLoaderFactory<FtpUrl, InputStream> {

        @NonNull
        @Override
        public ModelLoader<FtpUrl, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new FtpUrlLoader();
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }
}
