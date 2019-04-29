package com.miu360.legworkwrit.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jess.arms.di.component.AppComponent;
import com.miu30.common.config.Config;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerWritPrintListComponent;
import com.miu360.legworkwrit.di.module.WritPrintListModule;
import com.miu360.legworkwrit.mvp.contract.WritPrintListContract;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.mvp.presenter.WritPrintListPresenter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public class WritPrintListActivity extends BaseMvpActivity<WritPrintListPresenter> implements WritPrintListContract.View, View.OnClickListener {

    @BindView(R2.id.rb_no1)
    RadioButton rbNo1;
    @BindView(R2.id.rb_no2)
    RadioButton rbNo2;
    @BindView(R2.id.rb_no3)
    RadioButton rbNo3;
    @BindView(R2.id.rb_no4)
    RadioButton rbNo4;
    @BindView(R2.id.rb_no5)
    RadioButton rbNo5;
    @BindView(R2.id.rb_no6)
    RadioButton rbNo6;
    @BindView(R2.id.rb_no7)
    RadioButton rbNo7;
    @BindView(R2.id.rg_writ)
    RadioGroup rgWrit;
    @BindView(R2.id.btn_print)
    Button btnPrint;
    @BindView(R2.id.view1)
    View view1;
    @BindView(R2.id.view2)
    View view2;
    @BindView(R2.id.view3)
    View view3;
    @BindView(R2.id.view4)
    View view4;
    @BindView(R2.id.view5)
    View view5;
    @BindView(R2.id.view6)
    View view6;
    @BindView(R2.id.view7)
    View view7;

    @Inject
    HeaderHolder headerHolder;
    //文书ID
    private String instrumentID;

    private Handler handler;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerWritPrintListComponent
                .builder()
                .appComponent(appComponent)
                .writPrintListModule(new WritPrintListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_writ_print_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        headerHolder.init(self, "文书打印");
        btnPrint.setOnClickListener(this);

        if (mPresenter != null) {
            ArrayList<BlType> types = getIntent().getParcelableArrayListExtra("bl_types");
            init(types);
            Case c = getIntent().getParcelableExtra("case");
            mPresenter.init(this, c, types);
        }
    }

    private void init(ArrayList<BlType> types) {
        for (BlType type : types) {
            switch (type.getCOLUMNNAME()) {
                case Config.LIVERECORD:
                    rbNo1.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    break;
                case Config.FRISTREGISTER:
                    rbNo2.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    break;
                case Config.CARFORM:
                    rbNo3.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.VISIBLE);
                    break;
                case Config.LIVETRANSCRIPT:
                    rbNo4.setVisibility(View.VISIBLE);
                    view4.setVisibility(View.VISIBLE);
                    break;
                case Config.TALKNOTICE:
                    rbNo5.setVisibility(View.VISIBLE);
                    view5.setVisibility(View.VISIBLE);
                    break;
                case Config.CARDECIDE:
                    rbNo6.setVisibility(View.VISIBLE);
                    view6.setVisibility(View.VISIBLE);
                    break;
                case Config.ADMINISTRATIVE:
                    rbNo7.setVisibility(View.VISIBLE);
                    view7.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onGetTimes(ArrayList<String> data) {
        if (data.size() != 7) return;
        rbNo1.setText(Html.fromHtml(getString(R.string.live_check_record2) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, data.get(0))));
        rbNo2.setText(Html.fromHtml(getString(R.string.register_notice) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, data.get(1))));
        rbNo3.setText(Html.fromHtml(getString(R.string.detain_car_form) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, data.get(2))));
        rbNo4.setText(Html.fromHtml(getString(R.string.live_transcript) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, data.get(3))));
        rbNo5.setText(Html.fromHtml(getString(R.string.talk_notice) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, data.get(4))));
        rbNo6.setText(Html.fromHtml(getString(R.string.detain_car_decide) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, data.get(5))));
        rbNo7.setText(Html.fromHtml(getString(R.string.administrative_decide) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, data.get(6))));
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                final int pages = data.getIntExtra("pages", 0);
                if (pages > 0) {
                    handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            assert mPresenter != null;
                            switch (requestCode) {
                                case 0x0001:
                                    mPresenter.prepareModifyPrintTimes(Config.T_LIVERECORD, String.valueOf(pages), instrumentID);
                                    break;
                                case 0x0010:
                                    mPresenter.prepareModifyPrintTimes(Config.T_ADMINISTRATIVE, String.valueOf(pages), instrumentID);
                                    break;
                                case 0x0011:
                                    mPresenter.prepareModifyPrintTimes(Config.T_FRISTREGISTER, String.valueOf(pages), instrumentID);
                                    break;
                                case 0x0100:
                                    mPresenter.prepareModifyPrintTimes(Config.T_CARFORM, String.valueOf(pages), instrumentID);
                                    break;
                                case 0x0101:
                                    mPresenter.prepareModifyPrintTimes(Config.T_CARDECIDE, String.valueOf(pages), instrumentID);
                                    break;
                                case 0x0110:
                                    mPresenter.prepareModifyPrintTimes(Config.T_LIVETRANSCRIPT, String.valueOf(pages), instrumentID);
                                    break;
                                case 0x0111:
                                    mPresenter.prepareModifyPrintTimes(Config.T_TALKNOTICE, String.valueOf(pages), instrumentID);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }, 500);
                }
            }
        }
    }

    @Override
    public void getLiveCheckRecordSuccess(LiveCheckRecordQ liveCheckRecordQ) {
        this.instrumentID = liveCheckRecordQ.getID();
        startActivityForResult(WebViewActivity.getIntent(self, liveCheckRecordQ, false), 0x0001);
    }

    @Override
    public void getAdministrativePenaltySuccess(AdministrativePenalty administrativePenalty) {
        this.instrumentID = administrativePenalty.getID();
        startActivityForResult(WebViewActivity.getIntent(self, administrativePenalty, false), 0x0010);
    }

    @Override
    public void getFristRegisterSuccess(FristRegisterQ fristRegisterQ) {
        this.instrumentID = fristRegisterQ.getID();
        startActivityForResult(WebViewActivity.getIntent(self, fristRegisterQ, false), 0x0011);
    }

    @Override
    public void getDetainCarFormSuccess(DetainCarFormQ detainCarFormQ) {
        this.instrumentID = detainCarFormQ.getID();
        startActivityForResult(WebViewActivity.getIntent(self, detainCarFormQ, false), 0x0100);
    }

    @Override
    public void getDetainCarDecideSuccess(DetainCarDecideQ detainCarDecideQ) {
        this.instrumentID = detainCarDecideQ.getID();
        startActivityForResult(WebViewActivity.getIntent(self, detainCarDecideQ, false), 0x0101);
    }

    @Override
    public void getLiveTranscriptSuccess(LiveTranscript liveTranscript) {
        this.instrumentID = liveTranscript.getID();
        startActivityForResult(WebViewActivity.getIntent(self, liveTranscript, false), 0x0110);
    }

    @Override
    public void getTalkNoticeSuccess(TalkNoticeQ talkNoticeQ) {
        this.instrumentID = talkNoticeQ.getID();
        startActivityForResult(WebViewActivity.getIntent(self, talkNoticeQ, false), 0x0111);
    }

    @Override
    public void modifyPrintTimesSuccess(String instrumentType, String times) {
        if (!TextUtils.isEmpty(instrumentType)) {

            switch (instrumentType) {
                case Config.T_ADMINISTRATIVE:
                    rbNo7.setText(Html.fromHtml(getString(R.string.administrative_decide) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, times)));
                    break;
                case Config.T_CARDECIDE:
                    rbNo6.setText(Html.fromHtml(getString(R.string.detain_car_decide) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, times)));
                    break;
                case Config.T_CARFORM:
                    rbNo3.setText(Html.fromHtml(getString(R.string.detain_car_form) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, times)));
                    break;
                case Config.T_FRISTREGISTER:
                    rbNo2.setText(Html.fromHtml(getString(R.string.register_notice) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, times)));
                    break;
                case Config.T_LIVERECORD:
                    rbNo1.setText(Html.fromHtml(getString(R.string.live_check_record2) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, times)));
                    break;
                case Config.T_LIVETRANSCRIPT:
                    rbNo4.setText(Html.fromHtml(getString(R.string.live_transcript) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, times)));
                    break;
                case Config.T_TALKNOTICE:
                    rbNo5.setText(Html.fromHtml(getString(R.string.talk_notice) + "<font color=#41C3D6>" + getString(R.string.writ_print_times, times)));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        assert mPresenter != null;
        int i = rgWrit.getCheckedRadioButtonId();
        if (i == R.id.rb_no1) {
            mPresenter.getLiveCheckRecord();
        } else if (i == R.id.rb_no2) {
            mPresenter.getFristRegister();
        } else if (i == R.id.rb_no3) {
            mPresenter.getDetainCarForm();
        } else if (i == R.id.rb_no4) {
            mPresenter.getLiveTranscript();
        } else if (i == R.id.rb_no5) {
            mPresenter.getTalkNotice();
        } else if (i == R.id.rb_no6) {
            mPresenter.getDetainCarDecide();
        } else if (i == R.id.rb_no7) {
            mPresenter.getAdministrativePenalty();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
