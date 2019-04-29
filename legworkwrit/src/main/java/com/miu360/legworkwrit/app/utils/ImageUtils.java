package com.miu360.legworkwrit.app.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 作者：wanglei on 2018/9/19.
 * 邮箱：forwlwork@gmail.com
 */
public class ImageUtils {

    public static String saveViewToImage(View view) {
        if (view == null) {
            return null;
        }

        int width = view.getWidth();
        int height = 1528;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        FileOutputStream fos = null;
        try {
            File outputFile = new File(Environment.getExternalStorageDirectory(), "test.jpg");

            fos = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();

            return outputFile.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }


}
