package com.technology.yuyipad.activity.Message;

import com.technology.yuyipad.bean.UnReadMsgBean;

import java.util.List;

/**
 * Created by wanyu on 2017/3/30.
 */
//获取最新10条消息
public class bean_MyMessage {

    /**
     * total : 3
     * rows : [{"createTimeString":"2017-08-24 11:49:35","personalId":13717883005,"msgType":2,"updateTimeString":"","isRead":true,"content":"挂号成功：李朋伟,2017-08-24下午,涿州市中医医院-内科-呼吸内科门诊-孙思邈主治医生,请准时就诊","referId":38,"id":44,"operation":null},{"createTimeString":"2017-08-24 11:53:02","personalId":13717883005,"msgType":2,"updateTimeString":"","isRead":false,"content":"挂号成功：李朋伟,2017-08-24下午,涿州市中医医院-内科-呼吸内科门诊-李时珍主治医生,请准时就诊","referId":39,"id":45,"operation":null},{"createTimeString":"2017-08-24 11:53:14","personalId":13717883005,"msgType":2,"updateTimeString":"","isRead":false,"content":"挂号成功：李朋伟,2017-08-27下午,涿州市中医医院-内科-呼吸内科门诊-孙思邈主治医生,请准时就诊","referId":40,"id":46,"operation":null}]
     * colmodel : []
     */

    private int total;
    private List<RowsBean> rows;
    private List<?> colmodel;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public List<?> getColmodel() {
        return colmodel;
    }

    public void setColmodel(List<?> colmodel) {
        this.colmodel = colmodel;
    }

    public static class RowsBean {
        /**
         * createTimeString : 2017-08-24 11:49:35
         * personalId : 13717883005
         * msgType : 2
         * updateTimeString :
         * isRead : true
         * content : 挂号成功：李朋伟,2017-08-24下午,涿州市中医医院-内科-呼吸内科门诊-孙思邈主治医生,请准时就诊
         * referId : 38
         * id : 44
         * operation : null
         */

        private String createTimeString;
        private long personalId;
        private int msgType;
        private String updateTimeString;
        private boolean isRead;
        private String content;
        private int referId;
        private int id;
        private Object operation;

        public String getCreateTimeString() {
            return createTimeString;
        }

        public void setCreateTimeString(String createTimeString) {
            this.createTimeString = createTimeString;
        }

        public long getPersonalId() {
            return personalId;
        }

        public void setPersonalId(long personalId) {
            this.personalId = personalId;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public String getUpdateTimeString() {
            return updateTimeString;
        }

        public void setUpdateTimeString(String updateTimeString) {
            this.updateTimeString = updateTimeString;
        }

        public boolean isIsRead() {
            return isRead;
        }

        public void setIsRead(boolean isRead) {
            this.isRead = isRead;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getReferId() {
            return referId;
        }

        public void setReferId(int referId) {
            this.referId = referId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getOperation() {
            return operation;
        }

        public void setOperation(Object operation) {
            this.operation = operation;
        }
    }
}
