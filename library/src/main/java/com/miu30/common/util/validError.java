package com.miu30.common.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;

import java.util.List;

/**
 * Created by Murphy on 2018/10/13.
 */
public class validError {
    private static final String TAG = "validError";

    public static String getFailedFieldNames(Context context, List<ValidationError> errors) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (ValidationError error : errors) {
                View view = error.getView();
                TextView textView = view instanceof EditText
                        ? (EditText) view
                        : (TextView) view;
                String s = error.getCollatedErrorMessage(context);
                String fieldName = "";
                if(!TextUtils.isEmpty(textView.getHint())){
                    fieldName = textView.getHint().toString().toUpperCase().replaceAll(" ", "_");
                }

                stringBuilder.append(fieldName).append(s);
            }
            return stringBuilder.toString();
        }catch (Exception e){
            return "";
        }

    }

    /*
     * 这里我们只需要返回第一个错误信息即可
     */
    public static String getFailedContent(Context context,List<ValidationError> errors) {
        String result = "请确认提交的信息是否有误";
        try {
            if(errors != null && errors.size() > 0){
                View view = errors.get(0).getView();
                TextView textView = view instanceof EditText
                        ? (EditText) view
                        : (TextView) view;
                result = errors.get(0).getCollatedErrorMessage(context);
                if("This field is required".equals(result) && !TextUtils.isEmpty(textView.getHint())){
                    result = textView.getHint().toString().toUpperCase().replaceAll(" ", "_");
                }
                if(result != null && result.contains("\n") && result.split("\n").length > 1){
                    result = result.split("\n")[1];
                }
            }
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
        return result;
    }
}
