package com.miu30.common.connect.entity;

/**
 * 作者：wanglei on 2019/5/22.
 * 邮箱：forwlwork@gmail.com
 */
public interface IMesage {
    /** 登录 */
    int LOGIN = 0;
    /** 注销 */
    int LOGOUT = 1;
    /** 心跳 */
    int HEARTBEAT = 2;
    /** 报警信息 */
    int ALARM = 3;
    /** 绑定摄像头 */
    int BIND_CAMERA = 4;
    /** 取消绑定摄像头 */
    int CANCEL_BIND_CAMERA = 5;
}
