package com.miu360.legworkwrit.mvp.model.entity;

import com.miu30.common.ui.entity.JCItem;

import java.util.List;

/**
 * 作者：wanglei on 2019/3/8.
 * 邮箱：forwlwork@gmail.com
 */
public class JCItemWrapper {
    //违法行为
    private JCItem illegalBehavior;
    //违法情形
    private List<String> illegalSituation;

    public JCItemWrapper(JCItem illegalBehavior, List<String> illegalSituation) {
        this.illegalBehavior = illegalBehavior;
        this.illegalSituation = illegalSituation;
    }

    public JCItem getIllegalBehavior() {
        return illegalBehavior;
    }

    public void setIllegalBehavior(JCItem illegalBehavior) {
        this.illegalBehavior = illegalBehavior;
    }

    public List<String> getIllegalSituation() {
        return illegalSituation;
    }

    public void setIllegalSituation(List<String> illegalSituation) {
        this.illegalSituation = illegalSituation;
    }
}
