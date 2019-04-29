package com.miu360.legworkwrit.mvp.model.entity;

/**
 * 作者：wanglei on 2018/10/16.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 谈话通知书
 */
public class TalkNotice {
    private String id;
    private TalkNoticeQ talkNoticeQ;

    public TalkNotice(String id, TalkNoticeQ talkNoticeQ) {
        this.id = id;
        this.talkNoticeQ = talkNoticeQ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TalkNoticeQ getTalkNoticeQ() {
        return talkNoticeQ;
    }

    public void setTalkNoticeQ(TalkNoticeQ talkNoticeQ) {
        this.talkNoticeQ = talkNoticeQ;
    }
}
