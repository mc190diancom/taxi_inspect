package com.miu360.legworkwrit.util;

import android.webkit.JavascriptInterface;

import com.miu360.legworkwrit.mvp.model.entity.ParentQ;


/**
 * Created by Murphy on 2018/10/11.
 */
public class HtmlHandle {
    /**
     * 与js交互时用到的方法，在js里直接调用的
     */
    private ParentQ parentQ;

    @JavascriptInterface
    public String getValue() {
        if (parentQ != null) {
            return parentQ.toString();
        }
        return "";
    }

    public void setParentQ(ParentQ parentQ) {
        this.parentQ = parentQ;
    }
}
