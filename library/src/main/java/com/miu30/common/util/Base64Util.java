package com.miu30.common.util;

import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Base64Util {
    /**
     * @param bse64Str base64编码字符串
     * @param path   图片路径-具体到文件
     */
    public static boolean generateImage(String bse64Str, String path) {
        //对字节数组字符串进行Base64解码并生成图片
        if (bse64Str == null) { //图像数据为空
            return false;
        }
        try {
            //Base64解码
            byte[] b = Base64.decode(bse64Str,Base64.NO_WRAP);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            // 新生成的jpg图片
            // 新图片的文件夹, 如果没有, 就创建
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 文件夹现在存在了, 可以在此文件夹下创建图片了
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(file);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void saveStrToFile(String srcPath, String desPath) {
        String s = imageToBase64(srcPath);
        File file = new File(desPath);
        if(!file.exists()){
            File dir = new File(file.getParent());
            dir.mkdirs();
        }
        try {
            file.createNewFile();
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(s.getBytes());
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    private String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }
}
