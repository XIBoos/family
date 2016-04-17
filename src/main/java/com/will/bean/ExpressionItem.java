package com.will.bean;

/**
 * Created by will on 2016/2/22.
 */
public class ExpressionItem {
    String key;
    int ID;
    public ExpressionItem(String key,int id){
        this.key=key;
        this.ID=id;
    }

    public String getKey() {
        return key;
    }

    public int getID(){
        return ID;
    }

}
