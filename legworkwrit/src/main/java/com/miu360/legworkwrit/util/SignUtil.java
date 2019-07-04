package com.miu360.legworkwrit.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileIOUtils;
import com.miu30.common.config.Config;
import com.miu30.common.util.UIUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cn.org.bjca.anysign.android.api.Interface.OnSignatureResultListener;
import cn.org.bjca.anysign.android.api.core.CommentObj;
import cn.org.bjca.anysign.android.api.core.OriginalContent;
import cn.org.bjca.anysign.android.api.core.SignRule;
import cn.org.bjca.anysign.android.api.core.SignatureAPI;
import cn.org.bjca.anysign.android.api.core.SignatureObj;
import cn.org.bjca.anysign.android.api.core.Signer;
import cn.org.bjca.anysign.android.api.core.domain.AnySignBuild;
import cn.org.bjca.anysign.android.api.core.domain.CommentInputType;
import cn.org.bjca.anysign.android.api.core.domain.SignResult;
import cn.org.bjca.anysign.android.api.core.domain.SignatureType;

public class SignUtil {
    private SignatureAPI api;

    private static SignUtil signUtil;
    private static Activity context;
    private  String id;
    private SignCallBack signCallBack;
    private boolean isMoreSign;//判断是否是多字签名
    private boolean Signed;//已签名
    private boolean Commented;//已批注

    public static SignUtil getInstance(Activity context){
        SignUtil.context = context;
        if(signUtil == null){
            return signUtil = new SignUtil();
        }else {
            return signUtil;
        }
    }


    public interface SignCallBack{
        void signSuccess(SignatureType signatureType);
        void signFailure();
    }

    public void initApi(String id,String TemplateDataPath,SignCallBack signCallBack,String type){
        this.signCallBack = signCallBack;
        this.id = id;
        isMoreSign = Config.LIVERECORD.equals(type) || Config.LIVETRANSCRIPT.equals(type);
        // 设置签名算法，默认为RSA，可以设置成SM2
        AnySignBuild.Default_Cert_EncAlg = "SM2";
        /*
         *  初始化API
         */
        api = new SignatureAPI(context);

        //设置渠道号
        if(!initChannel()) return;

        //设置模板数据
        setTemplateData();

        setApiListner();
    }

    /**
     * 初始化渠道号
     */
    private boolean initChannel() {
        //所有api接口中设置成功返回 SignatureAPI.SUCCESS（0），其他错误
        int apiResult = api.setChannel("999999");
        return apiResult == SignatureAPI.SUCCESS;
    }

    /**
     * 设置模板数据
     */
    private void setTemplateData() {
        /*
         * 配置此次签名对应的模板数据
         * 参数1：表示模板类型，不可为空：如果为PDF和HTML格式，调用下面构造函数
         *        ContextID.FORMDATA_PDF：PDF格式，ContextID.FORMDATA_HTML：HTML格式
         * 参数2：表示模板数据byte数组类型，不可为空
         * 参数3：业务流水号/工单号，不可为空
         */
        api.setOrigialContent(new OriginalContent(OriginalContent.CONTENT_TYPE_PDF, "123".getBytes(), "111"));
        /*byte[] bTemplate  = new byte[0];
        File file = new File(TemplateDataPath);
        if(!file.exists()){
            UIUtils.toast(context,"文件生成失败，请重试！",Toast.LENGTH_SHORT);
            return;
        }
        try {
            FileInputStream is = new FileInputStream(file);
            //InputStream is = context.getResources().getAssets().open("test.pdf");
            bTemplate = new byte[is.available()];
            is.read(bTemplate);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    //使用关键字方式定位签名图片位置
    public void setSignObject(String name, String card) {
        SignRule signRule = SignRule.getInstance(SignRule.SignRuleType.TYPE_KEY_WORD);
        signRule.setKWRule(new SignRule.KWRule("签字：",SignRule.KWRule.SigAlignMethod.to_right_of_keyword, 3, 0, 1));
        Signer signer = new Signer(name, card, Signer.SignerCardType.TYPE_IDENTITY_CARD);
//			实例化手写签名对象
//			参数1：手写签名对象索引值
        SignatureObj obj = new SignatureObj(0,signRule,signer);
//			设置签名图片高度，单位dip
        obj.single_height = 25;
//			设置签名图片宽度，单位dip
        obj.single_width = name.length() * 15;
//          是否开启字迹轨迹记录，默认不开启
        obj.enableSignatureRecording = true;
//           手写识别开关，true为开启手写识别，false为关闭手写识别
        obj.isdistinguish = false;
//			 签名是否必须,设置为true时必须进行签名，默认true
        obj.nessesary = false;
//			 设置签名笔迹颜色，默认为黑色
        obj.penColor = Color.BLACK;
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
        api.addSignatureObj(obj);
    }

    public void setNotation2(String name, String card) {
        SignRule signRule_1 = SignRule.getInstance(SignRule.SignRuleType.TYPE_KEY_WORD);
        signRule_1.setKWRule(new SignRule.KWRule("上述内容请过", SignRule.KWRule.SigAlignMethod.below_keyword, 10, 1, 1));
//			签名人信息（姓名/身份证号/类型）
        /*Signer signer1 = new Signer("情况属实", "222", Signer.SignerCardType.TYPE_IDENTITY_CARD);*/
//			签名人信息（姓名/身份证号/类型）
        Signer signer = new Signer(name, card, Signer.SignerCardType.TYPE_IDENTITY_CARD);
//			实例化批注对象
//			参数1：批注对象索引值
        CommentObj massobj_1 = new CommentObj(1,signRule_1,signer);
//			多字输入框类型，默认为CommentInputType.Scrollable
        /*
         * CommentInputType 批注的类型
         * 			Scrollable  网格批注（适配在pad上）
         *  		Normal   动画批注（适配在手机上）
         *  		WhiteBoard 白板批注
         */
        massobj_1.mass_dlg_type = CommentInputType.Normal;
//			批注内容
//			massobj.commitment = "本人已阅读保险条款、产品说明书和投保提示书，了解本产品的特点和保单利益的不确定";
        massobj_1.commitment = "情况属实";
//			生成的签名图片中单行显示的字数
        massobj_1.mass_words_in_single_line = 4;
//			生成的签名图片中单个字的高
        massobj_1.mass_word_height = 40;
//			生成的签名图片中单个字的宽
        massobj_1.mass_word_width = 40;
//			 签名是否必须,设置为true时必须进行签名，默认true
        massobj_1.nessesary = false;
//			设置提示字的大小
        massobj_1.editBarTextSize = 18;
//			设置提示字的颜色
        massobj_1.editBarTextColor = Color.RED;
//			设置当前正在签署提示字的倍数
        massobj_1.currentEditBarTextSize = -2f;
//			设置当前正在签署提示字的颜色
        massobj_1.currentEditBarTextColor = Color.BLACK;
//			识别错误提示语
        massobj_1.distinguishErrorText = "错误";
//			背景字是否显示
        massobj_1.isShowBgText = true;
//			是否开启手写识别开关
        massobj_1.isdistinguish = false;
//			设置笔迹颜色，默认为黑色
        massobj_1.penColor = Color.BLACK;
//			 注册批注对象
        api.addCommentObj(massobj_1);
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
                if(signResult.resultCode == 0){
                    if(SignatureType.SIGN_TYPE_SIGN == signResult.signType){
                        UIUtils.toast(context,"签名成功",Toast.LENGTH_SHORT);
                        Signed = true;
                    }
                    if(SignatureType.SIGN_TYPE_COMMENT == signResult.signType){
                        UIUtils.toast(context,"批注添加成功",Toast.LENGTH_SHORT);
                        Commented = true;
                    }
                    signCallBack.signSuccess(signResult.signType);
                    if(!isMoreSign || Signed && Commented){//如果只有单个签名直接生成加密包；如果带批注的需要批注和签名都完成后再生成加密包
                        getGenData();
                    }
                }else{
                    signCallBack.signFailure();
                    UIUtils.toast(context,"签名失败",Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onCancel(int index, SignatureType signType) {

            }

            @Override
            public void onDismiss(int index, SignatureType signType) {
            }

        });
    }

    private void showImgPreviewDlg(Bitmap img) {
        ImageView iv = new ImageView(context);
        iv.setBackgroundColor(Color.TRANSPARENT);
        iv.setImageBitmap(img);
        new AlertDialog.Builder(context).setView(iv).show();
    }

    public void startSign(){
        if (api !=null) {
            int apiResult = api.showSignatureDialog(0);// 弹出单签签名框签名
            if(apiResult != SignatureAPI.SUCCESS){
                Toast.makeText(context, "错误码："+apiResult , Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "请先初始化API", Toast.LENGTH_SHORT).show();
        }
    }

    public void setNotation2(){
        if (api !=null) {
            int apiResult = api.showCommentDialog(1);// 弹出多字签名框
            if(apiResult != SignatureAPI.SUCCESS){
                Toast.makeText(context, "错误码："+apiResult , Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "请先初始化API", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 生成加密包
     */
    private void getGenData(){
        if (api != null) {
            if (api.isReadyToGen() == 0) {
                /*
                 * genSignRequest() 获取加密数据包
                 */
                String str = (String) api.genSignRequest();// 生成上传信手书服务端加密报文
                if (str != null && !"".equals(str)) {
                    //将加密报文写到本地
                    FileIOUtils.writeFileFromBytesByStream(Config.path_root +id+".txt", str.getBytes());//writeByteArrayToPath(path_root, str.getBytes());
                } else {
                    //Toast.makeText(context, "打包失败" , Toast.LENGTH_SHORT).show();
                    System.out.println("SignNote:code:"+api.isReadyToGen());
                }
            }else {
                //Toast.makeText(context, "错误码：" + api.isReadyToGen()+"", Toast.LENGTH_SHORT).show();
                signCallBack.signFailure();
                System.out.println("SignNote:code:"+api.isReadyToGen());
            }
        }else {
            //Toast.makeText(context, "请先初始化API", Toast.LENGTH_SHORT).show();
            signCallBack.signFailure();
            System.out.println("SignNote:code:"+api.isReadyToGen());
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (null != api)
            api.finalizeAPI();
        signUtil = null;
        context = null;
    }

}
