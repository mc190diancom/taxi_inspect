package com.miu360.legworkwrit.util;

import android.content.Context;

import com.jess.arms.utils.ArmsUtils;
import com.miu30.common.async.Result;
import com.miu30.common.config.Config;
import com.miu30.common.data.FilePreference;
import com.miu30.common.ui.entity.Template;
import com.miu30.common.util.FileUtil;
import com.miu360.legworkwrit.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Murphy on 2018/11/1.
 */
public class WritTemplateUtil {

    private Context mContext;
    private FilePreference fPre;

    public WritTemplateUtil(Context mContext){
        this.mContext = mContext;
    }

    public void init(){
        fPre = new FilePreference(mContext);
        checkTemplate();
        checkTemplateVersion();
    }

    /*
     * 检测模板是否存在本地
     */
    private void checkTemplate() {
        Observable.create(new ObservableOnSubscribe<Void>() { //定义被观察者
            @Override
            public void subscribe(final ObservableEmitter<Void> e){
                checkFileIsExist(Config.LIVERECORD + ".html");
                checkFileIsExist(Config.TALKNOTICE + ".html");
                checkFileIsExist(Config.FRISTREGISTER + ".html");
                checkFileIsExist(Config.CARDECIDE + ".html");
                checkFileIsExist(Config.LIVETRANSCRIPT + ".html");
                checkFileIsExist(Config.ADMINISTRATIVE + ".html");
                checkFileIsExist(Config.CARFORM + ".html");
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    //检测文件是否存在
    private void checkFileIsExist(String pathUrl){
        File file = new File(Config.PATHROOT + "/Android/data/" + mContext.getPackageName() + "/files/"+ pathUrl);
        if(!file.exists()){
            FileUtil.copyAssrert2Sd(mContext,pathUrl,file.getAbsolutePath());
        }
    }

    /*
     * 获取模板服务器的版本号
     */
    private void checkTemplateVersion() {
        ArmsUtils.obtainAppComponentFromContext(mContext)
                .repositoryManager()
                .obtainRetrofitService(MyApis.class)
                .selectTemplateAll("selectTemplateList")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyErrorHandleSubscriber<Result<List<Template>>>(ArmsUtils.obtainAppComponentFromContext(mContext).rxErrorHandler()) {

                    @Override
                    public void onNextResult(Result<List<Template>> result) {
                        System.out.println("Template:"+result.getData()+"-"+result.getMsg());
                        if (result.ok()) {
                            List<Template> templates = result.getData();
                            if(templates == null || templates.isEmpty()){
                                return;
                            }
                            try {
                                downLoadTemplate(templates);
                            }catch (Exception e){
                                System.out.println(e.toString());
                            }
                        }
                    }

                });


    }

    /*
     * 判断是否和服务器的模板版本号一致
     */
    private void downLoadTemplate(List<Template> templates) {
        for (int i = 0; i < templates.size(); i++) {
            if(Config.T_LIVERECORD.contains(templates.get(i).getTABLENAME()) && fPre.getLIVERECORD() != Integer.valueOf(templates.get(i).getVERSION())){
                download(Config.LIVERECORD,Config.SERVER_DOWNLOAD + "?type=downloadTemplate&jsonStr={\"BlID\":\"d6b562595aa04fb6b1d91e288c0f5846\"}");
                fPre.setLIVERECORD(Integer.valueOf(templates.get(i).getVERSION()));
            }
            if(Config.T_TALKNOTICE.contains(templates.get(i).getTABLENAME()) && fPre.getTALKNOTICE() != Integer.valueOf(templates.get(i).getVERSION())){
                download(Config.TALKNOTICE,Config.SERVER_DOWNLOAD + "?type=downloadTemplate&jsonStr={\"BlID\":\"e22671de33bd4e20a8f346f53d2ac44f\"}");
                fPre.setTALKNOTICE(Integer.valueOf(templates.get(i).getVERSION()));
            }
            if(Config.T_FRISTREGISTER.contains(templates.get(i).getTABLENAME()) && fPre.getFRISTREGISTER() != Integer.valueOf(templates.get(i).getVERSION())){
                download(Config.FRISTREGISTER,Config.SERVER_DOWNLOAD + "?type=downloadTemplate&jsonStr={\"BlID\":\"612bd85d96af4cebb264f265d2220e7e\"}");
                fPre.setFRISTREGISTER(Integer.valueOf(templates.get(i).getVERSION()));
            }
            if(Config.T_CARDECIDE.contains(templates.get(i).getTABLENAME()) && fPre.getCARDECIDE() != Integer.valueOf(templates.get(i).getVERSION())){
                download(Config.CARDECIDE,Config.SERVER_DOWNLOAD + "?type=downloadTemplate&jsonStr={\"BlID\":\"16e82e98ef4c4f7e90b08151d20f6f52\"}");
                fPre.setCARDECIDE(Integer.valueOf(templates.get(i).getVERSION()));
            }
            if(Config.T_LIVETRANSCRIPT.contains(templates.get(i).getTABLENAME()) && fPre.getLIVETRANSCRIPT() != Integer.valueOf(templates.get(i).getVERSION())){
                download(Config.LIVETRANSCRIPT,Config.SERVER_DOWNLOAD + "?type=downloadTemplate&jsonStr={\"BlID\":\"e6b230fb5c094aa2a0babc1aaa1eeed7\"}");
                fPre.setLIVETRANSCRIPT(Integer.valueOf(templates.get(i).getVERSION()));
            }
            if(Config.T_ADMINISTRATIVE.contains(templates.get(i).getTABLENAME()) && fPre.getADMINISTRATIVE() != Integer.valueOf(templates.get(i).getVERSION())){
                download(Config.ADMINISTRATIVE,Config.SERVER_DOWNLOAD + "?type=downloadTemplate&jsonStr={\"BlID\":\"575fd68709f246a6b0c068d254a75a04\"}");
                fPre.setADMINISTRATIVE(Integer.valueOf(templates.get(i).getVERSION()));
            }
            if(Config.T_CARFORM.contains(templates.get(i).getTABLENAME()) && fPre.getCARFORM() != Integer.valueOf(templates.get(i).getVERSION())){
                download(Config.CARFORM,Config.SERVER_DOWNLOAD + "?type=downloadTemplate&jsonStr={\"BlID\":\"a51d884d058344889bd7275b09fbab24\"}");
                fPre.setCARFORM(Integer.valueOf(templates.get(i).getVERSION()));
            }
        }
    }

    /*
     * 根据传过来的url，开启异步进行模板下载
     */
    private void download(final String name,final String url){
        Observable.create(new ObservableOnSubscribe<Void>() { //定义被观察者
            @Override
            public void subscribe(final ObservableEmitter<Void> e){
                DownLoadTemplate(name,url);
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    /**
     * 开启post请求下载模板
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void DownLoadTemplate(String name, final String url) {
        File file = new File(Config.PATHROOT + "/Android/data/" + mContext.getPackageName() + "/files/"+ name + ".html");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(12 * 1000);
            connection.setReadTimeout(12 * 1000);
            // 建立实际的连接
            // connection.connect();
            // 获取文件大小
            // 创建输入流
            is = connection.getInputStream();
            fos = new FileOutputStream(file);
            // 缓存
            byte buf[] = new byte[1024];
            int numread;
            // 写入到文件中
            while ((numread = is.read(buf)) != -1) {
                fos.write(buf, 0, numread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

}
