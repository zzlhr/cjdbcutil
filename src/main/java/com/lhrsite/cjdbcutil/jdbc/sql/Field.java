package com.lhrsite.cjdbcutil.jdbc.sql;

/**
 * 字段对象
 * @author lhr
 * @create 2018/1/11
 */
public class Field {


    private String filedName;



    public Field(String filedName){
        this.filedName = filedName;
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }
}
