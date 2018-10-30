package com.technology.yuyipad.bean;

import java.util.List;

/**
 * Created by liuhaidong on 2018/8/22.
 */

public class DoctorRoot {
    private int code;

    private List<PhysicianList> physicianList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<PhysicianList> getPhysicianList() {
        return physicianList;
    }

    public void setPhysicianList(List<PhysicianList> physicianList) {
        this.physicianList = physicianList;
    }
}
