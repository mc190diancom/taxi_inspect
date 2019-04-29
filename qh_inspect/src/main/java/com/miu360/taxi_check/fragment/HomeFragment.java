package com.miu360.taxi_check.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.ExceptionHandler;
import com.lubao.lubao.async.Result;
import com.miu30.common.data.UserPreference;
import com.miu360.inspect.R;
import com.miu360.legworkwrit.mvp.data.PhotoPreference;
import com.miu360.legworkwrit.mvp.ui.activity.CaseListActivity;
import com.miu360.legworkwrit.mvp.ui.activity.CaseSearchActivity;
import com.miu360.legworkwrit.mvp.ui.activity.CreateCaseActivity;
import com.miu360.legworkwrit.mvp.ui.activity.InquiryRecordActivity;
import com.miu360.legworkwrit.mvp.ui.activity.UploadListActivity;
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.YuJingPreference;
import com.miu360.taxi_check.data.HistoryData;
import com.miu360.taxi_check.data.UserData;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.NoticeImage;
import com.miu360.taxi_check.model.NoticeModel;
import com.miu360.taxi_check.model.RoateInfo;
import com.miu360.taxi_check.model.VehiclePositionModex;
import com.miu360.taxi_check.ui.BasicInfoActivity;
import com.miu360.taxi_check.ui.FindRecordActivity;
import com.miu360.taxi_check.ui.LawInpsectActivity;
import com.miu360.taxi_check.ui.OtherActivity;
import com.miu360.taxi_check.ui.RenYuanFenBuActivity;
import com.miu360.taxi_check.ui.VehiclePositionActivity;
import com.miu360.taxi_check.ui.WarningInspectActivity;
import com.miu360.taxi_check.ui.WeiZhangQueryActivity;
import com.miu360.taxi_check.ui.YuJinPushListActivity;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.ListViewHolder;
import com.miu360.taxi_check_viewPager.BannerViewPager;
import com.miu360.taxi_check_viewPager.PointView;
import com.miu360.taxi_check_viewPager.ViewPagerBannerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

//OnRefreshListener2<ListView>,
public class HomeFragment extends BaseFragment implements OnClickListener {

    private BannerViewPager mViewPager;
    private ArrayList<ImageView> mImageViews;
    private int[] imgIdArray;
    private ListViewHolder listHolder;
    private HomeAdapter listAdapter;
    private Handler handler;
    private PointView mIndicator;

    private List<VehiclePositionModex> mDataList;
    private List<VehiclePositionModex> DataList;

    private View mHeaderView;
    private Button weizhang_btn;
    private Button jichu_btn;
    private Button tongji_btn;
    private Button genzong_btn;
    private Button fenbu_btn;
    private Button inspect_btn;
    private Button btn_yujing;
    private Button btn_qita;

    private Button btn_case_create;
    private Button btn_record_question;
    private Button btn_case_list;
    private Button btn_writ_print;
    private TextView tv_notice_bt;
    private ImageView one_image;

    private DbUtils dbUtils;
    private Handler handlers = new Handler();
    public ArrayList<String> roateString;
    UserPreference pref;
    YuJingPreference yuJingPer;
    BitmapUtils bitmapUtils;
    ViewPagerBannerAdapter adapter;
    ArrayList<NoticeImage> noticeImageDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, null);
        dbUtils = DbUtils.create(VehiclePositionModex.getDaoConfig());
        dbUtils.configDebug(true);
        pref = new UserPreference(act);
        yuJingPer = new YuJingPreference(act);
        roateString = new ArrayList<>();
		/*final RoateInfo rInfo = new RoateInfo();
		rInfo.setZFZH(pref.getString("login_id", null));*/
        initPermission(pref.getString("login_id", null));
        registerMsgReceiver();
        bitmapUtils = new BitmapUtils(act);
        mImageViews = new ArrayList<>();
        noticeImageDate = new ArrayList<>();
        listHolder = ListViewHolder.initList(act, root);
        mDataList = new ArrayList<>();
        DataList = new ArrayList<>();
        listAdapter = new HomeAdapter();
        listHolder.list.setAdapter(listAdapter);
        listHolder.list.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
        // listHolder.list.setOnRefreshListener(this);

        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_home_header, null);
        mIndicator = (PointView) mHeaderView.findViewById(R.id.indicator);
        tv_notice_bt = (TextView) mHeaderView.findViewById(R.id.tv_notice_bt);
        one_image = (ImageView) mHeaderView.findViewById(R.id.one_image);
        // 点击跳转到更多的预警界面
        mHeaderView.findViewById(R.id.iv_more).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(act, YuJinPushListActivity.class));
            }
        });
        listHolder.list.getRefreshableView().addHeaderView(mHeaderView);
        listHolder.list.getRefreshableView().setDividerHeight(0);
        weizhang_btn = (Button) mHeaderView.findViewById(R.id.btn_weizhang);
        jichu_btn = (Button) mHeaderView.findViewById(R.id.btn_jichu);
        tongji_btn = (Button) mHeaderView.findViewById(R.id.btn_tongji);
        btn_yujing = (Button) mHeaderView.findViewById(R.id.btn_yujing);
        btn_yujing.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // UIUtils.toast(act, "暂无权限使用此功能", Toast.LENGTH_SHORT);
                Intent intent = new Intent(act, WarningInspectActivity.class);
                startActivity(intent);
            }
        });
        inspect_btn = (Button) mHeaderView.findViewById(R.id.btn_inspect);
        inspect_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!roateString.contains("APP-执法稽查")) {
                    UIUtils.toast(act, "暂无权限使用此功能", Toast.LENGTH_SHORT);
                    return;
                }
                Intent intent = new Intent(act, LawInpsectActivity.class);
                startActivity(intent);
            }
        });
        btn_qita = (Button) mHeaderView.findViewById(R.id.btn_qita);
        btn_qita.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // UIUtils.toast(act, "暂无权限使用此功能", Toast.LENGTH_SHORT);
                Intent intent = new Intent(getActivity(), OtherActivity.class);
                startActivity(intent);
            }
        });
        fenbu_btn = (Button) mHeaderView.findViewById(R.id.btn_fenbu);

        fenbu_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // UIUtils.toast(act, "暂无权限使用此功能", Toast.LENGTH_SHORT);
                if (!roateString.contains("APP-人员分布")) {
                    UIUtils.toast(act, "暂无权限使用此功能", Toast.LENGTH_SHORT);
                    return;
                }
                Intent intent = new Intent(act, RenYuanFenBuActivity.class);
                startActivity(intent);
            }
        });
        genzong_btn = mHeaderView.findViewById(R.id.btn_genzong);
        genzong_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(act, VehiclePositionActivity.class);
                startActivity(intent);
            }
        });
        tongji_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // UIUtils.toast(act, "暂无权限使用此功能", Toast.LENGTH_SHORT);
                Intent intent = new Intent(getActivity(), FindRecordActivity.class);
                startActivity(intent);
            }
        });
        jichu_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BasicInfoActivity.class);
                startActivity(intent);
            }
        });
        weizhang_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // UIUtils.toast(act, "暂无权限使用此功能", Toast.LENGTH_SHORT);
                Intent intent = new Intent(getActivity(), WeiZhangQueryActivity.class);
                startActivity(intent);
            }
        });
        initNewBtnItem();

        imgIdArray = new int[]{R.drawable.home_1, R.drawable.home_2, R.drawable.home_3, R.drawable.home_4,
                R.drawable.home_5};
        mViewPager = (BannerViewPager) mHeaderView.findViewById(R.id.viewpager);
        initNoticeImage();

        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                mIndicator.setIndex(arg0 % mImageViews.size());
                if (noticeImageDate.size() != 0) {
                    tv_notice_bt.setText(noticeImageDate.get(arg0 % mImageViews.size()).getBT());
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        return root;
    }

    private void initNewBtnItem() {
        btn_case_create = mHeaderView.findViewById(R.id.btn_case_create);
        btn_record_question = mHeaderView.findViewById(R.id.btn_record_question);
        btn_case_list = mHeaderView.findViewById(R.id.btn_case_list);
        btn_writ_print = mHeaderView.findViewById(R.id.btn_writ_print);
        btn_case_create.setOnClickListener(this);
        btn_record_question.setOnClickListener(this);
        btn_case_list.setOnClickListener(this);
        btn_writ_print.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_case_create:
                startActivity(new Intent(act, CreateCaseActivity.class));
                break;
            case R.id.btn_record_question:
                Set<String> localPhotos = new PhotoPreference().getPhotos();

                if (localPhotos == null || localPhotos.size() <= 0) {
                    startActivity(new Intent(act, InquiryRecordActivity.class));
                } else {
                    startActivity(new Intent(act, UploadListActivity.class));
                }
                break;
            case R.id.btn_case_list:
                Intent intent = new Intent(act, CaseListActivity.class);
                intent.putExtra("from", CaseListActivity.FROM_HOME);
                startActivity(intent);
                break;
            case R.id.btn_writ_print:
                startActivity(new Intent(act, CaseSearchActivity.class));
                break;
        }
    }

    private void initNoticeImage() {
        Calendar c = Calendar.getInstance();
        // 过去七天
        c.setTime(new Date());
        c.add(Calendar.DATE, -30);
        Date d = c.getTime();

        int starTime = (int) (d.getTime() / 1000);

        final NoticeModel noticeInfo = new NoticeModel();
        noticeInfo.setBT("");
        noticeInfo.setSTATUS("1");
        noticeInfo.setStartIndex(0);
        noticeInfo.setRELEASE_PERSON("");
        noticeInfo.setRELEASE_GROUP("");
        noticeInfo.setEndIndex(5);
        noticeInfo.setENDTIME((int) (System.currentTimeMillis() / 1000) + "");
        noticeInfo.setSTARTTIME(starTime + "");

        AsyncUtil.goAsync(new Callable<Result<List<NoticeImage>>>() {

            @Override
            public Result<List<NoticeImage>> call() throws Exception {
                // TODO Auto-generated method stub
                return UserData.queryNotice(noticeInfo);
            }
        }, new Callback<Result<List<NoticeImage>>>() {

            @Override
            public void onHandle(Result<List<NoticeImage>> result) {
                // TODO Auto-generated method stub

                if (result.ok() && result.getData().size() > 1) {
                    if (result.getData().size() > 5) {
                        for (int i = 0; i < result.getData().size() - 5; i++) {
                            result.getData().remove(i);
                        }
                    }
                    if (result.getData().size() == 1) {
                        one_image.setVisibility(View.VISIBLE);
                        String url = Config.SERVER_LOADNOTECE_IMAGE + result.getData().get(0).getID();
                        bitmapUtils.display(one_image, url);
                        mIndicator.setCount(1);
                        tv_notice_bt.setText(result.getData().get(0).getBT());
                        return;
                    } else {
                        one_image.setVisibility(View.GONE);
                    }
                    noticeImageDate.clear();
                    noticeImageDate.addAll(result.getData());
                    mImageViews.clear();
                    for (int i = 0; i < result.getData().size(); i++) {
                        ImageView imageView = new ImageView(getActivity());

                        // imageView.setBackgroundColor(Color.parseColor("#00acef"));
                        imageView.setScaleType(ScaleType.CENTER);
                        imageView.setScaleType(ScaleType.FIT_XY);
                        // imageView.setImageResource(imgIdArray[i]);
                        // 使用bitmapUtils展示图片
                        String url = Config.SERVER_LOADNOTECE_IMAGE + result.getData().get(i).getID();
                        bitmapUtils.display(imageView, url);
                        mImageViews.add(imageView);
                    }
                } else {
                    mImageViews.clear();
                    for (int i = 0; i < imgIdArray.length; i++) {
                        ImageView imageView = new ImageView(getActivity());
                        mImageViews.add(imageView);
                        // imageView.setBackgroundColor(Color.parseColor("#00acef"));
                        imageView.setScaleType(ScaleType.CENTER);
                        imageView.setScaleType(ScaleType.FIT_XY);
                        imageView.setImageResource(imgIdArray[i]);
                        // 使用bitmapUtils展示图片
                        // String
                        // url=Config.SERVER_LOADNOTECE_IMAGE+result.getData().get(i).getID();
                        // bitmapUtils.display(imageView, url);
                    }

                }
                mIndicator.setCount(mImageViews.size());
                adapter = new ViewPagerBannerAdapter(act) {

                    @Override
                    public void onItemClick(int position) {

                    }

                    @Override
                    public View getView(LayoutInflater inflater, int position) {
                        // return mImageViews[position % mImageViews.length];
                        ImageView imageView = new ImageView(getActivity());
                        return mImageViews.size() == 0 ? imageView : mImageViews.get(position % mImageViews.size());
                    }

                    @Override
                    public int getItemCount() {
                        return mImageViews.size();
                    }
                };
                mViewPager.setAdapter(adapter);
                mViewPager.startScrol();

            }
        });

    }

    private void initPermission(final String rInfo) {
        if(TextUtils.isEmpty(rInfo)){
            return;
        }
        AsyncUtil.goAsync(new Callable<Result<List<RoateInfo>>>() {

            @Override
            public Result<List<RoateInfo>> call() throws Exception {
                return WeiZhanData.queryZFRYRoteinfo_new(rInfo);
            }
        }, new Callback<Result<List<RoateInfo>>>() {

            @Override
            public void onHandle(Result<List<RoateInfo>> result) {
                if (result.ok()) {
                    if (result.getData() == null) {
                        return;
                    }

                    for (int i = 0; i < result.getData().size(); i++) {
                        roateString.add(result.getData().get(i).getNAME());
                    }
                    if (roateString.contains("APP-执法稽查")) {
                        pref.setBoolean("isLaw", true);
                    } else {
                        pref.setBoolean("isLaw", false);
                    }
                } else {
                    initPermission(rInfo);
                }
            }
        });
    }

    class HomeViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgIdArray.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(mImageViews.get(position % mImageViews.size()));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mImageViews.get(position % mImageViews.size()), 0);
            return mImageViews.get(position % mImageViews.size());
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handlers.post(new Runnable() {

            @Override
            public void run() {
                initData();
                handlers.postDelayed(this, 5 * 1000);
            }
        });
    }

    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return DataList.size();
        }

        @Override
        public Object getItem(int position) {
            return DataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VehiclePositionModex item = (VehiclePositionModex) getItem(position);
            viewHolder holder = null;
            if (convertView == null) {
                holder = new viewHolder();
                convertView = LayoutInflater.from(act).inflate(R.layout.yujinpushlistadapter2, null);
                convertView.setTag(holder);
                holder.car_address = (TextView) convertView.findViewById(R.id.car_address);
                holder.car_name = (TextView) convertView.findViewById(R.id.car_name);
                holder.car_reason = (TextView) convertView.findViewById(R.id.car_reason);
            } else {
                holder = (viewHolder) convertView.getTag();
            }
            LatLng ll = new LatLng(item.getLat(), item.getLon());
            holder.car_name.setText(item.getVname());
            holder.car_reason.setText(item.getAlarmReason());
            reverseGeoCode(holder.car_address, ll);

            return convertView;
        }

        /**
         * 反Geo搜索
         */
        public void reverseGeoCode(final TextView tv, final LatLng ptCenter) {
            AsyncUtil.goAsync(new Callable<Result<String>>() {

                @Override
                public Result<String> call() throws Exception {
                    return HistoryData.queryHistoryTrack(ptCenter);
                }
            }, new Callback<Result<String>>() {

                @Override
                public void onHandle(Result<String> result) {
                    if (result.ok()) {
                        tv.setText(result.getData());
                    }
                }
            });
        }

        private class viewHolder {
            TextView car_name;
            TextView car_address;
            TextView car_reason;
        }

    }

    /*
     * @Override public void onPullDownToRefresh(PullToRefreshBase<ListView>
     * refreshView) { initData(); }
     *
     * @Override public void onPullUpToRefresh(PullToRefreshBase<ListView>
     * refreshView) { initData(); }
     */

    private void initData() {
        if (yuJingPer.getBoolean("isChecked", false)) {
            listHolder.list.setMode(Mode.PULL_FROM_START);
            AsyncUtil.goAsync(new Callable<Result<List<VehiclePositionModex>>>() {

                @Override
                public Result<List<VehiclePositionModex>> call() throws Exception {
                    Result<List<VehiclePositionModex>> ret = new Result<List<VehiclePositionModex>>();
                    try {
                        List<VehiclePositionModex> cache = dbUtils
                                .findAll(Selector.from(VehiclePositionModex.class).orderBy("id", false));
                        if (cache == null) {
                            cache = new ArrayList<VehiclePositionModex>();
                        }
                        ret.setData(cache);
                    } catch (Exception e) {
                        ret.setThrowable(e);
                        ExceptionHandler.handleException(act, ret);
                    }
                    return ret;
                }
            }, new Callback<Result<List<VehiclePositionModex>>>() {

                @Override
                public void onHandle(Result<List<VehiclePositionModex>> result) {
                    if (result.ok()) {
                        mDataList.clear();
                        DataList.clear();
                        mDataList.addAll(result.getData());
                        if (mDataList.size() > 3) {
                            DataList.add(mDataList.get(mDataList.size() - 1));
                            DataList.add(mDataList.get(mDataList.size() - 2));
                            DataList.add(mDataList.get(mDataList.size() - 3));
                        } else {
                            DataList.addAll(mDataList);
                        }
                        listAdapter.notifyDataSetChanged();
                        listHolder.list.setMode(Mode.DISABLED);
                    } else {
                        new AlertDialog.Builder(act).setMessage(result.getErrorMsg()).show();
                    }
                    // listHolder.mayShowEmpty(mDataList.size());
                    listHolder.list.onRefreshComplete();
                }
            });

        }

    }

    public void registerMsgReceiver() {
        IntentFilter filter = new IntentFilter("com.miu360.push");
        act.registerReceiver(msgReceiver, filter);

    }

    public void unregisterMsgReceiver() {
        try {
            act.unregisterReceiver(msgReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BroadcastReceiver msgReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isOpen = intent.getBooleanExtra("Push", false);
            if (isOpen) {
                initData();
            } else {
                mDataList.clear();
                DataList.clear();
                listAdapter.notifyDataSetChanged();
            }
        }
    };

    public void onDestroy() {
        super.onDestroy();
        unregisterMsgReceiver();
    }

    ;

    public void initView() {
        // Button weizhang_btn=
    }

}
