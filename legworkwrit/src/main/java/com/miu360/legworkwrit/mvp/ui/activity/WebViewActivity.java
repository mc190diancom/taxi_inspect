package com.miu360.legworkwrit.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.config.Config;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerWebViewActivityComponent;
import com.miu360.legworkwrit.di.module.WebViewActivityModule;
import com.miu360.legworkwrit.mvp.contract.WebViewActivityContract;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.mvp.model.entity.WifiConfig;
import com.miu360.legworkwrit.mvp.presenter.WebViewActivityPresenter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.miu360.legworkwrit.util.TimeTool;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class WebViewActivity extends BaseMvpActivity<WebViewActivityPresenter> implements WebViewActivityContract.View {

    @BindView(R2.id.tv_print)
    TextView tvPrint;
    @BindView(R2.id.tv_current_choose_printer)
    TextView tvCurrentChoosePrinter;

    @Inject
    HeaderHolder header;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerWebViewActivityComponent
                .builder()
                .appComponent(appComponent)
                .webViewActivityModule(new WebViewActivityModule(this))
                .build()
                .inject(this);
    }

    @SuppressLint("NewApi")
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        WebView.enableSlowWholeDocumentDraw();
        return R.layout.activity_web_view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        boolean isPreView = getIntent().getBooleanExtra("isPreview", false);//是否只是预览
        if (isPreView) {
            tvPrint.setVisibility(View.GONE);
            tvCurrentChoosePrinter.setVisibility(View.GONE);
            header.init(self, "文书预览");
        } else {
            header.init(self, "文书打印");
            header.setUpRightTextBtn("选择打印机", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.choosePrinter();
                }
            });
        }
        assert mPresenter != null;
        mPresenter.init(self, isPreView);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onCurrentChoosePrinter(WifiConfig config) {
        //do nothing...
    }

    @Override
    @SuppressWarnings("all")
    public void onCurrentConnectPrinter(WifiConfig config) {
        if (config != null) {
            tvCurrentChoosePrinter.setText("当前连接的打印机：" + config.getSn());
        }
    }

    @OnClick(R2.id.tv_print)
    public void onViewClicked() {
        assert mPresenter != null;
        mPresenter.choosePages();
    }

    /*
     * 现场检查笔录(路检)
     */
    public static Intent getIntent(Activity activity, LiveCheckRecordQ liveCheckRecordQ, boolean isPreview) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        liveCheckRecordQ.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.LIVERECORD + ".html");
        liveCheckRecordQ.setFileName(Config.LIVERECORD + ".jpg");
        if (!TextUtils.isEmpty(liveCheckRecordQ.getBJCSJ()) && liveCheckRecordQ.getBJCSJ().length() > 13) {
            liveCheckRecordQ.setBJCSJ(TimeTool.parseStr(liveCheckRecordQ.getBJCSJ()));
        }
        if (!TextUtils.isEmpty(liveCheckRecordQ.getJCSJ1()) && liveCheckRecordQ.getJCSJ1().length() > 13) {
            liveCheckRecordQ.setJCSJ1(TimeTool.parseStr(liveCheckRecordQ.getJCSJ1()));
        }
        if (!TextUtils.isEmpty(liveCheckRecordQ.getJCSJ2()) && liveCheckRecordQ.getJCSJ2().length() > 13) {
            liveCheckRecordQ.setJCSJ2(TimeTool.parseStr(liveCheckRecordQ.getJCSJ2()));
        }
        if (!TextUtils.isEmpty(liveCheckRecordQ.getZFSJ()) && liveCheckRecordQ.getZFSJ().length() > 13) {
            liveCheckRecordQ.setZFSJ(TimeTool.parseStr(liveCheckRecordQ.getZFSJ()));
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, liveCheckRecordQ);
        intent.putExtra("isPreview", isPreview);
        intent.putExtra("needChoosePage", false);
        return intent;
    }

    /*
     * 行政处罚决定书
     */
    public static Intent getIntent(Activity activity, AdministrativePenalty administrativePenalty, boolean isPreview) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        administrativePenalty.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.ADMINISTRATIVE + ".html");
        administrativePenalty.setFileName(Config.ADMINISTRATIVE + ".jpg");
        if (!TextUtils.isEmpty(administrativePenalty.getBCSJ()) && administrativePenalty.getBCSJ().length() > 13) {
            administrativePenalty.setBCSJ(TimeTool.parseStr(administrativePenalty.getBCSJ()));
        }
        if (!TextUtils.isEmpty(administrativePenalty.getSDSJ()) && administrativePenalty.getSDSJ().length() > 13) {
            administrativePenalty.setSDSJ(TimeTool.parseStr(administrativePenalty.getSDSJ()));
        }
        if (!TextUtils.isEmpty(administrativePenalty.getQSSJ()) && administrativePenalty.getQSSJ().length() > 13) {
            administrativePenalty.setQSSJ(TimeTool.parseStr(administrativePenalty.getQSSJ()));
        }
        if (!TextUtils.isEmpty(administrativePenalty.getXZJGSJ()) && administrativePenalty.getXZJGSJ().length() > 13) {
            administrativePenalty.setXZJGSJ(TimeTool.parseStr(administrativePenalty.getXZJGSJ()));
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, administrativePenalty);
        intent.putExtra("isPreview", isPreview);
        intent.putExtra("needChoosePage", true);
        return intent;
    }

    /*
     * 先行登记通知书
     */
    public static Intent getIntent(Activity activity, FristRegisterQ fristRegisterQ, boolean isPreview) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        fristRegisterQ.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.FRISTREGISTER + ".html");
        fristRegisterQ.setFileName(Config.FRISTREGISTER + ".jpg");
        if (!TextUtils.isEmpty(fristRegisterQ.getJDSJ()) && fristRegisterQ.getJDSJ().length() > 13) {
            fristRegisterQ.setJDSJ(TimeTool.parseStr(fristRegisterQ.getJDSJ()));
        }
        if (!TextUtils.isEmpty(fristRegisterQ.getXZJGSJ()) && fristRegisterQ.getXZJGSJ().length() > 13) {
            fristRegisterQ.setXZJGSJ(TimeTool.parseStr(fristRegisterQ.getXZJGSJ()));
        }
        if (!TextUtils.isEmpty(fristRegisterQ.getQSSJ()) && fristRegisterQ.getQSSJ().length() > 13) {
            fristRegisterQ.setQSSJ(TimeTool.parseStr(fristRegisterQ.getQSSJ()));
        }
        if (!TextUtils.isEmpty(fristRegisterQ.getZFSJ()) && fristRegisterQ.getZFSJ().length() > 13) {
            fristRegisterQ.setZFSJ(TimeTool.parseStr(fristRegisterQ.getZFSJ()));
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, fristRegisterQ);
        intent.putExtra("isPreview", isPreview);
        intent.putExtra("needChoosePage", true);
        return intent;
    }

    /*
     * 扣押车辆决定书
     */
    public static Intent getIntent(Activity activity, DetainCarDecideQ detainCarDecideQ, boolean isPreview) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        detainCarDecideQ.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.CARDECIDE + ".html");
        detainCarDecideQ.setFileName(Config.CARDECIDE + ".jpg");
        if (!TextUtils.isEmpty(detainCarDecideQ.getJDSJ()) && detainCarDecideQ.getJDSJ().length() > 13) {
            detainCarDecideQ.setJDSJ(TimeTool.parseStr(detainCarDecideQ.getJDSJ()));
        }
        if (!TextUtils.isEmpty(detainCarDecideQ.getZFSJ()) && detainCarDecideQ.getZFSJ().length() > 13) {
            detainCarDecideQ.setZFSJ(TimeTool.parseStr(detainCarDecideQ.getZFSJ()));
        }
        if (!TextUtils.isEmpty(detainCarDecideQ.getXZJGSJ()) && detainCarDecideQ.getXZJGSJ().length() > 13) {
            detainCarDecideQ.setXZJGSJ(TimeTool.parseStr(detainCarDecideQ.getXZJGSJ()));
        }
        if (!TextUtils.isEmpty(detainCarDecideQ.getQSSJ()) && detainCarDecideQ.getQSSJ().length() > 13) {
            detainCarDecideQ.setQSSJ(TimeTool.parseStr(detainCarDecideQ.getQSSJ()));
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, detainCarDecideQ);
        intent.putExtra("isPreview", isPreview);
        intent.putExtra("needChoosePage", true);
        return intent;
    }

    /*
     * 现场笔录
     */
    public static Intent getIntent(Activity activity, LiveTranscript liveTranscript, boolean isPreview) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        liveTranscript.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.LIVETRANSCRIPT + ".html");
        liveTranscript.setFileName(Config.LIVETRANSCRIPT + ".jpg");
        if (!TextUtils.isEmpty(liveTranscript.getSSSJ1()) && liveTranscript.getSSSJ1().length() > 13) {
            liveTranscript.setSSSJ1(TimeTool.parseStr(liveTranscript.getSSSJ1()));
        }
        if (!TextUtils.isEmpty(liveTranscript.getSSSJ2()) && liveTranscript.getSSSJ2().length() > 13) {
            liveTranscript.setSSSJ2(TimeTool.parseStr(liveTranscript.getSSSJ2()));
        }
        if (!TextUtils.isEmpty(liveTranscript.getDSRQZSJ()) && liveTranscript.getDSRQZSJ().length() > 13) {
            liveTranscript.setDSRQZSJ(TimeTool.parseStr(liveTranscript.getDSRQZSJ()));
        }
        if (!TextUtils.isEmpty(liveTranscript.getZFSJ()) && liveTranscript.getZFSJ().length() > 13) {
            liveTranscript.setZFSJ(TimeTool.parseStr(liveTranscript.getZFSJ()));
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, liveTranscript);
        intent.putExtra("isPreview", isPreview);
        intent.putExtra("needChoosePage", false);
        return intent;
    }

    /*
     * 谈话通知书
     */
    public static Intent getIntent(Activity activity, TalkNoticeQ talkNoticeQ, boolean isPreview) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        talkNoticeQ.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.TALKNOTICE + ".html");
        talkNoticeQ.setFileName(Config.TALKNOTICE + ".jpg");
        if (!TextUtils.isEmpty(talkNoticeQ.getXZJGSJ()) && talkNoticeQ.getXZJGSJ().length() > 13) {
            talkNoticeQ.setXZJGSJ(TimeTool.parseStr(talkNoticeQ.getXZJGSJ()));
        }
        if (!TextUtils.isEmpty(talkNoticeQ.getQSSJ()) && talkNoticeQ.getQSSJ().length() > 13) {
            talkNoticeQ.setQSSJ(TimeTool.parseStr(talkNoticeQ.getQSSJ()));
        }
        if (!TextUtils.isEmpty(talkNoticeQ.getSDSJ()) && talkNoticeQ.getSDSJ().length() > 13) {
            talkNoticeQ.setSDSJ(TimeTool.parseStr(talkNoticeQ.getSDSJ()));
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, talkNoticeQ);
        intent.putExtra("isPreview", isPreview);
        intent.putExtra("needChoosePage", true);
        return intent;
    }

    /*
     * 车辆扣押交接单
     */
    public static Intent getIntent(Activity activity, DetainCarFormQ detainCarFormQ, boolean isPreview) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        detainCarFormQ.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.CARFORM + ".html");
        detainCarFormQ.setFileName(Config.CARFORM + ".jpg");
        if (!TextUtils.isEmpty(detainCarFormQ.getKCSJ()) && detainCarFormQ.getKCSJ().length() > 13) {
            detainCarFormQ.setKCSJ(TimeTool.parseStr(detainCarFormQ.getKCSJ()));
        }
        if (!TextUtils.isEmpty(detainCarFormQ.getYSSJ()) && detainCarFormQ.getYSSJ().length() > 13) {
            detainCarFormQ.setYSSJ(TimeTool.parseStr(detainCarFormQ.getYSSJ()));
        }
        if (!TextUtils.isEmpty(detainCarFormQ.getZFDWQZSJ()) && detainCarFormQ.getZFDWQZSJ().length() > 13) {
            detainCarFormQ.setZFDWQZSJ(TimeTool.parseStr(detainCarFormQ.getZFDWQZSJ()));
        }
        if (!TextUtils.isEmpty(detainCarFormQ.getYJTL()) && detainCarFormQ.getYJTL().contains("《") && detainCarFormQ.getYJTL().contains("》")) {
            try {
                detainCarFormQ.setYJTL(detainCarFormQ.getYJTL().substring(1, detainCarFormQ.getYJTL().length() - 1));
            } catch (Exception e) {

            }
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, detainCarFormQ);
        intent.putExtra("isPreview", isPreview);
        intent.putExtra("needChoosePage", false);
        return intent;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.release();
    }
}
