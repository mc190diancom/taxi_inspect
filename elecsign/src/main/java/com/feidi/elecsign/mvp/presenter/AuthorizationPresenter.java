package com.feidi.elecsign.mvp.presenter;

import android.app.Application;

import com.feidi.elecsign.mvp.contract.AuthorizationContract;
import com.feidi.elecsign.mvp.model.entity.AuthMy;
import com.feidi.elecsign.mvp.model.entity.MyAuth;
import com.feidi.elecsign.mvp.ui.adapter.AuthMyAdapter;
import com.feidi.elecsign.mvp.ui.adapter.MyAuthAdapter;
import com.feidi.elecsign.util.DialogUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.ui.entity.queryZFRYByDWMC;
import com.miu30.common.ui.view.ThreeStateSwitch;
import com.miu30.common.util.JacksonUtil;
import com.miu30.common.util.MapUtil;
import com.miu30.common.util.RxUtils;
import com.miu30.common.util.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/14/2019 18:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class AuthorizationPresenter extends BasePresenter<AuthorizationContract.Model, AuthorizationContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    List<queryZFRYByDWMC> queryZFRYByDWMCS;

    @Inject
    public AuthorizationPresenter(AuthorizationContract.Model model, AuthorizationContract.View rootView) {
        super(model, rootView);
    }

    public void showAuthDescriptionDialog() {
        DialogUtils.showAuthDescriptionDialog(mAppManager.getCurrentActivity());
    }

    public MyAuthAdapter getMyAuthAdapter() {
        //测试数据
        List<MyAuth> myAuths = new ArrayList<>();
        myAuths.add(new MyAuth("李毅", "00012221", "3天后到期", ThreeStateSwitch.STATE_ON));
        myAuths.add(new MyAuth("三三", "00012024", "2天后到期", ThreeStateSwitch.STATE_CRITICAL));
        myAuths.add(new MyAuth("四四", "01012021", "1天后到期", ThreeStateSwitch.STATE_ON));
        myAuths.add(new MyAuth("呜呜", "00013021", "今日到期", ThreeStateSwitch.STATE_OFF));
        myAuths.add(new MyAuth("溜溜", "10012021", "", ThreeStateSwitch.STATE_ON));
        return new MyAuthAdapter(myAuths);
    }

    public AuthMyAdapter getAuthMyAdapter() {
        //测试数据
        List<AuthMy> authMys = new ArrayList<>();
        authMys.add(new AuthMy("李毅", "00012221", "2019-12-22前有效"));
        authMys.add(new AuthMy("三三", "00012024", "2019-12-22前有效"));
        authMys.add(new AuthMy("四四", "01012021", "2019-12-22前有效"));
        authMys.add(new AuthMy("呜呜", "00013021", "2019-12-22前有效"));
        authMys.add(new AuthMy("溜溜", "10012021", "2019-12-22前有效"));
        return new AuthMyAdapter(authMys);
    }

    private static String getJsonStr(Object o) {
        BaseData.trimEmptyToNull(o);
        return JacksonUtil.writeEntity2JsonStr(o);
    }

    /*
     * 获取执法人员2
     */
    public void getZfry2() {
        queryZFRYByDWMC info = new queryZFRYByDWMC();
        info.setZFDWMC(MiuBaseApp.user.getString("zfdwmc", null));
        Map<String, Object> map = new MapUtil().getMap("query_Zfry_Name", getJsonStr(info));
        mModel.getCheZuList(map)
                .compose(RxUtils.<Result<List<queryZFRYByDWMC>>>applySchedulers(mRootView,true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<queryZFRYByDWMC>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<queryZFRYByDWMC>> listResult) {
                        System.out.println("queryZFRYByDWMC:"+listResult.getData());
                    }

                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
