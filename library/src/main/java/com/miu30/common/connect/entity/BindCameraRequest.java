package com.miu30.common.connect.entity;

/**
 * 作者：wanglei on 2019/5/23.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 绑定摄像头请求
 */
public class BindCameraRequest implements IMesage {
    /** 消息类型 */
    private int msgType = BIND_CAMERA;
    /** 执法账号 */
    private String zfzh;
    /**
     * 绑定摄像头ID列表，多个以逗号隔开
     * <p>
     * 例如：
     * 111111111111,22222222222222222,333333333
     */
    private String cameraIDList;

    public BindCameraRequest(String zfzh, String cameraIDList) {
        this.zfzh = zfzh;
        this.cameraIDList = cameraIDList;
    }
}
