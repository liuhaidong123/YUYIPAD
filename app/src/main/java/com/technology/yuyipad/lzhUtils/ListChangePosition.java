package com.technology.yuyipad.lzhUtils;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by wanyu on 2017/10/12.
 */

public class ListChangePosition {
   static ListChangePosition lChange;
    private ListChangePosition(){

    }
    public static ListChangePosition getInstance(){
        if (lChange==null){
            lChange=new ListChangePosition();
        }
        return lChange;
    }

    //元素中第一个与指定的pos的互换
    public <T>void changeList(List<T>list,int oldPosition,int  newPosition){
        if (list==null){
            throw new IllegalStateException("ListChangePosition类中List集合交换方法错误：您传递的list不能为null，请检查");
        }
//        T tempElement = list.get(oldPosition);
//        // 向前移动，前面的元素需要向后移动
//        if(oldPosition < newPosition){
//            for(int i = oldPosition; i < newPosition; i++){
//                list.set(i, list.get(i + 1));
//            }
//            list.set(newPosition, tempElement);
//        }
//        // 向后移动，后面的元素需要向前移动
//        if(oldPosition > newPosition){
//            for(int i = oldPosition; i > newPosition; i--){
//                list.set(i, list.get(i - 1));
//            }
//            list.set(newPosition, tempElement);
//        }
        T oldElement=list.get(oldPosition);
        T newElement=list.get(newPosition);
        list.set(newPosition,oldElement);
        list.set(oldPosition,newElement);
    }
}
