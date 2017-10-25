package com.technology.yuyipad.activity.Message;

/**
 * Created by wanyu on 2017/10/20.
 */

public class MessageListPresenter {
    MessageListModel model;
    public void getMessage(int st,int limit,IMessage iMessage){
        if (model==null){
            model=new MessageListModel();
        }
        model.getMessageList(st,limit,iMessage);
    }
}
