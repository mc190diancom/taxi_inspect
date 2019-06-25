package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;

import dagger.Lazy;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.util.MapUtil;
import com.miu30.common.util.RxUtils;
import com.miu360.legworkwrit.mvp.contract.CaseSignContract;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarForm;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormID;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.WebViewFactory;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@ActivityScope
public class CaseSignPresenter extends BasePresenter<CaseSignContract.Model, CaseSignContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    Lazy<WebViewFactory> webViewFactory;

    private Activity activity;
    public static final String RECORD_ITEM = "record_item";


    @Inject
    public CaseSignPresenter(CaseSignContract.Model model, CaseSignContract.View rootView) {
        super(model, rootView);
    }

    public void init(Activity activity){
        this.activity = activity;
        preView();
    }

    public void preView() {
        ParentQ parentQ = activity.getIntent().getParcelableExtra(RECORD_ITEM);

        if (parentQ == null) {
            mRootView.showMessage("无效的文件");
            return;
        }

        webViewFactory.get().preView(parentQ);
    }

    public void uploadSignFile(File file, final String id) {
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        paramsMap.put("wfid",id);
        paramsMap.put("fileType","txt");
        paramsMap.put("wfType", Config.T_LIVERECORD);

        Map<String, Object> map = new MapUtil().getMap("upLoadXwblPhoto", BaseData.gson.toJson(paramsMap));

        mModel.uploadSignFile(map, MapUtil.getFilePart(file))
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<Void> voidResult) {
                        if (voidResult.ok()) {
                            System.out.println("uploadSignFile:成功");
                           // mRootView.uploadSuccess();
                        } else {
                            System.out.println("uploadSignFile:失败");
                            //mRootView.uploadFailure(voidResult.getMsg());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
