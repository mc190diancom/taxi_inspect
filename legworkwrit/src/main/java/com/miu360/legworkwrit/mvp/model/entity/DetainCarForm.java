package com.miu360.legworkwrit.mvp.model.entity;

/**
 * 作者：wanglei on 2018/10/17.
 * 邮箱：forwlwork@gmail.com
 */
public class DetainCarForm {
    private String id;
    private DetainCarFormQ detainCarFormQ;

    public DetainCarForm(String id, DetainCarFormQ detainCarFormQ) {
        this.id = id;
        this.detainCarFormQ = detainCarFormQ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DetainCarFormQ getDetainCarFormQ() {
        return detainCarFormQ;
    }

    public void setDetainCarFormQ(DetainCarFormQ detainCarFormQ) {
        this.detainCarFormQ = detainCarFormQ;
    }
}
