package com.miu360.legworkwrit.mvp.model.api.service;

import com.miu30.common.async.Result;
import com.miu30.common.config.Config;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.FaGuiDetail;
import com.miu30.common.ui.entity.HyRyFg;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.ui.entity.Template;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu30.common.ui.entity.queryZFRYByDWMC;
import com.miu360.legworkwrit.mvp.model.entity.AccordRule;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.District;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.IllegalDetail;
import com.miu360.legworkwrit.mvp.model.entity.IllegalDetailItem;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.Park;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.mvp.model.entity.WifiConfig;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * Created by Murphy on 2018/2/2.
 * 存放所有自己的接口
 */

public interface MyApis {

    @FormUrlEncoded
    @POST(Config.SERVER_OTHER)
    Observable<Result<List<queryZFRYByDWMC>>> getCheZuList(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<JSONObject>> addCase(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<JSONObject>> updateCase(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_OTHER)
    Observable<Result<List<JCItem>>> getJclb(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_WEBSERVICE)
    Observable<Result<List<District>>> getDistrict(@Field("type") String s);

    @FormUrlEncoded
    @POST(Config.SERVER)
    Observable<Result<List<VehicleInfo>>> getVehicleInfo(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<List<JCItem>>> getIllegalDetail(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<JSONObject>> addTalkNotice(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<List<Park>>> getParkList(@Field("type") String type);

    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<List<IllegalDetail>>> getIllegalDetailList(@Field("type") String type);

    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<JSONObject>> addFristRegister(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<List<AgencyInfo>>> getAgencyInfos(@Field("type") String type);

    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<JSONObject>> addXcjcblLjInfo(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<List<Case>>> selectCaseAll(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<List<UTC>>> selectCaseBlTime(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<Void>> sendBackOffice(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_OTHER)
    Observable<Result<List<FaGuiDetail>>> getWFQX(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<List<BlType>>> getBlType(@Field("type") String blType);

    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<Void>> getBindBl(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<JSONObject>> getAdministrativeData(@FieldMap Map<String, Object> map);

    //现场检查笔录查询
    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<List<LiveCheckRecordQ>>> getInitLiveCheckRecord(@FieldMap Map<String, Object> map);

    //行政处罚决定书查询
    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<List<AdministrativePenalty>>> getAdministrativePenalty(@FieldMap Map<String, Object> map);

    //谈话通知书
    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<List<TalkNoticeQ>>> getTalkNotice(@FieldMap Map<String, Object> map);

    //先行登记通知书
    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<List<FristRegisterQ>>> getFristRegister(@FieldMap Map<String, Object> map);

    //车辆交接单
    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<List<DetainCarFormQ>>> getDetainCarForm(@FieldMap Map<String, Object> map);

    //扣押车辆决定书
    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<List<DetainCarDecideQ>>> getDetainCarDecide(@FieldMap Map<String, Object> map);

    //现场笔录
    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<List<LiveTranscript>>> getLiveTranscript(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<JSONObject>> addDetainCarForm(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<JSONObject>> addDetainCarDecide(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<JSONObject>> getLiveData(@FieldMap Map<String, Object> map);

    /**
     * 询问笔录上传图片
     */
    @Multipart
    @POST(Config.SERVER_BLLIST)
    Observable<Result<Void>> inquiryRecordUploadPhotos(@QueryMap Map<String, Object> params, @Part MultipartBody.Part part);

    /**
     * 扣押车辆交接单上传图片
     */
    @Multipart
    @POST(Config.SERVER_BLLIST)
    Observable<Result<Void>> detainCarFormUploadPhotos(@QueryMap Map<String, Object> map, @Part() List<MultipartBody.Part> parts);

    /**
     * 获取案件绑定的笔录
     */
    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<ArrayList<BlType>>> getBlTypeByCase(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<List<InquiryRecordPhoto>>> getInquiryRecordPhotoList(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<List<AccordRule>>> getAccordRuleList(@Field("type") String type);

    /**
     * 修改询问笔录
     */
    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<Void>> updateInquiryRecord(@FieldMap Map<String, Object> map);

    /**
     * 删除一个询问笔录
     */
    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<Void>> deleteInquiryRecord(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<List<WifiConfig>>> getPrinterWifiConfigMsg(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<List<AgencyInfo>>> getPrinterAgencyInfoMsg(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Config.SERVER_BLLIST)
    Observable<Result<Void>> deleteAllInquiryRecord(@FieldMap Map<String, Object> map);

    /**
     * 获取最新的模板文件
     */
    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<List<Template>>> selectTemplateAll(@Field("type") String s);

    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<List<WifiConfig>>> getAllPrinterWifiConfig(@Field("type") String type);

    @FormUrlEncoded
    @POST(Config.SERVER)
    Observable<Result<List<DriverInfo>>> getDriverInfo(@FieldMap Map<String, Object> map);

    /**
     * 修改打印次数
     */
    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<String>> modifyPrintTimes(@FieldMap Map<String, Object> map);

    /**
     * 检查项数据获取
     */
    @FormUrlEncoded
    @POST(Config.SERVER_OTHER)
    Observable<Result<List<HyRyFg>>> getJCXData(@FieldMap Map<String, Object> map);

    /**
     * 获取违法内容
     */
    @FormUrlEncoded
    @POST(Config.SERVER_WAIQIN)
    Observable<Result<List<IllegalDetailItem>>> getIllegalContent(@FieldMap Map<String, Object> map);


    @Multipart
    @POST(Config.SERVER_SIGN)
    Observable<Result<Void>> uploadSignFile(@QueryMap Map<String, Object> map, @Part() MultipartBody.Part part);
}
