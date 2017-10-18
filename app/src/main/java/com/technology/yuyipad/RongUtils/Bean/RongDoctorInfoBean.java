package com.technology.yuyipad.RongUtils.Bean;

/**
 * Created by wanyu on 2017/10/11.
 */
//医生实体类
public class RongDoctorInfoBean {

    /**
     * id : 9
     * TrueName : 刘诗诗
     * Avatar : /static/image/2017/3/29/1490755041901.jpg
     */

    private String id;
    private String TrueName;
    private String Avatar;
    /**
     * code : 0
     * PermissionInfo : true
     * token : wR9ZDDuSHHXdXWd9uIqf3IuZqhNeP62jUkZowKgPQxuWVR/N71VJ9mdM5K0HZDaZb16sV0QxBE+E5LeCeESZ+A6RxRrPS2w8
     */

    private String code;
    private boolean PermissionInfo;
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrueName() {
        return TrueName;
    }

    public void setTrueName(String TrueName) {
        this.TrueName = TrueName;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String Avatar) {
        this.Avatar = Avatar;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String  code) {
        this.code = code;
    }

    public boolean isPermissionInfo() {
        return PermissionInfo;
    }

    public void setPermissionInfo(boolean PermissionInfo) {
        this.PermissionInfo = PermissionInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
