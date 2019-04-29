package com.miu360.legworkwrit.mvp.contract;

import com.jess.arms.mvp.IView;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;

import java.util.Date;

public interface BaseInstrumentContract extends IView{
     void showResult(ParentQ info, int resultCode);
     void showTime(Date date, boolean isStart);
     void startWebActivity(ParentQ parentQ);
}
