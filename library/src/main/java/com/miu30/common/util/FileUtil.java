package com.miu30.common.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.miu30.common.util.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 作者：wanglei on 2018/10/18.
 * 邮箱：forwlwork@gmail.com
 */
public class FileUtil {

    public static File getDir(File parent, String dirName) {
        if (parent == null || !parent.isDirectory() || TextUtils.isEmpty(dirName)) {
            return null;
        }

        File dirFile = new File(parent, dirName);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        return dirFile;
    }

    public static Uri getFileUri(Context context, File file) {
        Uri uri = null;

        if (file != null) {
            if (UIUtils.hasN()) {
                uri = FileProvider.getUriForFile(context, "com.miu360.take_photo", file);
            } else {
                uri = Uri.fromFile(file);
            }
        }

        return uri;
    }

    public static String getPathFromUri(Activity activity, Uri photoUri) {
        String[] filePathColumns = {MediaStore.Images.Media.DATA};

        if (photoUri != null) {
            Cursor cursor = activity.getContentResolver()
                    .query(photoUri, filePathColumns, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                String imagePath = cursor.getString(columnIndex);
                cursor.close();

                return imagePath;
            }
        }

        return "";
    }

    /**
     * 拷贝assert文件到sd卡
     */
    public static void copyAssrert2Sd(Context context,String assert_name, String file_path) {
        File file = new File(file_path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
            OutputStream myOutput = new FileOutputStream(file);
            InputStream myInput = context.getAssets().open(assert_name);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = myInput.read(buffer)) != -1) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
