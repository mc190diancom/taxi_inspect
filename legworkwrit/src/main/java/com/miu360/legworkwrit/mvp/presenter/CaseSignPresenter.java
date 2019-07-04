package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.print.PdfPrint;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu30.common.async.Result;
import com.miu30.common.config.Config;
import com.miu30.common.util.MapUtil;
import com.miu30.common.util.MyProgressDialog;
import com.miu30.common.util.RxUtils;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu360.legworkwrit.mvp.contract.CaseSignContract;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.mvp.ui.adapter.GridViewAdapter;
import com.miu360.legworkwrit.util.MyWebViewUtil;
import com.miu360.legworkwrit.util.SignUtil;
import com.miu360.legworkwrit.util.WebViewFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.org.bjca.anysign.android.api.core.domain.SignatureType;
import dagger.Lazy;
import io.reactivex.Observable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

@ActivityScope
public class CaseSignPresenter extends BasePresenter<CaseSignContract.Model, CaseSignContract.View>  implements  SignUtil.SignCallBack {

    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    Lazy<WebViewFactory> webViewFactory;

    private Activity activity;
    private static final String RECORD_ITEM = "record_item";
    private String id;//文书id
    private ParentQ parentQ;
    private WebView mWebView;
    public final int SIGN_SIGN =1,SIGN_COMMENT =2,SIGN_COMPLETE =3;
    private String uploadType,uploadName;

    @Inject
    public CaseSignPresenter(CaseSignContract.Model model, CaseSignContract.View rootView) {
        super(model, rootView);
    }

    public void init(Activity activity,WebView wvDocument){
        this.activity = activity;
        this.mWebView = wvDocument;
        preView();
    }

    private void preView() {
        parentQ = activity.getIntent().getParcelableExtra(RECORD_ITEM);
        if (parentQ == null) {
            mRootView.showMessage("无效的文件");
            return;
        }
        webViewFactory.get().preView(parentQ);
    }

    //初始化签名参数
    public void initSignParam(ParentQ parentQ,String type,String sfzh) {
        String id,dsr;
        if(Config.LIVERECORD.equals(type)){
            LiveCheckRecordQ documents = (LiveCheckRecordQ)parentQ;
            id = documents.getID();
            dsr = documents.getBJCR();
            uploadType = Config.T_LIVERECORD;
            uploadName = Config.STR_LIVERECORD;
        }else if(Config.LIVETRANSCRIPT.equals(type)){
            LiveTranscript documents = (LiveTranscript)parentQ;
            id = documents.getID();
            dsr = documents.getDSR();
            uploadType = Config.T_LIVETRANSCRIPT;
            uploadName = Config.STR_LIVETRANSCRIPT;
        }else if(Config.ADMINISTRATIVE.equals(type)){
            AdministrativePenalty documents = (AdministrativePenalty)parentQ;
            id = documents.getID();
            dsr = documents.getDSR();
            uploadType = Config.T_ADMINISTRATIVE;
            uploadName = Config.STR_ADMINISTRATIVE;
        }else if(Config.FRISTREGISTER.equals(type)){
            FristRegisterQ documents = (FristRegisterQ)parentQ;
            id = documents.getID();
            dsr = documents.getDSR();
            uploadType = Config.T_FRISTREGISTER;
            uploadName = Config.STR_FRISTREGISTER;
        }else if(Config.CARDECIDE.equals(type)){
            DetainCarDecideQ documents = (DetainCarDecideQ)parentQ;
            id = documents.getID();
            dsr = documents.getDSR();
            uploadType = Config.T_CARDECIDE;
            uploadName = Config.STR_CARDECIDE;
        }else if(Config.CARFORM.equals(type)){
            DetainCarFormQ documents = (DetainCarFormQ)parentQ;
            id = documents.getID();
            dsr = documents.getCZXM();
            uploadType = Config.T_CARFORM;
            uploadName = Config.STR_CARFORM;
        }else{
            TalkNoticeQ documents = (TalkNoticeQ)parentQ;
            id = documents.getID();
            dsr = documents.getDSR();
            uploadType = Config.T_TALKNOTICE;
            uploadName = Config.STR_TALKNOTICE;
        }
        this.id = id;
        initPdf(mWebView);
        if(!TextUtils.isEmpty(sfzh)){
            SignUtil.getInstance(activity).initApi(id,Config.path_root+id+".pdf",this,type);
            SignUtil.getInstance(activity).setSignObject(dsr, sfzh);
            if(Config.LIVERECORD.equals(type) || Config.LIVETRANSCRIPT.equals(type)){
                SignUtil.getInstance(activity).setNotation2(dsr, sfzh);//现场检查笔录和现场笔录需要签署“情况属实”
            }
        }
    }


    private void initPdf(WebView wvDocument) {
        new PdfPrint().printPdfFromWebView(wvDocument, new File(Config.path_root), id + ".pdf", new PdfPrint.Callback() {
            @Override
            public void onSuccess(File pdfFile) {
                System.out.println("printPdfFromWebView：成功——"+pdfFile.toString());
            }

            @Override
            public void onFailure(String message) {
                System.out.println("printPdfFromWebView：失败——"+message);
            }
        });
    }


    private Observable<Result<Void>> httpRequest1,httpRequest2,httpRequest3,httpRequest4,httpRequest5;
    /**
     * 开始上传文件，接下来有几个接口串联，为什么要在客户端串联，因为后台不给写，呵呵
     */
    private void uploadSignFile() {
        File file = new File(Config.path_root+id+".pdf");
        if(!file.exists()){
            UIUtils.toast(activity,"pdf未创建", Toast.LENGTH_SHORT);
            return;
        }
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        paramsMap.put("type","uploadsignfile");
        paramsMap.put("wfid",id);
        paramsMap.put("fileType","pdf");
        paramsMap.put("wfType", uploadType);
        Map<String, Object> map = new MapUtil().getMapKeyAndValue(paramsMap);
        httpRequest1 =  mModel.uploadSignFile(map, MapUtil.getFilePart(file))
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView));
    }

    private void asyncSignAddJob(){
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        paramsMap.put("wsid",id);
        paramsMap.put("wsName",uploadName);
        paramsMap.put("msspid", "720abe3f23f2f3f7fab0880a56bce6ed461187df197a2f6c3819452c3505f766");
        httpRequest2 =  mModel.asyncSignAddJob(paramsMap)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView));
    }


    private void pdfsign(){
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        paramsMap.put("wsid",id);
        httpRequest3=  mModel.pdfsign(paramsMap)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView));
    }

    /**
     * 开始上传加密包
     */
    private void upGenData() {
        File file = new File(Config.path_root+id+".txt");
        if(!file.exists()){
            UIUtils.toast(activity,"数据包未创建", Toast.LENGTH_SHORT);
            return;
        }
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        paramsMap.put("type","uploadsignfile");
        paramsMap.put("wfid",id);
        paramsMap.put("fileType","txt");
        paramsMap.put("wfType", uploadType);
        Map<String, Object> map = new MapUtil().getMapKeyAndValue(paramsMap);
        httpRequest4 =  mModel.uploadSignFile(map, MapUtil.getFilePart(file))
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView));
    }

    private void testAnySignEncPackage(){
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        paramsMap.put("wsid",id);
        httpRequest5 = mModel.testAnySignEncPackage(paramsMap)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView));
    }

    //上传图片
    public void uploadImage() {
        screenshots();
        if(picFile == null || !picFile.exists()){
            UIUtils.toast(activity,"图片未创建", Toast.LENGTH_SHORT);
            return;
        }
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        paramsMap.put("type","uploadsignfile");
        paramsMap.put("wfid",id);
        paramsMap.put("fileType","jpg");
        paramsMap.put("wfType", Config.T_LIVERECORD);
        Map<String, Object> map = new MapUtil().getMapKeyAndValue(paramsMap);
        mModel.uploadSignFile(map, MapUtil.getFilePart(picFile))
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<Void> voidResult) {
                        if (voidResult.ok()) {
                        } else {
                        }
                    }
                });
    }

    int successTime;
    //执法人员签章和添加公章
    public void Sign(){
        successTime = 0;
        uploadSignFile();
        asyncSignAddJob();
        pdfsign();
        Observable.concat(httpRequest1,httpRequest2,httpRequest3).subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {
            @Override
            public void onNextResult(Result<Void> voidResult) {
                System.out.println("SignNote:code"+voidResult.getError()+",msg="+voidResult.getMsg());
                if (voidResult.ok()) {
                    successTime ++;
                    if( successTime ==3){
                        zfryAndSignature();
                    }
                } else {
                    mRootView.uploadSuccess(false);
                }
            }
        });
    }

    //被执法人员签章
    private void zfryAndSignature(){
        upGenData();
        testAnySignEncPackage();
        Observable.concat(httpRequest4,httpRequest5).subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {
            @Override
            public void onNextResult(Result<Void> voidResult) {
                System.out.println("SignNote:code"+voidResult.getError()+",msg="+voidResult.getMsg());
                if (voidResult.ok()) {
                    successTime ++;
                    if(successTime ==5){
                        File file = new File(Config.path_root+id+".txt");
                        File file2 = new File(Config.path_root+id+".pdf");
                        if(file.exists() && file2.exists()){
                            file.delete();
                            file2.delete();
                        }
                        mRootView.uploadSuccess(true);
                    }
                } else {
                    mRootView.uploadSuccess(false);
                }
            }
        });
    }

    private void screenshots(){
        webViewFactory.get().configPrintWebView();
        int delay;
        if (Build.VERSION.SDK_INT >= 22) {
            delay = 300;
        } else {
            delay = 500;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webViewFactory.get().toFile(new MyWebViewUtil.OnScreenshotListener() {
                    @Override
                    public void onScreenshot(File capture) {
                        picFile = capture;
                    }
                });
            }
        }, delay);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private List<String> mList1 = new ArrayList<>();
    private List<String> mList2 = new ArrayList<>();

    public GridViewAdapter getGVAdapter(String type) {
        mList1.add("被检查人");
        if (Config.LIVERECORD.equals(type) || Config.LIVETRANSCRIPT.equals(type)) {
            mList1.add("情况属实");
        }
        return new GridViewAdapter(mList1,activity);
    }

    public GridViewAdapter getGV2Adapter() {
        mList2.add("执法人员1");
        mList2.add("执法人员2");
        return new GridViewAdapter(mList2,activity);
    }

    public List<String> getList(){
        return mList1;
    }

    private File picFile;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public void signSuccess(SignatureType type) {
        int result = SIGN_SIGN;
        if(SignatureType.SIGN_TYPE_SIGN == type && mList1.contains("被检查人")){
            mList1.remove("被检查人");
            mList2.add("被检查人");
            result = SIGN_SIGN;
        }else if(SignatureType.SIGN_TYPE_COMMENT == type && mList1.contains("情况属实")){
            mList1.remove("情况属实");
            mList2.add("情况属实");
            result = SIGN_COMMENT;
        }
        if(mList1 == null || mList1.size() == 0){
            result = SIGN_COMPLETE;
        }

        mRootView.signResult(result);
    }

    @Override
    public void signFailure() {

    }
}
