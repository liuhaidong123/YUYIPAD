package com.technology.yuyipad.bean.SelectDoctor;

import java.util.List;

/**
 * Created by liuhaidong on 2017/3/24.
 */

public class Root {
    private List<Result> result;

    private String code;

    public void setResult(List<Result> result){
        this.result = result;
    }
    public List<Result> getResult(){
        return this.result;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
}
