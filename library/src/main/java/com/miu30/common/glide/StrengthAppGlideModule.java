package com.miu30.common.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.module.LibraryGlideModule;
import com.miu30.common.glide.ftp.FtpUrl;
import com.miu30.common.glide.ftp.FtpUrlLoader;

import java.io.InputStream;

/**
 * 作者：wanglei on 2019/6/24.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 配置 GlideModule
 */
@GlideModule
public class StrengthAppGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        /*
         * 让 Glide 支持使用 FTP 加载图片
         * 使用方法如下：
         * FtpUrl url = new FtpUrl("ftp://10.150.15.237:21/PicPath/photo.jpg", "wang", "123");
         * GlideApp.with(context).load(url).into(imageView);
         */
        registry.prepend(FtpUrl.class, InputStream.class, new FtpUrlLoader.Factory());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
