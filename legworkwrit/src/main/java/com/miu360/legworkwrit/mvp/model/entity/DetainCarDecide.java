package com.miu360.legworkwrit.mvp.model.entity;

/**
 * 作者：wanglei on 2018/10/18.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 扣押车辆决定书
 */
public class DetainCarDecide {
    private String id;
    private DetainCarDecideQ detainCarDecideQ;

    public DetainCarDecide(String id, DetainCarDecideQ detainCarDecideQ) {
        this.id = id;
        this.detainCarDecideQ = detainCarDecideQ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DetainCarDecideQ getDetainCarDecideQ() {
        return detainCarDecideQ;
    }

    public void setDetainCarDecideQ(DetainCarDecideQ detainCarDecideQ) {
        this.detainCarDecideQ = detainCarDecideQ;
    }
}
