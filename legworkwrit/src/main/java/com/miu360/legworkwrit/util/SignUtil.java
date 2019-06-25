package com.miu360.legworkwrit.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cn.org.bjca.anysign.android.api.Interface.OnSignatureResultListener;
import cn.org.bjca.anysign.android.api.core.OriginalContent;
import cn.org.bjca.anysign.android.api.core.SignRule;
import cn.org.bjca.anysign.android.api.core.SignatureAPI;
import cn.org.bjca.anysign.android.api.core.SignatureObj;
import cn.org.bjca.anysign.android.api.core.Signer;
import cn.org.bjca.anysign.android.api.core.domain.AnySignBuild;
import cn.org.bjca.anysign.android.api.core.domain.SignResult;
import cn.org.bjca.anysign.android.api.core.domain.SignatureType;

public class SignUtil {
    private SignatureAPI api;
    private String path_root = Environment.getExternalStorageDirectory()+"/anysign_2.4.4.txt";
    private static SignUtil signUtil;
    private static Activity context;

    public static SignUtil getInstance(Activity context){
        SignUtil.context = context;
        if(signUtil == null){
            return signUtil = new SignUtil();
        }else {
            return signUtil;
        }
    }

    public void initApi(String TemplateDataPath){
        // 设置签名算法，默认为RSA，可以设置成SM2
        AnySignBuild.Default_Cert_EncAlg = "SM2";
        /*
         *  初始化API
         */
        api = new SignatureAPI(context);

        //设置渠道号
        if(!initChannel()) return;

        //设置模板数据
        setTemplateData(TemplateDataPath);

        setApiListner();
    }

    /**
     * 初始化渠道号
     */
    private boolean initChannel() {
        //所有api接口中设置成功返回 SignatureAPI.SUCCESS（0），其他错误
        int apiResult = api.setChannel("999999");
        if(apiResult == SignatureAPI.SUCCESS){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 设置模板数据
     */
    private void setTemplateData(String TemplateDataPath) {
        //InputStream is = context.getResources().openRawResource(R.raw.test);
        byte[] bTemplate  = new byte[0];
        File file = new File(TemplateDataPath);
        try {
            FileInputStream is = new FileInputStream(file);
            bTemplate = new byte[is.available()];
            is.read(bTemplate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
         * 配置此次签名对应的模板数据
         * 参数1：表示模板类型，不可为空：如果为PDF和HTML格式，调用下面构造函数
         *        ContextID.FORMDATA_PDF：PDF格式，ContextID.FORMDATA_HTML：HTML格式
         * 参数2：表示模板数据byte数组类型，不可为空
         * 参数3：业务流水号/工单号，不可为空
         */
        int apiResult =  api.setOrigialContent(new OriginalContent(OriginalContent.CONTENT_TYPE_HTML, bTemplate, "111"));
    }

    //使用关键字方式定位签名图片位置
    public void setSignObject(String name, String card) {
        SignRule signRule = SignRule.getInstance(SignRule.SignRuleType.TYPE_XYZ);
        signRule.setXYZRule(new SignRule.XYZRule(84, 523, 200, 411, 0,"dp"));
        Signer signer = new Signer(name, card, Signer.SignerCardType.TYPE_IDENTITY_CARD);
//			实例化手写签名对象
//			参数1：手写签名对象索引值
        SignatureObj obj = new SignatureObj(0,signRule,signer);
//			设置签名图片高度，单位dip
        obj.single_height = 100;
//			设置签名图片宽度，单位dip
        obj.single_width = 100;
//          是否开启字迹轨迹记录，默认不开启
        obj.enableSignatureRecording = true;
//           手写识别开关，true为开启手写识别，false为关闭手写识别
        obj.isdistinguish = false;
//			 签名是否必须,设置为true时必须进行签名，默认true
        obj.nessesary = false;
//			 设置签名笔迹颜色，默认为黑色
        obj.penColor = Color.RED;
//			 需要显示在签名框顶栏的标题
        obj.title = "请"+name+"签字";
//			单字签名框中需要突出显示部分的起始位置和结束位置
        obj.titleSpanFromOffset = 1;
//			单字签名框中需要突出显示部分的起始位置和结束位置
        obj.titleSpanToOffset = name.length();
//			是否开启边拍边签功能
        obj.openCamera = false;
//			是否在边拍边签中打开人脸识别
        obj.openFaceDetection = false;
//			是否把拍照证据添加到证据列表中
        obj.isAddEvidence = false;
//			识别错误提示语
        obj.distinguishErrorText = "识别错误";

//			 注册单签签名对象
        int apiResult = api.addSignatureObj(obj);
    }

    private void setApiListner() {
        /*
         * 注册签名结果回调函数
         */
        api.setOnSignatureResultListener(new OnSignatureResultListener() {

            @Override
            public void onSignResult(final SignResult signResult) {
//				在这里取签名图片
//				参数1：签名索引值
//				参数2：签名图片
                showImgPreviewDlg(signResult.signature);

                if(signResult.eviPic != null && signResult.eviPic.length != 0){
                    context.runOnUiThread(new Runnable() {
                        public void run() {
                            Bitmap camera_pic = BitmapFactory.decodeByteArray(signResult.eviPic, 0, signResult.eviPic.length);
                            showImgPreviewDlg(camera_pic);
                        }
                    });
                }

                Log.d("XSS", "onSignResult signIndex : " + signResult.signIndex + "  resultCode : " + signResult.resultCode+ "  signType : " + signResult.signType+ "  pointStack : " + signResult.pointStack
                        + "  eviPic : " + signResult.eviPic);

            }

            @Override
            public void onCancel(int index, SignatureType signType) {
                Log.d("XSS", "onCancel index : " + index + "  signType : " + signType);

            }

            @Override
            public void onDismiss(int index, SignatureType signType) {
                Log.d("XSS", "onDismiss index : " + index + "  signType : " + signType);
            }

        });
    }

    private void showImgPreviewDlg(Bitmap img) {
        ImageView iv = new ImageView(context);
        iv.setBackgroundColor(Color.WHITE);
        iv.setImageBitmap(img);
        new AlertDialog.Builder(context).setView(iv).show();
    }

    public void startSignActivity(){
        if (api !=null) {
            int apiResult = api.showSignatureDialog(0);// 弹出单签签名框签名
            if(apiResult == SignatureAPI.SUCCESS){
            }else{
                Toast.makeText(context, "错误码："+apiResult , Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "请先初始化API", Toast.LENGTH_SHORT).show();
        }
    }
}
