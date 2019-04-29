package com.miu360.taxi_check.ui;

import java.util.List;
import java.util.concurrent.Callable;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu30.common.ui.entity.FaGuiDetail;
import com.miu30.common.ui.entity.FaGuiDetailQ;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

public class CheckDetailWeiFaActivity extends BaseActivity {

	@ViewInject(R.id.bianma)
	private TextView bianma;
	@ViewInject(R.id.jclb)
	private TextView jclb;
	@ViewInject(R.id.wfxw)
	private TextView wfxw;
	@ViewInject(R.id.wfqx)
	private TextView wfqx;
	@ViewInject(R.id.syfg)
	private TextView syfg;
	@ViewInject(R.id.wftk)
	private TextView wftk;
	@ViewInject(R.id.cftk)
	private TextView cftk;
	@ViewInject(R.id.fggd)
	private TextView fggd;
	String lbms = "无",lbmc="无",bm="无";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_detail_wei_fa);
		initView();
	}

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "检查项明细");
		queryWFQX();
		lbms = getIntent().getStringExtra("lbms");
		lbmc = getIntent().getStringExtra("lbmc");
		bm = getIntent().getStringExtra("bm");
	}

	private void queryWFQX() {
		final MyProgressDialog d = Windows.waiting(self);
		final FaGuiDetailQ fgQ = new FaGuiDetailQ();
		fgQ.setXqid(getIntent().getStringExtra("xqid"));
		AsyncUtil.goAsync(new Callable<Result<List<FaGuiDetail>>>() {

			@Override
			public Result<List<FaGuiDetail>> call() throws Exception {
				return WeiZhanData.getFaGuiDetails(fgQ);
			}
		}, new Callback<Result<List<FaGuiDetail>>>() {

			@Override
			public void onHandle(Result<List<FaGuiDetail>> result) {
				d.dismiss();
				if (result.ok()) {
					if(result.getData().toString().equals("[]") || result.getData() == null){
						UIUtils.toast(self, "该法规没有详情", Toast.LENGTH_SHORT);
						finish();
						return;
					}
					initData(result.getData());
				}else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					finish();
				}
			}
		});
	}

	String qx="";
	int count=1;
	private void initData(List<FaGuiDetail> info) {
		bianma.setText(isNull.isEmpty(bm));
		jclb.setText(isNull.isEmpty(lbms));
		wfxw.setText(isNull.isEmpty(lbmc));
		for(int i=0,len=info.size();i<len;i++){
			if(i==len-1){
				qx += count+"、"+info.get(i).getXMMC();
			}else{
				qx += count+"、"+info.get(i).getXMMC()+"\n";
			}
			count++;
		}
		wfqx.setText(isNull.isEmpty(qx));
		syfg.setText(isNull.isEmpty(info.get(0).getSYFLFG()));
		wftk.setText(isNull.isEmpty(info.get(0).getWFT()));
		cftk.setText(isNull.isEmpty(info.get(0).getCFT()));
		fggd.setText(isNull.isEmpty(info.get(0).getCFBZ()));
	}
}
