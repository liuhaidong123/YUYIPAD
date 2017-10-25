package com.technology.yuyipad.activity.Message;

import java.util.List;

/**
 * Created by wanyu on 2017/10/20.
 */

public interface IMessage {
    void onGetMessageSuccess(List<bean_MyMessage.RowsBean> listBean);
    void onGetMessageError(String msg);
}
