package com.miu30.common.connect.entity;

/**
 * 作者：wanglei on 2019/5/24.
 * 邮箱：forwlwork@gmail.com
 *
 *
 */
public class CancelBindCameraRequest implements IMesage {
    /**
     * msgType : 5
     * zfzh : 123456
     * cameraIDList : 111111111111,22222222222222222,333333333
     */
    private int msgType = CANCEL_BIND_CAMERA;
    private String zfzh;
    private String cameraIDList;

    public CancelBindCameraRequest(String zfzh, String cameraIDList) {
        this.zfzh = zfzh;
        this.cameraIDList = cameraIDList;
    }
}
