package com.feidi.video.mvp.model.entity;

/**
 * 作者：wanglei on 2019/6/3.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 需要实现单选功能的列表的实体类需实现此接口
 */
public interface ISelector {

    boolean isSelected();

    void setSelected(boolean selected);

}
